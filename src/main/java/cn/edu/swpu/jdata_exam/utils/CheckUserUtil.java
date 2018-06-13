package cn.edu.swpu.jdata_exam.utils;

import cn.edu.swpu.jdata_exam.entity.dto.UserLogin;
import cn.edu.swpu.jdata_exam.enums.CodeEnum;
import cn.edu.swpu.jdata_exam.enums.ExceptionEnum;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class CheckUserUtil {

    //检验用户传入的登录信息是否符合要求
    public static ResultVo checkLogin(UserLogin userLogin){
        log.info("进入CheckUserUtil的checkLogin");

        String regx = "[0-9]{12}";

        ResultVo resultVo = ResultVoUtil.success();

        log.info(userLogin.getUserId());
        log.info(userLogin.getUserPassword());
        log.info(userLogin.getVerifyCode());

        //检测用户id是否为空
        if (StringUtils.isEmpty(userLogin.getUserId())){
            log.info(userLogin.getUserId());
            resultVo = ResultVoUtil.error(CodeEnum.ERROR.getCode(),ExceptionEnum.USER_NOT_EXIT.getMessage());
            return resultVo;
        }
        //检测用户id是否符合格式
        if (!userLogin.getUserId().matches(regx)){
            resultVo = ResultVoUtil.error(CodeEnum.ERROR.getCode(),ExceptionEnum.USER_NOT_EXIT.getMessage());
            return resultVo;
        }
        //检测用户密码是否为空
        if (StringUtils.isEmpty(userLogin.getUserPassword())){
            resultVo = ResultVoUtil.error(CodeEnum.ERROR.getCode(),ExceptionEnum.PASSWORD_ERROR.getMessage());
            return resultVo;
        }
        //检测验证码是否为空
        if (StringUtils.isEmpty(userLogin.getVerifyCode())){
            resultVo = ResultVoUtil.error(CodeEnum.ERROR.getCode(),ExceptionEnum.VERIFY_CODE_ERROR.getMessage());
            return resultVo;
        }
        if (userLogin.getVerifyCode().length()!=4){
            resultVo = ResultVoUtil.error(CodeEnum.ERROR.getCode(),ExceptionEnum.VERIFY_CODE_ERROR.getMessage());
            return resultVo;
        }

        return resultVo;
    }


}
