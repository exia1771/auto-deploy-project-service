package github.exia1771.deploy.common.entity;

import github.exia1771.deploy.common.entity.dto.AbstractDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class AbstractEntity<T extends Serializable> {

    private T id;
    private String creatorId;
    private Date creationTime;
    private String updaterId;
    private Date updateTime;

    public AbstractEntity() {
    }

    public abstract AbstractDTO<T> toDTO();

}
