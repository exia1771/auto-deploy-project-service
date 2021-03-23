package github.exia1771.deploy.core.dto;

import github.exia1771.deploy.common.entity.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectConfigDTO extends AbstractDTO<String> {

    private String namespace;
    private long memory;
    private long storage;
    private long core;
    private String port;

}
