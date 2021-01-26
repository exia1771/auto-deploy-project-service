package github.exia1771.deploy.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String[] METHODS = {"GET", "POST", "PUT", "DELETE"};
    private static final String ALL_MAPPING = "/**";
    private static final String ALL_ORIGINAL = "*";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(ALL_MAPPING)
                .allowedOrigins(ALL_ORIGINAL)
                .allowCredentials(true)
                .allowedMethods(METHODS);
    }
}
