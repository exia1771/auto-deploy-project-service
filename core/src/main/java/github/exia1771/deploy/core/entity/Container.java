package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.core.service.docker.AbstractWebEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Container extends AbstractWebEntity {

    private String id;
    private List<String> names;
    private String image;
    private String imageId;
    private List<Port> ports;
    private String state;
    private String status;


}
