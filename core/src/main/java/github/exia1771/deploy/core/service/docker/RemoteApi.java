package github.exia1771.deploy.core.service.docker;

import com.alibaba.fastjson.JSONObject;
import github.exia1771.deploy.common.util.Beans;
import github.exia1771.deploy.common.util.Requests;

import java.util.List;

public interface RemoteApi<T extends AbstractWebEntity, A extends AbstractRemoteApiParam> {

	String getURL();

	default String getRequestURL(A param) {
		String concat = Requests.concat(getURL(), Beans.toMap(param), A.EXCLUDED_FIELDS);
		return Requests.concat(concat, param.getFilters());
	}

	List<T> findByParam(A param);

	List<T> findByParam(Object param, String url);

	JSONObject inspect(String id);
}
