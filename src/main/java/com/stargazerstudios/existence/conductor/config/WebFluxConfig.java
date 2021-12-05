package com.stargazerstudios.existence.conductor.config;

import com.stargazerstudios.existence.conductor.constants.WebFluxConstants;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {

    @Value("${ldap.server.uri}")
    private String ldapServer;

    @Bean
    public WebClient getWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, WebFluxConstants.CONNECTION_TIMEOUT)
                .responseTimeout(Duration.ofMillis(WebFluxConstants.RESPONSE_TIMEOUT))
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(WebFluxConstants.READ_TIMEOUT, TimeUnit.MILLISECONDS))
                            .addHandlerLast(new WriteTimeoutHandler(WebFluxConstants.WRITE_TIMEOUT, TimeUnit.MILLISECONDS));
                });

        return WebClient.builder()
                .baseUrl(ldapServer)
                .clientConnector(new ReactorClientHttpConnector(httpClient.wiretap(WebFluxConstants.WIRETAP_CLIENT)))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
