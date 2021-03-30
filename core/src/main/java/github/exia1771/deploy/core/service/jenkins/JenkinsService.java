package github.exia1771.deploy.core.service.jenkins;

import github.exia1771.deploy.core.entity.ProjectContainer;

import java.io.IOException;

public interface JenkinsService {

	String getBuildType();

	void createNewJob(String jobName, ProjectContainer projectContainer) throws IOException;

	void build(ProjectContainer build) throws IOException;

	String getBuildLog(String jobName, int buildNumber) throws IOException;

	String stop(String jobName, int buildNumber) throws IOException;
}
