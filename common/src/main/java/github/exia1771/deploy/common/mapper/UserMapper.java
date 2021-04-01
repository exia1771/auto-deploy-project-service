package github.exia1771.deploy.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import github.exia1771.deploy.common.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

	void batchUpdateDeptId(@Param("list") List<User> users);

}
