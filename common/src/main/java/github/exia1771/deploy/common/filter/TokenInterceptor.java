package github.exia1771.deploy.common.filter;

import com.alibaba.fastjson.JSON;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.common.util.Tokens;
import github.exia1771.deploy.common.util.Users;
import io.jsonwebtoken.JwtException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Data
public class TokenInterceptor implements HandlerInterceptor {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ALLOW_ORIGINAL = "Access-Control-Allow-Origin";
    private static final String ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private static final String ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String METHODS = "GET, HEAD, POST, PUT, DELETE, OPTIONS, PATCH";
    private static final String OPTIONS_METHOD = "OPTIONS";
    private String DOMAIN;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());

        if (OPTIONS_METHOD.equals(request.getMethod())) {
            response.setHeader(CONTENT_TYPE, APPLICATION_JSON);
            response.setHeader(ALLOW_ORIGINAL, DOMAIN);
            response.setHeader(ALLOW_CREDENTIALS, Boolean.toString(true));
            response.setHeader(ALLOW_METHODS, METHODS);
            response.setHeader(ALLOW_HEADERS, CONTENT_TYPE);
            response.setStatus(HttpStatus.OK.value());
            return true;
        }


        String cookieToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ResponseBody responseBody = new ResponseBody(null, Tokens.TOKEN_NOT_FOUND);
            response.getWriter().print(JSON.toJSONString(responseBody));
            return false;
        }

        for (Cookie cookie : cookies) {
            if (Tokens.TOKEN_KEY.equals(cookie.getName())) {
                cookieToken = cookie.getValue();
            }
        }

        if (cookieToken == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ResponseBody responseBody = new ResponseBody(null, Tokens.TOKEN_NOT_FOUND);
            response.getWriter().print(JSON.toJSONString(responseBody));
            return false;
        }

        cookieToken = URLDecoder.decode(cookieToken, StandardCharsets.UTF_8.displayName());

        if (!cookieToken.startsWith(Tokens.TOKEN_PREFIX)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ResponseBody responseBody = new ResponseBody(null, Tokens.TOKEN_ILLEGAL);
            response.getWriter().print(JSON.toJSONString(responseBody));
            return false;

        } else {

            try {
                String token = cookieToken.substring(Tokens.TOKEN_PREFIX.length());
                Map<String, Object> parse = Tokens.parse(token);
                String userId = parse.get(Users.Params.USER_ID.getValue()).toString();
                String roleId = parse.get(Users.Params.ROLE_ID.getValue()).toString();

                if (!Users.compareToken(token, userId)) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    ResponseBody responseBody = new ResponseBody(null, Tokens.TOKEN_ILLEGAL);
                    response.getWriter().print(JSON.toJSONString(responseBody));
                    return false;
                }

                if (Tokens.isWillExpire(token)) {
                    User user = new User();
                    user.setId(userId);
                    user.setRoleId(roleId);
                    String refreshToken = Users.getUserToken(user);
                    Tokens.setCookie(response, refreshToken);
                }
                Users.setUser(new Users.SimpleUser(Long.valueOf(userId), Long.valueOf(roleId)));

                return true;

            } catch (JwtException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                ResponseBody responseBody = new ResponseBody(null, Tokens.TOKEN_ILLEGAL_OR_EXPIRATION);
                response.getWriter().print(JSON.toJSONString(responseBody));
                return false;
            }

        }


    }

}
