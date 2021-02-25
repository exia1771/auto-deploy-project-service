package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.common.entity.dto.AbstractDTO;
import github.exia1771.deploy.core.Pageable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
public class ImageTemplate extends AbstractEntity<String> implements Pageable {

    @NotNull
    @NotBlank
    @Size(min = 6, max = 255)
    private String templateName;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 255)
    private String templateTag;

    @NotNull
    @NotBlank
    @Size(min = 6, max = 255)
    private String dockerImageId;
    private transient Long current;
    private transient Long size;

    @Override
    public AbstractDTO<String> toDTO() {
        return null;
    }
}
