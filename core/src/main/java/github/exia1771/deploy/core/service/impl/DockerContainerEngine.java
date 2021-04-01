package github.exia1771.deploy.core.service.impl;

import github.exia1771.deploy.core.props.DockerProperties;
import github.exia1771.deploy.core.service.docker.ApplicationContainerEngine;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

@Data
public abstract class DockerContainerEngine implements ApplicationContainerEngine {

	private final DockerProperties properties;
	private final RestTemplate restTemplate;

	public DockerContainerEngine(DockerProperties properties, RestTemplate restTemplate) {
		this.properties = properties;
		this.restTemplate = restTemplate;
	}

	@Override
	public String getServerAddress() {
		String serverAddress = properties.getServerAddress();
		if (serverAddress.charAt(serverAddress.length() - 1) != '/') {
			serverAddress += '/';
		}
		return serverAddress;
	}
}
