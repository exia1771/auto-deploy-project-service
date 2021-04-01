package github.exia1771.deploy.common.entity;

import lombok.Data;

import java.util.List;

@Data
public class FileRequest {

	private String fileName;
	private Long maxSize;
	private List<String> fileType;
	private String directory;

}
