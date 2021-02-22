package github.exia1771.deploy.core.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import github.exia1771.deploy.core.entity.DockerRemoteApiParam;
import github.exia1771.deploy.core.entity.Image;
import github.exia1771.deploy.core.props.DockerProperties;
import github.exia1771.deploy.core.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service("docker")
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
        JSONObject result;
        try {
            result = getRestTemplate().getForObject(getServerAddress() + "images" + "/" + id + "/json", JSONObject.class);
        } catch (HttpClientErrorException.NotFound e) {
            result = null;
        }
        return result;
    }


    @Override
    public List<Image> findByTag(String tag) {
        String url = getURL();
        List<Image> imageList = getRestTemplate().getForObject(url, JSONArray.class).toJavaList(Image.class);
        return imageList.stream().filter(image -> {
            List<String> repoTags = image.getRepoTags();
            boolean result = false;

            for (String repoTag : repoTags) {
                if (repoTag.contains(tag)) {
                    result = true;
                    break;
                }
            }

            return result;
        }).collect(Collectors.toList());
    }
}
