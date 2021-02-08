package github.exia1771.deploy.core.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import github.exia1771.deploy.core.entity.DockerRemoteApiParam;
import github.exia1771.deploy.core.entity.Image;
import github.exia1771.deploy.core.props.DockerProperties;
import github.exia1771.deploy.core.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DockerImageServiceImpl extends DockerContainerEngine implements ImageService {

    private static final String IMAGE_PREFIX = "images/json?";


    public DockerImageServiceImpl(DockerProperties properties, RestTemplate restTemplate) {
        super(properties, restTemplate);
    }


    @Override
    public String getURL() {
        return getServerAddress() + IMAGE_PREFIX;
    }

    @Override
    public List<Image> findByParam(DockerRemoteApiParam param) {
        String url = getRequestURL(param);
        return getRestTemplate().getForObject(url, JSONArray.class).toJavaList(Image.class);
    }

    @Override
    public JSONObject inspect(String id) {
        return getRestTemplate().getForObject(getServerAddress() + "images" + "/" + id + "/json", JSONObject.class);
    }
}
