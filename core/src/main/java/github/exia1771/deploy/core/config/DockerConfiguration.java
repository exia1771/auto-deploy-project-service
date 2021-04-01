package github.exia1771.deploy.core.config;

import com.alibaba.fastjson.JSONObject;
import github.exia1771.deploy.core.props.DockerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@EnableConfigurationProperties(DockerProperties.class)
@Configuration
public class DockerConfiguration {

	private final DockerProperties properties;
	private static final RestTemplate TEMPLATE = new RestTemplate();

	public DockerConfiguration(DockerProperties properties) {
		this.properties = properties;
		testConnection();
	}

	public void testConnection() {
		final String version = "/version";
		new Thread(() -> {
			while (true) {
				try {
					TEMPLATE.getForObject(properties.getServerAddress() + version, JSONObject.class);
					break;
				} catch (ResourceAccessException e) {
					log.error("Docker Remote Api 无法正确连接");
				}
			}
		}).start();
	}

	@Bean
	public RestTemplate restTemplate() {
		return TEMPLATE;
	}

}


