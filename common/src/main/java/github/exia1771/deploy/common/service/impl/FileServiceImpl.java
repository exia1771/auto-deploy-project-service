package github.exia1771.deploy.common.service.impl;

import cn.hutool.core.io.FileUtil;
import github.exia1771.deploy.common.entity.FileRequest;
import github.exia1771.deploy.common.entity.dto.FileDTO;
import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.upload.root}")
    private String rootPath;

    @Value("${file.upload.mapping-url}")
    private String mappingUrl;
    private static final String contextPath = System.getProperty("user.dir");
    private final HttpServletRequest request;

    public FileServiceImpl(HttpServletRequest request) {
        this.request = request;
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
    public FileDTO upload(MultipartFile file, FileRequest fileRequest) {
        validateFile(file, fileRequest);


        String workPath = rootPath == null ? contextPath : rootPath;
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

        String serverAddress = request.getServerName() + ":" + request.getServerPort() + mappingUrl;
        return new FileDTO() {{
            setFileName(fileName);
            setUrl(serverAddress + fileRequest.getDirectory() + fileName);
        }};
    }
}
