package github.exia1771.deploy.common.entity;

import github.exia1771.deploy.common.entity.dto.DeptDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class Dept extends AbstractEntity<String> {

	private String name;

	@Override
	public DeptDTO toDTO() {
		DeptDTO deptDTO = new DeptDTO();
		BeanUtils.copyProperties(this, deptDTO);
		return deptDTO;
	}
}
