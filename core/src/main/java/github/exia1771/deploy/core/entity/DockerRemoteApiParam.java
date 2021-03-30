package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.core.service.docker.AbstractRemoteApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DockerRemoteApiParam extends AbstractRemoteApiParam {

    private String id;
    private String name;

}
