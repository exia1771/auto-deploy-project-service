package github.exia1771.deploy.common.service;

import github.exia1771.deploy.common.entity.User;

public interface UserService extends BaseService<Long, User> {

    Boolean isExistedName(String name);

    String submit(User user);
}
