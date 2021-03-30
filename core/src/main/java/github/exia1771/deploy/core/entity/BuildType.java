package github.exia1771.deploy.core.entity;

import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.core.dto.BuildTypeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class BuildType extends AbstractEntity<String> {

	private String name;

	@Override
	public BuildTypeDTO toDTO() {
		BuildTypeDTO buildTypeDTO = new BuildTypeDTO();
		BeanUtils.copyProperties(this, buildTypeDTO);
		return buildTypeDTO;
	}
}
