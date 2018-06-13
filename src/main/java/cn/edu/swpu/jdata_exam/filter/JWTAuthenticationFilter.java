package cn.edu.swpu.jdata_exam.filter;

import cn.edu.swpu.jdata_exam.enums.ExceptionEnum;
import cn.edu.swpu.jdata_exam.exception.JdataExamException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * token的校验
 * 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    //私钥
    private String key = "spring-security-@Jwt!&Secret^#";

    //token的开头
    private String tokenHead = "Bearer ";

    //token头部
    private String tokenHeader = "Authorization";

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        log.info("进入JWTAuthenticationFilter的doFilterInternal方法。校验token");

        String token = request.getHeader(tokenHeader);   //取得token内容

        log.info("从头部获取的token={}",token);

        if(StringUtils.isEmpty(token)||!token.startsWith(tokenHead)){
            chain.doFilter(request,response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request,response);


    }

    public UsernamePasswordAuthenticationToken getAuthentication(String responseToken){

        log.info("进入JWTAuthenticationFilter的getAuthentication校验token");

        Claims claims;
        String userId = null;

        String token = responseToken.split("\\s+")[1];

        log.info("检验中分离出来的token={}",token);

        if (!StringUtils.isEmpty(token)){

            try {
                //解析token
                claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token)
                        .getBody();

                userId =  (String )claims.get("name");
                log.info("解析的claims内容{}",userId);


            }catch (Exception e){
                userId = ExceptionEnum.REPEAT_LOGIN.getMessage();
            }

        }else{
            userId = ExceptionEnum.PLEASE_LOGIN_FIRST.getMessage();
        }

        return new UsernamePasswordAuthenticationToken(userId,null,new ArrayList<>());


    }




}
