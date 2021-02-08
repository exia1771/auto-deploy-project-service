package github.exia1771.deploy.core.config;

import github.exia1771.deploy.core.props.DockerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@EnableConfigurationProperties(DockerProperties.class)
@Configuration
public class DockerConfiguration {

    private final DockerProperties properties;

    public DockerConfiguration(DockerProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}


