package github.exia1771.deploy.common.entity;

import github.exia1771.deploy.common.entity.dto.AbstractDTO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class AbstractEntity<T extends Serializable> {

    private T id;
    private Long creatorId;
    private LocalDateTime creationTime = LocalDateTime.now();
    private Long updaterId;
    private LocalDateTime updateTime;

    public AbstractEntity() {
    }

    public abstract AbstractDTO<T> toDTO();

}
