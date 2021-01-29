package github.exia1771.deploy.common.filter;

import com.alibaba.fastjson.JSON;
import github.exia1771.deploy.common.config.WebConfig;
import github.exia1771.deploy.common.entity.User;
import github.exia1771.deploy.common.util.ResponseBody;
import github.exia1771.deploy.common.util.Tokens;
import github.exia1771.deploy.common.util.Users;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Map;


public class TokenInterceptor implements HandlerInterceptor {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_TYPE = "application/json";
    private static final String ALLOW_ORIGINAL = "Access-Control-Allow-Origin";
    private static final String OPTIONS_METHOD = "OPTIONS";
    private static final String ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String EXPOSE_HEADERS = "Access-Control-Expose-Headers";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader(TOKEN_HEADER);

        response.setHeader(CONTENT_TYPE_HEADER, JSON_TYPE);
        response.setHeader(ALLOW_ORIGINAL, WebConfig.ALL_ORIGINAL);
        response.setHeader(ALLOW_HEADERS, TOKEN_HEADER);
        response.setHeader(EXPOSE_HEADERS, TOKEN_HEADER);
        response.setCharacterEncoding(Charset.defaultCharset().displayName());
        response.setStatus(HttpStatus.OK.value());

        if (OPTIONS_METHOD.equals(request.getMethod())) {
            return true;
        }


        if (header == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ResponseBody responseBody = new ResponseBody(null, Tokens.TOKEN_NOT_FOUND);
            response.getWriter().print(JSON.toJSONString(responseBody));
            return false;
        }

        if (!header.startsWith(TOKEN_PREFIX)) {
            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            ResponseBody responseBody = new ResponseBody(null, Tokens.TOKEN_ILLEGAL);
            response.getWriter().print(JSON.toJSONString(responseBody));
            return false;

        } else {

            try {
                String token = header.substring(TOKEN_PREFIX.length());
                Map<String, Object> parse = Tokens.parse(token);
                String userId = parse.get(Users.Params.USER_ID.getValue()).toString();
                String roleId = parse.get(Users.Params.ROLE_ID.getValue()).toString();

                if (!Users.compareToken(token, userId)) {
                    response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                    ResponseBody responseBody = new ResponseBody(null, Tokens.TOKEN_ILLEGAL);
                    response.getWriter().print(JSON.toJSONString(responseBody));
                    return false;
                }

                if (Tokens.isWillExpire(token)) {
                    User user = new User();
                    user.setId(Long.valueOf(userId));
                    user.setRoleId(Long.valueOf(roleId));
                    String newToken = Users.getUserToken(user);
                    response.setHeader(TOKEN_HEADER, newToken);
                }

                Users.setUser(new Users.SimpleUser(Long.valueOf(userId), Long.valueOf(roleId)));

                return true;

            } catch (JwtException e) {
                response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                ResponseBody responseBody = new ResponseBody(null, Tokens.TOKEN_ILLEGAL_OR_EXPIRATION);
                response.getWriter().print(JSON.toJSONString(responseBody));
                return false;
            }

        }


    }

}
