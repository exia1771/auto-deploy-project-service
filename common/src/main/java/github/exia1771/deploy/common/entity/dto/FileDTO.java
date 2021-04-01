package github.exia1771.deploy.common.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileDTO extends AbstractDTO<String> {

	public static final String FILE_EXT_DELIMITER = ".";
	public static final String FILE_SEPARATOR = "/";

	private String fileName;
	private String url;

}
