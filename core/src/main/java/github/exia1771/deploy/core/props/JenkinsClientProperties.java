package github.exia1771.deploy.core.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jenkins-client")
public class JenkinsClientProperties {

	private String host;
	private String admin;
	private String password;

}
