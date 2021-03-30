package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.core.service.docker.AbstractWebEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class Image extends AbstractWebEntity {

    private String id;
    private List<String> repoTags;
    private Map<String, Object> labels;
    private String parentId;

}
