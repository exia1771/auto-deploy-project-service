package github.exia1771.deploy.common.service;

import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.entity.dto.UserDTO;

public interface UserService extends BaseService<Long, User> {

    Boolean isExistedName(String name);

    UserDTO submit(User user);

    UserDTO login(User user);

    UserDTO login(String token);

    User findByName(String name);
}
