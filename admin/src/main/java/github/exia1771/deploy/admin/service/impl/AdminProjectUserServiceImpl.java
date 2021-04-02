package github.exia1771.deploy.admin.service.impl;

import github.exia1771.deploy.admin.service.AdminProjectUserService;
import github.exia1771.deploy.core.entity.ProjectUser;
import github.exia1771.deploy.core.service.ProjectUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminProjectUserServiceImpl implements AdminProjectUserService {

	private final ProjectUserService projectUserService;

	@Override
	public void batchUpdate(String projectId, List<String> userIdList) {
		projectUserService.batchUpdate(projectId, userIdList);
	}

	@Override
	public List<ProjectUser> findUsersByProjectId(String projectId) {
		return projectUserService.findByProjectId(projectId);
	}
}
