package github.exia1771.deploy.common.util;

import cn.hutool.core.util.IdUtil;

public class Commons {

	private static final Long WORK_ID = 1L;
	private static final Long DATA_ID = 1L;

	public static String getId() {
		long id = IdUtil.getSnowflake(WORK_ID, DATA_ID).nextId();
		return String.valueOf(id);
	}
}
