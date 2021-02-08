package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.core.abs.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Container extends AbstractEntity {

    private String id;
    private List<String> names;
    private String image;
    private String imageId;
    private List<Port> ports;
    private String state;
    private String status;


}
