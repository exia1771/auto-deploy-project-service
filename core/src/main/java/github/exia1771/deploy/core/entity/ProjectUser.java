package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.common.entity.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectUser extends AbstractEntity<String> {

    private String projectId;
    private String userId;

    @Override
    public AbstractDTO<String> toDTO() {
        return null;
    }
}
