package github.exia1771.deploy.common.util;

import github.exia1771.deploy.common.exception.ServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

public abstract class Tokens {

    public static final String TOKEN_KEY = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_COOKIE_DOMAIN = "localhost";
    public static final String TOKEN_COOKIE_PATH = "/";
    public static final String TOKEN_ILLEGAL = "授权违法，需要重新登录!";
    public static final String TOKEN_NOT_FOUND = "请重新登录!";
    public static final String TOKEN_ILLEGAL_OR_EXPIRATION = "授权违法或过期，需要重新登录!";
    public static final String COOKIE_NOT_FOUND = "Needed Cookie Not Found!";

    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String SUBJECT = "EXIA1771";
    private static final Duration DEATH_MINUTES = Duration.ofMinutes(5);
    public static final Duration EXPIRATION_MINUTES = Duration.ofMinutes(30);

    public static String create(Map<String, Object> map) {
        Date now = Dates.now();
        Date expiration = Dates.plus(now, EXPIRATION_MINUTES);
        return Jwts.builder()
                .setId(Commons.getId().toString())
                .setSubject(SUBJECT)
                .setIssuedAt(now)
                .signWith(KEY)
                .setExpiration(expiration)
                .addClaims(map)
                .compact();
    }

    public static Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .requireSubject(SUBJECT)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static void setCookie(HttpServletResponse response, String token) throws UnsupportedEncodingException {
        Cookie cookie = new Cookie(Tokens.TOKEN_KEY, URLEncoder.encode(Tokens.TOKEN_PREFIX, StandardCharsets.UTF_8.displayName()) + token);
        cookie.setDomain(Tokens.TOKEN_COOKIE_DOMAIN);
        cookie.setPath(Tokens.TOKEN_COOKIE_PATH);
        response.addCookie(cookie);
    }

    public static Boolean isWillExpire(String token) {
        Claims parse = parse(token);
        Date expiration = parse.getExpiration();
        Duration minus = Dates.minus(Dates.now(), expiration);
        return Dates.minus(DEATH_MINUTES, minus).isNegative();
    }

}
