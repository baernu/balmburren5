package com.messerli.balmburren.configs;

import com.messerli.balmburren.exceptions.RestResponseStatusExceptionResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;

@Slf4j
@Component
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {

//    @Autowired
    @Qualifier("handlerExceptionResolver")
    private RestResponseStatusExceptionResolver resolver;

    public DelegatedAuthenticationEntryPoint(RestResponseStatusExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        resolver.resolveException(request, response, null, authException);
    }
}