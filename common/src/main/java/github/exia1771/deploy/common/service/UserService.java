package github.exia1771.deploy.common.service;

import github.exia1771.deploy.common.entity.Password;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.entity.dto.FileDTO;
import github.exia1771.deploy.common.entity.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

public interface UserService extends BaseService<Long, User> {

    Boolean isExistedName(String name);

    UserDTO submit(User user) throws UnsupportedEncodingException;

    UserDTO login(User user) throws UnsupportedEncodingException;

    UserDTO login();

    void logout();

    User findByName(String name);

    UserDTO updateBasicInfo(User user);

    FileDTO uploadAvatar(MultipartFile file);

    UserDTO updateAvatar(User user);

    void changePassword(Password password);
}
