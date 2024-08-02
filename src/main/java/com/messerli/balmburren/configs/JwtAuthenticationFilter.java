package com.messerli.balmburren.configs;

import com.messerli.balmburren.services.serviceImpl.JwtService;
import io.jsonwebtoken.io.Encoders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.io.Decoders;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.util.Base64;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private String jwt;

    public JwtAuthenticationFilter(
        JwtService jwtService,
        UserDetailsService userDetailsService,
        HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
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


//            if (token2 == null) {
//                filterChain.doFilter(request, response);
//                return;
//            }

        } else {

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                String jwt1 = authHeader.substring(7);
                jwt = authHeader.substring(7);
//                jwt = jwtService.extractUsername(jwt1);
            }
            else {
                filterChain.doFilter(request, response);
                return;
            }
        }

        try {

            final String userEmail = jwtService.extractUsername(jwt);

//            final String userEmail = jwt;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
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
//                        jwt = URLDecoder.decode(cookie.getValue()).trim();
                        jwt = cookie.getValue();
//                        JSONObject jsonObj = new JSONObject(jwt);
//                        name = jsonObj.getString("token");
                    }
                }

            }
        }
//        return name;
    }


}
