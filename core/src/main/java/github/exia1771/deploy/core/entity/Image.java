package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.core.abs.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class Image extends AbstractEntity {

    private String id;
    private List<String> repoTags;
    private Map<String, Object> labels;
    private String parentId;

}
