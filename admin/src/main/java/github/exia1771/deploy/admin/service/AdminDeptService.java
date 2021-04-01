package github.exia1771.deploy.admin.service;


import github.exia1771.deploy.common.entity.Dept;

public interface AdminDeptService extends SearchPagedService<Dept, String> {

	void deleteById(String id);

}
