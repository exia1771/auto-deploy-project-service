package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.core.abs.AbstractWebEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Mounts extends AbstractWebEntity {

    private String type;
    private String name;
    private String source;
    private String destination;

}
