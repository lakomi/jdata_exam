package cn.edu.swpu.jdata_exam.filter;

import cn.edu.swpu.jdata_exam.entity.dto.TokenDTO;
import cn.edu.swpu.jdata_exam.entity.dto.UserLogin;
import cn.edu.swpu.jdata_exam.enums.BackMessageEnum;
import cn.edu.swpu.jdata_exam.utils.ResultVoUtil;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法
 * attemptAuthentication ：接收并解析用户凭证。
 * successfulAuthentication ：用户成功登录后，这个方法会被调用，并生成token。
 */
@Slf4j
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    //私钥
    private String KEY = "spring-security-@Jwt!&Secret^#";

    //token的开头
    private String tokenHead = "Bearer ";

    //token头部
    private String tokenHeader = "Authorization";

    //token的有效期  30分钟   毫秒单位
    private long expiration = 30*60*1000;


    //接收并解析用户凭证,从request中取出authentication
    public Authentication attemptAuthentication(UserLogin userLogin) throws AuthenticationException {

        log.info("进入JWTLoginFilter的attemptAuthentication");

        Authentication authenticationToken = null;

        log.info("userId={},userPassword={}",userLogin.getUserId(),userLogin.getUserPassword());

        if(StringUtils.isEmpty(userLogin.getUserId())&&StringUtils.isEmpty(userLogin.getUserPassword())) {
            authenticationToken = new UsernamePasswordAuthenticationToken("", "");
        }else{
            authenticationToken = new UsernamePasswordAuthenticationToken(userLogin.getUserId(),userLogin.getUserPassword());
        }

//        } catch (IOException e) {
//            e.printStackTrace();
//            authenticationToken = new UsernamePasswordAuthenticationToken("", "");
//        }


//        log.info("userId={},userPassword={}",userId,password);
//
//        if(StringUtils.isEmpty(userId)&&StringUtils.isEmpty(password)) {
//            authenticationToken = new UsernamePasswordAuthenticationToken("", "");
//        }else{
//            authenticationToken = new UsernamePasswordAuthenticationToken(userId,password);
//        }

        return authenticationToken;
    }

    /**
     * 生成token
     * token：
     * header(头)：alg(指明算法) typ（token类型 JWT  此处不必写）
     * payload(负载)：
     * 系统保留的声明（建议使用）：iss（签发者） exp（过期时间）  sub（主题）
     * 公共声明：
     * 私有声明：根据业务需求定义
     * signature(签名)：base64编码的header、base64编码的payload和秘钥（secret）进行运算
     * @param response
     * @param authResult
     */
    //用户成功登录后，该方法被调用
    public ResultVo successfulAuthentication(HttpServletResponse response,
                                             Authentication authResult) {

        Map<String,Object> claims = new HashMap<>();
        TokenDTO tokenDTO = new TokenDTO();
        Date expireTime = new Date(System.currentTimeMillis()+expiration);


        claims.put("name",authResult.getPrincipal());
        claims.put("time",System.currentTimeMillis());



        log.info("authResult.getCredentials()={}",authResult.getCredentials());


        log.info("放入claims中的name={}",authResult.getPrincipal());

        //生成token
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireTime)                   //设置过期时间
                .signWith(SignatureAlgorithm.HS256,KEY)       //签名
                .compact();


        String responseToken = tokenHead + token;
        tokenDTO.setAuthorization(responseToken);

        log.info("responseToken={}",responseToken);

        log.info("过期时间是={}",expireTime);

        //返回token
        response.addHeader(tokenHeader,responseToken);


        return ResultVoUtil.success(BackMessageEnum.LOGIN_SUCCESS.getMessage(),tokenDTO);
    }

}
