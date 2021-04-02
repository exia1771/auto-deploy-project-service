package github.exia1771.deploy.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.admin.entity.SearchEntity;
import github.exia1771.deploy.admin.service.AdminProjectService;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.core.entity.Project;
import github.exia1771.deploy.core.service.ProjectConfigService;
import github.exia1771.deploy.core.service.ProjectService;
import github.exia1771.deploy.core.service.ProjectUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminProjectServiceImpl implements AdminProjectService {

	private final ProjectService projectService;
	private final ProjectConfigService projectConfigService;
	private final ProjectUserService projectUserService;

	private static final String USERNAME_COLUMN = "username";
	private static final String NAME_COLUMN = "name";
	private static final String DESCRIPTION_COLUMN = "description";
	private static final String IDENTIFICATION_COLUMN = "identification";
	private static final String GIT_URL_COLUMN = "git_url";

	@Override
	public QueryWrapper<Project> getWrapper(SearchEntity entity) {
		QueryWrapper<Project> wrapper = new QueryWrapper<>();
		wrapper.like(USERNAME_COLUMN, entity.getSearchText());
		wrapper.or(q -> q.like(NAME_COLUMN, entity.getSearchText()));
		wrapper.or(q -> q.like(DESCRIPTION_COLUMN, entity.getSearchText()));
		wrapper.or(q -> q.like(IDENTIFICATION_COLUMN, entity.getSearchText()));
		wrapper.or(q -> q.like(GIT_URL_COLUMN, entity.getSearchText()));
		return wrapper;
	}

	@Override
	public BaseService<String, Project> getService() {
		return projectService;
	}

	@Override
	public void deleteById(String id) {
		//1.  删配置
		projectConfigService.deleteByProjectId(id);

		//2. 删工程用户映射
		projectUserService.deleteByProjectId(id);

		//3. 删自己
		projectService.deleteById(id);
	}
}
