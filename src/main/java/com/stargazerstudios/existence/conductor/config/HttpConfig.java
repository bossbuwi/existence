package com.stargazerstudios.existence.conductor.config;

import com.stargazerstudios.existence.conductor.constants.EnumHttpHeader;
import com.stargazerstudios.existence.conductor.constants.EnumHttpRequestMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class HttpConfig implements WebMvcConfigurer {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add("*");

        List<String> allowedMethods = List.of(
                EnumHttpRequestMethod.HEAD.getValue(),
                EnumHttpRequestMethod.GET.getValue(),
                EnumHttpRequestMethod.POST.getValue(),
                EnumHttpRequestMethod.PUT.getValue(),
                EnumHttpRequestMethod.DELETE.getValue(),
                EnumHttpRequestMethod.PATCH.getValue()
        );

        List<String> allowedHeaders = List.of(
                EnumHttpHeader.AUTHORIZATION.getValue(),
                EnumHttpHeader.CACHE_CONTROL.getValue(),
                EnumHttpHeader.CONTENT_TYPE.getValue()
        );

        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        // configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.addExposedHeader(EnumHttpHeader.CONTENT_DISPOSITION.getValue());

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
