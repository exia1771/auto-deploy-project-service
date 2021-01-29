package github.exia1771.deploy.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class Tokens {

    public static final String TOKEN_ILLEGAL = "授权已过期，需要重新登录!";

    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String SUBJECT = "EXIA1771";

    public static String create(Map<String, Object> map) {
        Date now = Dates.now();
        Date expiration = Dates.expire(now, Duration.ofMinutes(30));
        return Jwts.builder()
                .setId(Commons.getId().toString())
                .setSubject(SUBJECT)
                .setIssuedAt(now)
                .signWith(KEY)
                .setExpiration(expiration)
                .addClaims(map)
                .compact();
    }

    public static Map<String, Object> parse(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .requireSubject(SUBJECT)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (JwtException exception){
            return new HashMap<>();
        }
    }

}
