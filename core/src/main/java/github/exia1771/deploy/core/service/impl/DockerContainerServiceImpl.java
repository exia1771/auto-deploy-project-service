package github.exia1771.deploy.core.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import github.exia1771.deploy.core.entity.Container;
import github.exia1771.deploy.core.entity.DockerRemoteApiParam;
import github.exia1771.deploy.core.props.DockerProperties;
import github.exia1771.deploy.core.service.ContainerService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    public JSONObject inspect(String id) {
        return null;
    }
}
