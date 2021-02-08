package github.exia1771.deploy.common.config;

import github.exia1771.deploy.common.filter.TokenInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String[] METHODS = {"GET", "POST", "PUT", "DELETE"};
    public static final String ALL_MAPPING = "/**";
    public static final String ALL_ORIGINAL = "*";
    private static final String PUBLIC_USER_MAPPING = "/user/public/**";
    private static final String FILE_RESOURCE_PREFIX = "file:/";
    private static final String FILE_RESOURCE_SUFFIX = "/";
    @Value("${server.servlet.session.cookie.domain:http://localhost:8081}")
    private String domain;

    @Value("${file.upload.mapping-url}")
    private String FILE_MAPPING_PATH;
    @Value("${file.upload.root}")
    private String FILE_STORE_PATH;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(ALL_MAPPING)
                .allowedOrigins(ALL_ORIGINAL)
                .allowCredentials(true)
                .allowedMethods(METHODS);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(FILE_MAPPING_PATH + ALL_MAPPING)
                .addResourceLocations(FILE_RESOURCE_PREFIX + FILE_STORE_PATH + FILE_RESOURCE_SUFFIX);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        TokenInterceptor interceptor = new TokenInterceptor();
        interceptor.setDOMAIN(domain);
        registry.addInterceptor(interceptor)
                .addPathPatterns(ALL_MAPPING)
                .excludePathPatterns(PUBLIC_USER_MAPPING)
                .excludePathPatterns(FILE_MAPPING_PATH + ALL_MAPPING);
    }


}
