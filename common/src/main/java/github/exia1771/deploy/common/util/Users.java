package github.exia1771.deploy.common.util;


import github.exia1771.deploy.common.entity.User;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class Users {

    private static RedisTemplate<String, Object> template;
    private static final String TOKEN = "token";
    private static final ThreadLocal<SimpleUser> LOCAL_USER = new ThreadLocal<>();


    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        template = redisTemplate;
    }

    public static void setUser(SimpleUser user) {
        if (LOCAL_USER.get() != null) {
            LOCAL_USER.remove();
        }
        LOCAL_USER.set(user);
    }

    public static SimpleUser getUser() {
        SimpleUser simpleUser = LOCAL_USER.get();
        Objects.requireNonNull(simpleUser);
        return simpleUser;
    }

    public static String getUserToken(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put(Users.Params.USER_ID.getValue(), user.getId());
        map.put(Users.Params.ROLE_ID.getValue(), user.getRoleId());

        String token = Tokens.create(map);
        template.boundHashOps(user.getId().toString()).put(TOKEN, token);
        return token;
    }

    public static void discardUserToken(String id) {
        template.boundHashOps(id).delete(TOKEN);
    }

    public static Boolean compareToken(String token, String userId) {
        Object o = template.boundHashOps(userId).get(TOKEN);
        return o != null && o.toString().equals(token);
    }

    @Data
    public final static class SimpleUser {
        private Long userId;
        private Long roleId;

        public SimpleUser(Long userId, Long roleId) {
            this.userId = userId;
            this.roleId = roleId;
        }

    }


    @Getter
    public enum Params {
        USER_ID("userId"),
        ROLE_ID("roleId");

        private final String value;

        Params(String value) {
            this.value = value;
        }
    }

}
