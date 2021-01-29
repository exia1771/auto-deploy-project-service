package github.exia1771.deploy.common.util;

import github.exia1771.deploy.common.exception.ServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.Map;

public abstract class Tokens {

    public static final String TOKEN_ILLEGAL = "授权违法，需要重新登录!";
    public static final String TOKEN_EXPIRATION = "授权过期，需要重新登录!";
    public static final String TOKEN_NOT_FOUND = "请重新登录!";
    public static final String TOKEN_ILLEGAL_OR_EXPIRATION = "授权违法或过期，需要重新登录!";

    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String SUBJECT = "EXIA1771";
    private static final TemporalAmount DEATH_MINUTES = Duration.ofMinutes(5);
    private static final TemporalAmount EXPIRATION_MINUTES = Duration.ofMinutes(30);
    private static final TemporalAmount PROLONG_MINUTES = Duration.ofMinutes(15);

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
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .requireSubject(SUBJECT)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new ServiceException(Tokens.TOKEN_ILLEGAL_OR_EXPIRATION);
        }
    }

    public static Boolean isWillExpire(String token) {
        Claims parse = parse(token);
        Date expiration = parse.getExpiration();
        Duration minus = Dates.minus(Dates.now(), expiration);
        return Dates.minus(minus, DEATH_MINUTES).isNegative();
    }

}
