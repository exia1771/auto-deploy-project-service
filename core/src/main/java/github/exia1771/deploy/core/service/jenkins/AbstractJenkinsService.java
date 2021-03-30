package github.exia1771.deploy.core.service.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.JobWithDetails;
import freemarker.template.TemplateException;
import github.exia1771.deploy.common.util.XmlUtil;
import github.exia1771.deploy.core.entity.Project;
import github.exia1771.deploy.core.entity.ProjectContainer;
import github.exia1771.deploy.core.service.ImageTemplateService;
import github.exia1771.deploy.core.service.ProjectConfigService;
import github.exia1771.deploy.core.service.ProjectService;
import github.exia1771.deploy.core.service.docker.ContainerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Slf4j
public abstract class AbstractJenkinsService implements JenkinsService {

	protected final JenkinsServer server;
	protected final ProjectService projectService;
	protected final ProjectConfigService projectConfigService;
	protected final ImageTemplateService imageTemplateService;
	protected final ContainerService containerService;

	@Override
	public void createNewJob(String jobName, ProjectContainer projectContainer) throws IOException {
		server.createJob(jobName, getBuildXml(projectContainer));
	}

	@Override
	public void build(ProjectContainer build) throws IOException {
		Project project = projectService.findById(build.getProjectId());
		String jobName = project.getIdentification();
		build.setJenkinsJobName(jobName);
		build.setDockerContainerId(build.getJenkinsJobName());
		JobWithDetails job = server.getJob(jobName);
		if (job == null) {
			createNewJob(jobName, build);
		} else {
			server.updateJob(jobName, getBuildXml(build));
		}
		job = server.getJob(jobName);
		job.build();
		build.setJenkinsBuildNumber(job.getNextBuildNumber());
		BuildResult result = null;
		while (result == null) {
			try {
				TimeUnit.SECONDS.sleep(10);
				job = server.getJob(jobName);
				result = job.getBuildByNumber(build.getJenkinsBuildNumber()).details().getResult();
				log.debug("{}.{}正在构建", build.getJenkinsJobName(), build.getJenkinsBuildNumber());
			} catch (InterruptedException e) {
				log.error("睡眠失败=>", e);
			}
		}
		if (result.equals(BuildResult.FAILURE)) {
			build.setStatus(ProjectContainer.Status.FAILURE.getValue());
		} else if (result.equals(BuildResult.SUCCESS)) {
			build.setStatus(ProjectContainer.Status.SUCCESS.getValue());
		}
	}

	@Override
	public String getBuildLog(String jobName, int buildNumber) throws IOException {
		JobWithDetails job = server.getJob(jobName);
		Build build = job.getBuildByNumber(buildNumber);
		return build.details().getConsoleOutputText();
	}

	private String getBuildXml(ProjectContainer projectContainer) throws IOException {
		try {
			return XmlUtil.getResult(getTemplateFile(), getVariables(projectContainer));
		} catch (TemplateException e) {
			throw new IOException(e);
		}
	}

	protected abstract Map<String, Object> getVariables(ProjectContainer projectContainer);

	protected abstract Resource getTemplateFile();
}
