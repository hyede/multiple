package com.yede.multiple.user.service;


import com.yede.multiple.exception.ApplicationException;
import com.yede.multiple.model.UserDetailsBean;
import com.yede.multiple.utils.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Date;


@Service
public class TokenService {

    enum ClientType {
        web,
        mobile,
        ;

        public static ClientType fromString(String str) {
            if(StringUtils.isEmpty(str)) {
                return null;
            }
            for(ClientType ct : ClientType.values()) {
                if(ct.toString().equalsIgnoreCase(str)) {
                    return ct;
                }
            }
            return null;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    public static final String TOKEN_HEADER = "CTTS-Token";
    public static final String SET_TOKEN_HEADER = "Set-CTTS-Token";
    public static final String USER_AGENT_HEADER = "User-Agent";
    public static final String MOBILE_TYPE = "mobile";

    private static final String ISSUER = "witwin";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    public static final String ORGANIZATION_CODE_KEY = "organizationCode";
    public static final String USER_ID= "userId";
    public static final String USER_NAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    private static final String CLIENT_IP_KEY = "clientIP";
    private static final String CLIENT_TYPE_KEY = "clientType";

    @Value("${authentication.secret:'SECRET'}")
    private String secretValue;
    @Value("${authentication.ttl:300}")
    private int ttlValue;
    private static String SECRET;
    private static int TTL;

    @Autowired
    private SysUserService sysUserService;

    public TokenService() {

    }

    @PostConstruct
    public void initProperties() {
        SECRET = secretValue;
        TTL = ttlValue;
    }

    public static String generateToken(String clientIP, UserDetailsBean userDetailsBean) {
        return generateToken(clientIP, userDetailsBean, false);
    }


    public static String generateToken(String clientIP, UserDetailsBean userDetailsBean, boolean isMobile) {
        if (userDetailsBean == null) {
            return null;
        }
        Date now = new Date();
        Claims claims = Jwts.claims()
                .setSubject(userDetailsBean.getUserId() + "@" + userDetailsBean.getOrganizationCode())
                .setIssuedAt(now)
                .setExpiration(isMobile ? DateUtils.plusDays(now, Integer.MAX_VALUE) : DateUtils.plusMinutes(now, TTL))
                .setIssuer(ISSUER);
        claims.put(ORGANIZATION_CODE_KEY, userDetailsBean.getOrganizationCode());
        claims.put(USER_NAME_KEY, userDetailsBean.getUserName());
        claims.put(USER_ID, userDetailsBean.getUserId());
        claims.put(PASSWORD_KEY, userDetailsBean.getPassword());
        claims.put(CLIENT_IP_KEY, clientIP);
        claims.put(CLIENT_TYPE_KEY, isMobile ? ClientType.mobile : ClientType.web);

        String token = Jwts.builder()
         .setHeaderParam("alg", SignatureAlgorithm.HS512.toString())
         .setHeaderParam("typ", TOKEN_HEADER)
         .setClaims(claims)
         .signWith(SIGNATURE_ALGORITHM, SECRET).compact();

        return token;
    }

    public static String refreshToken(String token) throws ApplicationException{
        if(token == null) {
            return null;
        }
        Claims claims = parseToken(token);
        Date now = new Date();
        claims.setIssuedAt(now);
        claims.setExpiration(DateUtils.plusMinutes(now, TTL));

        token = Jwts.builder()
                .setHeaderParam("alg", SignatureAlgorithm.HS512.toString())
                .setHeaderParam("typ", TOKEN_HEADER)
                .setClaims(claims)
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();

        return token;
    }

    public static Claims parseToken(String token)  throws ApplicationException{
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (MalformedJwtException e) {
            throw new ApplicationException("Invalid token");
        } catch (ExpiredJwtException e) {
            throw new ApplicationException("Expired token");
        } catch (Exception e) {
            throw new ApplicationException("未知的错误");
        }
        return claims;
    }

    //TODO: need to complete token validation
    /**
     * 1. get organization by code
     * 2. switch datasource
     * 3. find user by username
     * 4. validate the password
     * 5. other validations
     */
    public UserDetailsBean validateToken(String clientIP, String token) throws ApplicationException{
        Date now = new Date();
        Claims claims = parseToken(token);
        if(claims == null) {
            throw new ApplicationException("Invalid token");
        }
        if(!claims.getIssuer().equals(ISSUER)) {
            throw new ApplicationException("Invalid issuer");
        }
//        remove client ip authentication
//        if(!claims.get(CLIENT_IP_KEY).equals(clientIP)) {
//            throw new InvalidAuthenticationException("Invalid client IP");
//        }
        if(DateUtils.before(claims.getExpiration(), now)) {
            throw new ApplicationException("Expired token");
        }
        String organizationCode = (String) claims.get(ORGANIZATION_CODE_KEY);
        String userName = (String) claims.get(USER_NAME_KEY);
        String password = (String) claims.get(PASSWORD_KEY);
        UserDetailsBean userDetailsBean = sysUserService.findUserDetails(organizationCode, userName);
        if(userDetailsBean == null || !userDetailsBean.getPassword().equals(password)) {
            throw new ApplicationException("Organization or user is not found: [" +
                    userName + "@" + organizationCode + "]");
        }
        return userDetailsBean;
    }

}
