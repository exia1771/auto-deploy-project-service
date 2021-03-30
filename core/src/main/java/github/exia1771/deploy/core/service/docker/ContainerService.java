package github.exia1771.deploy.core.service.docker;


import github.exia1771.deploy.core.entity.Container;
import github.exia1771.deploy.core.entity.DockerRemoteApiParam;
import lombok.Getter;

import java.io.IOException;

public interface ContainerService extends RemoteApi<Container, DockerRemoteApiParam> {

	@Getter
	enum Status {
		NOT_FOUNT("不存在的容器"),
		SERVER_ERROR("Docker服务错误"),
		SUCCESS("成功"),
		OTHER("其他情况失败");

		private final String describe;

		Status(String describe) {
			this.describe = describe;
		}
	}

	default String getContainerLogs(String containerNameOrId) throws IOException {
		return getContainerLogs(containerNameOrId, 0);
	}

	String getContainerLogs(String containerNameOrId, long since) throws IOException;

	Status restart(String containerNameOrId);
}
