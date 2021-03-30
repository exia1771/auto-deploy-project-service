package github.exia1771.deploy.core.config;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import github.exia1771.deploy.core.props.JenkinsClientProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@EnableConfigurationProperties({JenkinsClientProperties.class})
@AllArgsConstructor
public class JenkinsClientConfiguration {

	private final JenkinsClientProperties properties;

	@Bean(name = "jenkinsClient")
	public JenkinsHttpClient jenkinsHttpClient() throws URISyntaxException {
		return new JenkinsHttpClient(new URI(properties.getHost()),
				properties.getAdmin(),
				properties.getPassword());
	}

	@Bean(name = "jenkinsServer")
	public JenkinsServer jenkinsServer(JenkinsHttpClient jenkinsClient) {
		return new JenkinsServer(jenkinsClient);
	}

}
