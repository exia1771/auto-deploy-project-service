package github.exia1771.deploy.core.entity;


import github.exia1771.deploy.common.entity.AbstractEntity;
import github.exia1771.deploy.core.dto.NamespaceDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class Namespace extends AbstractEntity<String> {

	private String name;

	@Override
	public NamespaceDTO toDTO() {
		NamespaceDTO namespaceDTO = new NamespaceDTO();
		BeanUtils.copyProperties(this, namespaceDTO);
		return namespaceDTO;
	}
}
