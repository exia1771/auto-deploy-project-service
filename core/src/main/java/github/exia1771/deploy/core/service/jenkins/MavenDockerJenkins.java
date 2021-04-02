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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MavenDockerJenkins extends AbstractJenkinsService {

	public MavenDockerJenkins(JenkinsServer server, ProjectService projectService, ProjectConfigService projectConfigService, ImageTemplateService imageTemplateService, ContainerService containerService, RedisTemplate<String, Object> redisTemplate) {
		super(server, projectService, projectConfigService, imageTemplateService, containerService, redisTemplate);
	}

	@Override
	protected Resource getTemplateFile() {
		return new ClassPathResource("template/git-maven-docker.xml");
	}

	@Override
	public String getBuildType() {
		return "maven";
	}

	private static final String TAG_SUFFIX = "Jenkins";

	@Override
	protected Map<String, Object> getVariables(ProjectContainer projectContainer) {
		Map<String, Object> map = new HashMap<>();
		Project project = projectService.findById(projectContainer.getProjectId());
		map.put("gitUrl", project.getGitUrl());
		map.put("uuid", UUID.randomUUID().toString());
		map.put("tag", "${Tag}");
		String buildCommand = projectContainer.getBuildCommand();
		if (StringUtil.isNotBlank(buildCommand) && buildCommand.startsWith("mvn")) {
			buildCommand = buildCommand.replaceAll("mvn", "");
		}
		map.put("targets", buildCommand);
		String dockerShellCommand = getDockerShellCommand(projectContainer, project);
		map.put("shell", dockerShellCommand);
		return map;
	}

	private String buildImage(ProjectContainer projectContainer, ImageTemplate imageTemplate) {
		StringBuilder builder = new StringBuilder();

		String[] split = projectContainer.getCpToContainerPath().split("\\s+");
		String hostFile = split[0];
		String containerFile = split[1];

		builder.append("docker build --build-arg image=")
				.append(imageTemplate.getDockerImageId())
				.append(" --build-arg from=")
				.append(hostFile)
				.append(" --build-arg to=")
				.append(containerFile)
				.append(" --build-arg command='")
				.append(projectContainer.getRunCommand())
				.append("' -f ../DockerFiles/maven/DockerFile -t ")
				.append(projectContainer.getDockerContainerId())
				.append(":")
				.append(TAG_SUFFIX)
				.append(" . ");

		return builder.append(System.lineSeparator()).toString();
	}

	private String getDockerShellCommand(ProjectContainer projectContainer, Project project) {
		ProjectConfigDTO config = projectConfigService.findByProjectIdAndNamespaceId(projectContainer.getProjectId(), projectContainer.getNamespaceId());
		ImageTemplate imageTemplate = imageTemplateService.findById(project.getTemplateId());

		StringBuilder builder = new StringBuilder();

		builder.append(buildImage(projectContainer, imageTemplate));

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
				.append(projectContainer.getDockerContainerId())
				.append(":")
				.append(TAG_SUFFIX);
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
						// 停止容器
						builder.append(" docker stop ")
								.append(projectContainer.getDockerContainerId())
								.append(System.lineSeparator())
								// 删除容器
								.append(" docker rm ")
								.append(projectContainer.getDockerContainerId())
								.append(System.lineSeparator())
								// 删除镜像
								.append(" docker rmi ")
								.append(projectContainer.getDockerContainerId())
								.append(":")
								.append(TAG_SUFFIX)
								.append(System.lineSeparator());
						break;
					}
				}
			}
		}
	}

}
