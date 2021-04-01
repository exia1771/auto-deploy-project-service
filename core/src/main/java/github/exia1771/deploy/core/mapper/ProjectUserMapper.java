package github.exia1771.deploy.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import github.exia1771.deploy.core.entity.ProjectUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectUserMapper extends BaseMapper<ProjectUser> {

	int batchAddProjectMember(@Param("list") List<ProjectUser> projectUserList);

	int batchRemoveProjectMember(@Param("list") List<ProjectUser> projectUsers);
}
