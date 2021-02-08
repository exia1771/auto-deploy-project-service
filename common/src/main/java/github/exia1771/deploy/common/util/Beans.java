package github.exia1771.deploy.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Map;

public abstract class Beans {

    public static Map<String, Object> toMap(Object bean, SerializerFeature... features) {
        String jsonString = JSONObject.toJSONString(bean, features);
        return JSONObject.parseObject(jsonString, Map.class);
    }

}
