package github.exia1771.deploy.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import github.exia1771.deploy.core.entity.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectMapper extends BaseMapper<Project> {

    List<Project> findListById(@Param("list") List<String> projectIdList);

}
