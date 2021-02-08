package github.exia1771.deploy.core.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "deploy.docker")
public class DockerProperties {

    private String username;
    private String password;
    private String email;
    private String serverAddress;

}
