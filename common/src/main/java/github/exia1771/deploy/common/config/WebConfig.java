package github.exia1771.deploy.common.config;

import github.exia1771.deploy.common.filter.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String[] METHODS = {"GET", "POST", "PUT", "DELETE"};
    public static final String ALL_MAPPING = "/**";
    public static final String ALL_ORIGINAL = "*";
    private static final String PUBLIC_USER_MAPPING = "/user/public/**";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(ALL_MAPPING)
                .allowedOrigins(ALL_ORIGINAL)
                .allowCredentials(true)
                .allowedMethods(METHODS);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor())
                .addPathPatterns(ALL_MAPPING)
                .excludePathPatterns(PUBLIC_USER_MAPPING);
    }
}
