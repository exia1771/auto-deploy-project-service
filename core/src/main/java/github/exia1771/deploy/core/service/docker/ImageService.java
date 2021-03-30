package github.exia1771.deploy.core.service.docker;

import github.exia1771.deploy.core.entity.DockerRemoteApiParam;
import github.exia1771.deploy.core.entity.Image;

import java.util.List;

public interface ImageService extends RemoteApi<Image, DockerRemoteApiParam> {


    List<Image> findByTag(String tag);

}
