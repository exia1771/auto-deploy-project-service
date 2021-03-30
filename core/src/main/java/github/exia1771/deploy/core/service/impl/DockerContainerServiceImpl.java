package github.exia1771.deploy.core.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import github.exia1771.deploy.core.entity.Container;
import github.exia1771.deploy.core.entity.DockerRemoteApiParam;
import github.exia1771.deploy.core.props.DockerProperties;
import github.exia1771.deploy.core.service.docker.ContainerService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class DockerContainerServiceImpl extends DockerContainerEngine implements ContainerService {

	private static final String CONTAINER_API_PREFIX = "containers/json?";

	public DockerContainerServiceImpl(DockerProperties properties, RestTemplate restTemplate) {
		super(properties, restTemplate);
	}

	@Override
	public String getURL() {
		return getServerAddress() + CONTAINER_API_PREFIX;
	}

	@Override
	public List<Container> findByParam(DockerRemoteApiParam param) {
		String url = getRequestURL(param);
		return getRestTemplate().getForObject(url, JSONArray.class).toJavaList(Container.class);
	}

	@Override
	public List<Container> findByParam(Object param, String url) {
		return getRestTemplate().getForObject(url, JSONArray.class, param).toJavaList(Container.class);
	}

	@Override
	public JSONObject inspect(String id) {
		return null;
	}


	@Override
	public String getContainerLogs(String containerNameOrId, long since) throws IOException {
		String url = getServerAddress() + "container/" + containerNameOrId + "/logs?stdout=true&stderr=true&since=" + since;
		try {
			ResponseEntity<Resource> responseEntity = getRestTemplate().getForEntity(url, Resource.class);
			Resource body = responseEntity.getBody();
			if (body == null) {
				return "";
			}
			InputStream inputStream = body.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder builder = new StringBuilder();
			reader.lines().forEach(builder::append);
			return builder.toString();
		} catch (HttpClientErrorException.NotFound e) {
			return "";
		}
	}

	@Override
	public Status restart(String containerNameOrId) {
		String url = getServerAddress() + "container/" + containerNameOrId + "/restart";
		try {
			getRestTemplate().postForObject(url, null, String.class);
			return Status.SUCCESS;
		} catch (HttpClientErrorException.NotFound e) {
			return Status.NOT_FOUNT;
		} catch (Exception e) {
			return Status.OTHER;
		}
	}
}
