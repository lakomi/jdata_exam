package cn.edu.swpu.jdata_exam.config;



import cn.edu.swpu.jdata_exam.filter.JWTAuthenticationFilter;
import cn.edu.swpu.jdata_exam.filter.JWTLoginFilter;
import cn.edu.swpu.jdata_exam.support.MyUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * SpringSecurity的配置
 * 配置security的功能
 * 通过SpringSecurity的配置，将JWTLoginFilter，JWTAuthenticationFilter组合在一起
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserInfoService myUserInfoService;

    /**
     * 处理用户密码加密解密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        log.info("进入SecurityConfig的passwordEncoder");
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置所有请求的访问控制
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        log.info("进入SecurityConfig的configure(HttpSecurity http)");
        http.csrf().disable()           //关闭csrf
                .authorizeRequests()      //授权配置
                .antMatchers("/user/login","/user/getCode").permitAll()
                .antMatchers(HttpMethod.OPTIONS,"/user/updatePw","/user/select/score","/file/upload","/file/submit","/user/login").permitAll()
                .antMatchers("/user/select/score").hasRole("ADMIN")
                .anyRequest()          //任何请求
                .authenticated()      //身份认证
                .and()
//                .cors().and()     //跨域问题解决   响应到corsConfigurationSource
                .addFilter(new JWTLoginFilter())
                .addFilter(new JWTAuthenticationFilter(authenticationManager()));
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        //允许的请求域名
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        //允许的请求方式
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST"));


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",corsConfiguration);

        return source;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {


        log.info("进入SecurityConfig的configure(AuthenticationManagerBuilder auth)");

        auth.userDetailsService(myUserInfoService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);

        web.ignoring().antMatchers("/home/ubuntu/qiu/front");
    }








}
