package github.exia1771.deploy.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.exia1771.deploy.common.entity.Dept;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.entity.dto.UserDTO;
import github.exia1771.deploy.common.exception.ServiceException;
import github.exia1771.deploy.common.mapper.DeptMapper;
import github.exia1771.deploy.common.service.DeptService;
import github.exia1771.deploy.common.service.UserService;
import github.exia1771.deploy.common.util.StringUtil;
import github.exia1771.deploy.common.util.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeptServiceImpl extends BaseServiceImpl<String, Dept> implements DeptService {

	private final DeptMapper mapper;

	@Autowired
	private UserService userService;

	private static final String NAME_COLUMN = "name";

	public DeptServiceImpl(DeptMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}


	@Override
	protected void beforeSave(Dept dept) {
		Validators.requireLength("部门名称", dept.getName(), 1, 255, true);
		QueryWrapper<Dept> wrapper = new QueryWrapper<>();
		wrapper.eq(NAME_COLUMN, dept.getName());
		if (isExisted(wrapper)) {
			throw new ServiceException("已经存在的部门名称");
		}
	}

	@Override
	protected void beforeDelete(String id) {
		User user = userService.findById(getCurrentUser().getUserId());
		if (StringUtil.isNotBlank(user.getDeptId()) && user.getDeptId().equals(id)) {
			throw new ServiceException("无法删除自己所在部门");
		}

		List<UserDTO> userDTOList = userService.findByDeptId(id);
		List<User> userList = new ArrayList<>();
		for (UserDTO userDTO : userDTOList) {
			User u = new User();
			u.setId(userDTO.getId());
			u.setDeptId(null);
			userList.add(u);
		}

		userService.batchUpdateDeptId(userList);
	}

}
