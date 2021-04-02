package github.exia1771.deploy.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import github.exia1771.deploy.common.service.BaseService;
import github.exia1771.deploy.core.dto.ProjectContainerDTO;
import github.exia1771.deploy.core.entity.ProjectContainer;
import github.exia1771.deploy.core.entity.ProjectContainerSearch;
import github.exia1771.deploy.core.service.docker.ContainerService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProjectContainerService extends BaseService<String, ProjectContainer> {

	List<String> findRepositoryTags(String projectId, String keyword);

	IPage<ProjectContainerDTO> findBySpecificFields(ProjectContainerSearch search);

	String getBuildLog(String projectContainerId);

	String stopBuild(String projectContainerId);

	String getContainerLog(String projectContainerId, long timestamp);

	void getContainerLogStream(String projectContainerId, long timestamp, HttpServletResponse response);

	ContainerService.Status restartContainer(String projectContainerId);
}
