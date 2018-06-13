package cn.edu.swpu.jdata_exam.utils;

import cn.edu.swpu.jdata_exam.enums.ExceptionEnum;
import cn.edu.swpu.jdata_exam.exception.JdataExamException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtil {

    public static String getAuthUserId(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String authUserId = (String )authentication.getPrincipal();

        //token失效
        if (authUserId.equals(ExceptionEnum.REPEAT_LOGIN.getMessage())){
            throw new JdataExamException(ExceptionEnum.REPEAT_LOGIN);
        }
        //token为null(在JWT中已拦截。若token为null，则直接返回403)
        if (authUserId.equals(ExceptionEnum.PLEASE_LOGIN_FIRST.getMessage())){
            throw new JdataExamException(ExceptionEnum.PLEASE_LOGIN_FIRST);
        }

        return authUserId;
    }

}
