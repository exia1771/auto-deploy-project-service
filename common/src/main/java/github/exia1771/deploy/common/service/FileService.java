package github.exia1771.deploy.common.service;

import github.exia1771.deploy.common.entity.FileRequest;
import github.exia1771.deploy.common.entity.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileDTO upload(MultipartFile file, FileRequest request);

    default FileDTO upload(MultipartFile file) {
        return upload(file, null);
    }

    FileDTO download(String url, FileRequest request);

    default FileDTO download(String url) {
        return download(url, null);
    }
}
