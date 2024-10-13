package com.messerli.balmburren.configs;

import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.repositories.UsersRoleRepo;
import com.messerli.balmburren.services.JwtService;
import com.messerli.balmburren.services.MyUserDetails;
import com.messerli.balmburren.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.springframework.http.HttpHeaders;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private final UserService userService;
    private String jwt;
    private UsersRoleRepo usersRoleRepo;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver,
            UserService userService, UsersRoleRepo usersRoleRepo) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.userService = userService;
        this.usersRoleRepo = usersRoleRepo;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String requestUrl = request.getRequestURL().toString();

        // Forward requests not already pointing to localhost:8006/api
        if (requestUrl.contains("service.balmburren.net:80/api")) {
            String forwardUrl = "http://localhost:8006" + request.getRequestURI();

            // Use RestTemplate to forward the request internally to localhost
            RestTemplate restTemplate = new RestTemplate();

            try {
                // Forward the HTTP request to localhost:8006/api
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", request.getHeader("Authorization")); // Pass the Authorization header
                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<String> forwardResponse = restTemplate.exchange(
                        forwardUrl,
                        HttpMethod.valueOf(request.getMethod()),  // Match the request method
                        entity,
                        String.class
                );

                // Write the forwarded response back to the client
//                response.setStatus(forwardResponse.getStatusCodeValue());
                response.setStatus(forwardResponse.getStatusCode().value());
                response.getWriter().write(Objects.requireNonNull(forwardResponse.getBody()));
                return;

            } catch (Exception e) {
                log.error("Error forwarding request: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error forwarding request.");
                return;
            }
        }


        if (request.getCookies()!= null) {

            checkForCookie(request);

        } else {

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
            }
            else {
                filterChain.doFilter(request, response);
                return;
            }
        }

        try {

            final String userEmail = jwtService.extractUsername(jwt);
            Optional<User> user1 = userService.findUser(userEmail);
            MyUserDetails myUserDetails1 = new MyUserDetails(user1.get(), usersRoleRepo);

            Collection<? extends GrantedAuthority> roles = myUserDetails1.getAuthorities();


            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
//                            null,
                            roles,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("authToken: " + authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }



    }



    private void checkForCookie(HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        Cookie[] cookies = httpServletRequest.getCookies();
        String name = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("Cookie with name: " + cookie.getName());
                if (cookie.getName().equalsIgnoreCase("jwt")) {
                    if (cookie.getValue() != null) {
                        jwt = cookie.getValue();
                    }
                }

            }
        }
    }


}
