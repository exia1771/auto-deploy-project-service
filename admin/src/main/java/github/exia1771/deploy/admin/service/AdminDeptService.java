package github.exia1771.deploy.admin.service;


import github.exia1771.deploy.common.entity.Dept;
import github.exia1771.deploy.common.entity.User;

import java.util.List;

public interface AdminDeptService extends SearchPagedService<Dept, String> {

	List<User> findUserByDeptId(String id);

}
