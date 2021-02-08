package github.exia1771.deploy.core.service;

import com.alibaba.fastjson.JSONObject;
import github.exia1771.deploy.common.util.Beans;
import github.exia1771.deploy.common.util.Requests;
import github.exia1771.deploy.core.abs.AbstractEntity;
import github.exia1771.deploy.core.abs.AbstractRemoteApiParam;

import java.util.List;

public interface RemoteApi<T extends AbstractEntity, A extends AbstractRemoteApiParam> {

    String getURL();

    default String getRequestURL(A param) {
        String concat = Requests.concat(getURL(), Beans.toMap(param), A.EXCLUDED_FIELDS);
        return Requests.concat(concat, param.getFilter());
    }

    List<T> findByParam(A param);

    JSONObject inspect(String id);
}
