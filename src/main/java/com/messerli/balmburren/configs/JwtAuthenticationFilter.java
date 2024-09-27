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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Optional;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
//    private final MyUserDetails myUserDetails;

    private final UserService userService;
    private String jwt;
    @Autowired
    private UsersRoleRepo usersRoleRepo;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver,
            MyUserDetails myUserDetails, UserService userService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
//        this.myUserDetails = myUserDetails;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");



        if (request.getCookies()!= null) {

            checkForCookie(request);
            log.info("The JwtRequestfilter with cookie is in process with token: " + jwt);


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
//            Collection<? extends GrantedAuthority> roles = myUserDetails.getAuthorities();
            log.info("Roles: " + roles);

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










//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            final String jwt = authHeader.substring(7);
//            final String userEmail = jwtService.extractUsername(jwt);
//
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (userEmail != null && authentication == null) {
//                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
//
//                if (jwtService.isTokenValid(jwt, userDetails)) {
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            userDetails.getAuthorities()
//                    );
//
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//            }
//
//            filterChain.doFilter(request, response);
//        } catch (Exception exception) {
//            handlerExceptionResolver.resolveException(request, response, null, exception);
//        }
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
