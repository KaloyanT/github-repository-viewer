package com.kaloyantodorov.githubrepositoryviewer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class GitHubRepositoryViewerConfig {

    @Bean
    public RestTemplate gitHubRestTemplate(@Value("${github.api.connectionTimeout}") int connectionTimeout, @Value("${github.api.readTimeout}") int readTimeout) {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(connectionTimeout);
        simpleClientHttpRequestFactory.setReadTimeout(readTimeout);
        RestTemplate gitHubRestTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        return gitHubRestTemplate;
    }

}
