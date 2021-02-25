package github.exia1771.deploy.common.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    @NestedConfigurationProperty
    private Upload upload;

    @Data
    public static class Upload{

        private String root;
        private String mappingUrl;

    }
}
