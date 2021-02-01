package github.exia1771.deploy.common.entity.dto;

import lombok.Data;

@Data
public class AbstractDTO<T> {
    private T id;
}
