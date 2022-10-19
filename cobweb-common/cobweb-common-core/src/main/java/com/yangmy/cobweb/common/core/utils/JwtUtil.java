package com.yangmy.cobweb.common.core.utils;

import io.jsonwebtoken.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {

    /**
     * 生成jwt
     * 使用Hs256算法, 对称加密密钥使用固定JWT_SEC秘钥
     * @param jwtSec    jwt密钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param ttlMillis jwt过期时间(毫秒)
     * @param subjectId  用户名 可根据需要传递的信息添加更多, 因为浏览器get传参url限制，不建议放置过多的参数
     * @return
     */
    public static String createJWT(String jwtSec, long ttlMillis, String subjectId) {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 添加payload声明
        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                // iat: jwt的签发时间
                .setIssuedAt(now)
                // 代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串
                .setSubject(subjectId)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, jwtSec.getBytes(StandardCharsets.UTF_8));
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            // 设置过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 生成jwt
     * 使用Hs256算法, 对称加密密钥使用固定JWT_SEC秘钥
     * @param jwtSec    jwt密钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param ttlMillis jwt过期时间(毫秒)
     * @param userId  用户名 可根据需要传递的信息添加更多, 因为浏览器get传参url限制，不建议放置过多的参数
     * @param claims 创建payload的私有声明（根据特定的业务需要添加）
     * @return
     */
    public static String createJWT(String jwtSec, long ttlMillis, String userId,Claims claims) {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 添加payload声明
        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                // iat: jwt的签发时间
                .setIssuedAt(now)
                // 代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串
                .setSubject(userId)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, jwtSec.getBytes(StandardCharsets.UTF_8));
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            // 设置过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();
    }


    /**
     * jwt的解密
     * @param jwtSec jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token  加密后的token
     * @return
     */
    public static Claims parseJWT(String jwtSec, String token)
            throws ExpiredJwtException, UnsupportedJwtException,
            MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(jwtSec.getBytes(StandardCharsets.UTF_8))
                // 设置需要解析的jwt
                .parseClaimsJws(token).getBody();
    }


    public static void main(String[] args) {
//        // 生成jwt  // 10秒过期
//        String jwt = JwtUtil.createJWT("1234567", 10000, "admin");
//        // 生成token
//        System.out.println(jwt);
        // 解析jwt
        Claims claims = parseJWT("9?,#ksbfsb%ksndons-.?", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyYzk5OWY2Ny0wYmU0LTRhZDktYWM2OC1iODFjNjljMzlkN2IiLCJpYXQiOjE2NjM2NjY0MjMsInN1YiI6Ijk5OSIsImV4cCI6MTY2Mzc1MjgyM30.VJwDCG-YMaJt-RRwFfTY7j88tTYRF58cTmdfHuHELRw");
        // 获取用户名信息
        Object username = claims.getId();
        // 解析token
        System.out.println("用户名:"+username);
    }

}
