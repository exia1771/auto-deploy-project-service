package github.exia1771.deploy.core.service.jenkins;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.offbytwo.jenkins.JenkinsServer;
import github.exia1771.deploy.common.util.StringUtil;
import github.exia1771.deploy.core.dto.ProjectConfigDTO;
import github.exia1771.deploy.core.entity.Container;
import github.exia1771.deploy.core.entity.ImageTemplate;
import github.exia1771.deploy.core.entity.Project;
import github.exia1771.deploy.core.entity.ProjectContainer;
import github.exia1771.deploy.core.service.ImageTemplateService;
import github.exia1771.deploy.core.service.ProjectConfigService;
import github.exia1771.deploy.core.service.ProjectService;
import github.exia1771.deploy.core.service.docker.ContainerService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MavenDockerJenkins extends AbstractJenkinsService {

	public MavenDockerJenkins(JenkinsServer server, ProjectService projectService, ProjectConfigService projectConfigService, ImageTemplateService imageTemplateService, ContainerService containerService) {
		super(server, projectService, projectConfigService, imageTemplateService, containerService);
	}

	@Override
	protected Resource getTemplateFile() {
		return new ClassPathResource("template/git-maven-docker.xml");
	}

	@Override
	public String getBuildType() {
		return "maven";
	}

	@Override
	protected Map<String, Object> getVariables(ProjectContainer projectContainer) {
		Map<String, Object> map = new HashMap<>();
		Project project = projectService.findById(projectContainer.getProjectId());
		map.put("gitUrl", project.getGitUrl());
		map.put("tag", projectContainer.getGitTag());
		String buildCommand = projectContainer.getBuildCommand();
		if (StringUtil.isNotBlank(buildCommand) && buildCommand.startsWith("mvn")) {
			buildCommand = buildCommand.replaceAll("mvn", "");
		}
		map.put("targets", buildCommand);
		String dockerShellCommand = getDockerShellCommand(projectContainer, project);
		String otherDockerShellCommand = getOtherDockerShellCommand(projectContainer);
		String runShellCommand = getRunShellCommand(projectContainer);
		map.put("shell", dockerShellCommand + otherDockerShellCommand + runShellCommand);
		return map;
	}

	private String getDockerShellCommand(ProjectContainer projectContainer, Project project) {
		ProjectConfigDTO config = projectConfigService.findByProjectIdAndNamespaceId(projectContainer.getProjectId(), projectContainer.getNamespaceId());
		ImageTemplate imageTemplate = imageTemplateService.findById(project.getTemplateId());
		StringBuilder builder = new StringBuilder();
		builder.append("cd ./target")
				.append(System.lineSeparator());

		ifExistedContainerStop(projectContainer, builder);

		builder.append("docker run ")
				.append(" -d ")
				.append(" --name=")
				.append(projectContainer.getDockerContainerId());
		if (StringUtil.isNotBlank(config.getPort())) {
			JSONArray ports = JSON.parseArray(config.getPort());
			builder.append(" -p ");
			for (int i = 0; i < ports.size(); i++) {
				JSONObject port = ports.getJSONObject(i);
				builder.append(" ")
						.append(port.getString("host"))
						.append(":")
						.append(port.getString("container"))
						.append(" ");
			}
		}
		if (config.getMemory() != 0) {
			builder.append(" -m ")
					.append(config.getMemory())
					.append("M");
		}
		if (config.getCore() != 0) {
			builder.append(" --cpuset-cpus=")
					.append("0-")
					.append(config.getCore() - 1);
		}

		builder.append(" ")
				.append(imageTemplate.getDockerImageId());
		return builder.append(System.lineSeparator()).toString();
	}

	private String getOtherDockerShellCommand(ProjectContainer projectContainer) {
		StringBuilder builder = new StringBuilder();
		if (StringUtil.isNotBlank(projectContainer.getCpToContainerPath())) {
			String[] split = projectContainer.getCpToContainerPath().split("\\s+");
			builder.append("docker cp ")
					.append(split[0])
					.append(" ")
					.append(projectContainer.getDockerContainerId())
					.append(":")
					.append(split[1]);
		}
		return builder.append(System.lineSeparator()).toString();
	}

	private void ifExistedContainerStop(ProjectContainer projectContainer, StringBuilder builder) {
		StringBuilder filters = new StringBuilder();
		filters.append("{\"name\":[\"")
				.append(projectContainer.getDockerContainerId())
				.append("\"]}");

		List<Container> containerList = containerService.findByParam(filters.toString(), containerService.getURL() + "filters={filters}");
		if (!containerList.isEmpty()) {
			JSONArray array = new JSONArray();
			array.addAll(containerList);
			for (int i = 0; i < array.size(); i++) {
				JSONObject temp = array.getJSONObject(i);
				List<String> names = temp.getJSONArray("names").toJavaList(String.class);
				for (String name : names) {
					if (name.contains(projectContainer.getDockerContainerId())) {
						builder.append(" docker stop ")
								.append(projectContainer.getDockerContainerId())
								.append(System.lineSeparator())
								.append(" docker rm ")
								.append(projectContainer.getDockerContainerId())
								.append(System.lineSeparator());
						break;
					}
				}
			}
		}
	}

	private String getRunShellCommand(ProjectContainer projectContainer) {
		StringBuilder builder = new StringBuilder();
		if (StringUtil.isNotBlank(projectContainer.getRunCommand())) {
			builder.append("docker exec -it ")
					.append(projectContainer.getDockerContainerId())
					.append(" ")
					.append(projectContainer.getRunCommand())
					.append(System.lineSeparator());
		}
		return builder.toString();
	}
}
