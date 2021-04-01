package github.exia1771.deploy.common.service.impl;

import cn.hutool.core.io.FileUtil;
import github.exia1771.deploy.common.entity.FileRequest;
import github.exia1771.deploy.common.entity.dto.FileDTO;
import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.props.FileProperties;
import github.exia1771.deploy.common.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

	private static final String contextPath = System.getProperty("user.dir");
	private static final String HOST_PORT_SEPARATOR = ":";
	private static final String SCHEME_SEPARATOR = "://";

	private final FileProperties properties;
	private final HttpServletRequest request;

	public FileServiceImpl(HttpServletRequest request, FileProperties properties) {
		this.request = request;
		this.properties = properties;
	}

	private void validateFile(MultipartFile file, FileRequest request) {
		if (request.getMaxSize() != null) {
			long size = file.getSize();
			if (size > request.getMaxSize()) {
				throw new ServiceException("文件的大小不能超过" + (request.getMaxSize() / 1024 / 1024) + "M");
			}
		}

		if (request.getFileType() != null) {
			String contentType = file.getContentType();
			if (!request.getFileType().contains(contentType)) {
				throw new ServiceException("文件的类型必须为" + request.getFileType());
			}

		}

	}


	@Override
	public String getScheme() {
		return properties.getScheme() + SCHEME_SEPARATOR;
	}

	@Override
	public FileDTO upload(MultipartFile file, FileRequest fileRequest) {
		validateFile(file, fileRequest);

		String root = properties.getUpload().getRoot();

		String workPath = root == null ? contextPath : root;
		String fileName = fileRequest.getFileName() == null ? file.getName() : fileRequest.getFileName();
		File filePath = new File(workPath + fileRequest.getDirectory(), fileName);

		if (!filePath.getParentFile().exists()) {
			FileUtil.mkParentDirs(filePath);
		}

		try {
			file.transferTo(filePath);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}

		String mappingUrl = properties.getUpload().getMappingUrl();
		return new FileDTO() {{
			setFileName(fileName);
			setUrl(mappingUrl + fileRequest.getDirectory() + fileName);
		}};
	}

	@Override
	public String getServerAddress() {
		return request.getServerName() + HOST_PORT_SEPARATOR + request.getServerPort();
	}

	@Override
	public FileDTO download(String url, FileRequest request) {
		return null;
	}
}
