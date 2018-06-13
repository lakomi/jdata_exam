package cn.edu.swpu.jdata_exam.service.impl;

import cn.edu.swpu.jdata_exam.dao.UserBasicInfoDAO;
import cn.edu.swpu.jdata_exam.entity.UserBasicInfo;
import cn.edu.swpu.jdata_exam.entity.dto.UserLogin;
import cn.edu.swpu.jdata_exam.entity.dto.UserUpdatePw;
import cn.edu.swpu.jdata_exam.enums.BackMessageEnum;
import cn.edu.swpu.jdata_exam.enums.CodeEnum;
import cn.edu.swpu.jdata_exam.enums.ExceptionEnum;
import cn.edu.swpu.jdata_exam.exception.JdataExamException;
import cn.edu.swpu.jdata_exam.filter.JWTLoginFilter;
import cn.edu.swpu.jdata_exam.service.UserBasicInfoService;
import cn.edu.swpu.jdata_exam.service.VerifyCodeService;
import cn.edu.swpu.jdata_exam.support.UserAuthenticationProvider;
import cn.edu.swpu.jdata_exam.utils.AuthenticationUtil;
import cn.edu.swpu.jdata_exam.utils.CheckUserUtil;
import cn.edu.swpu.jdata_exam.utils.ResultVoUtil;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Service
public class UserBasicInfoServiceImpl implements UserBasicInfoService {
    @Autowired
    private UserBasicInfoDAO userBasicInfoDAO;

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    private VerifyCodeService verifyCodeService;


    private JWTLoginFilter jwtLoginFilter = new JWTLoginFilter();

    @Override
    public ResultVo userLogin(UserLogin userLogin, HttpServletRequest request, HttpServletResponse response) {

        log.info("进入UserBasicInfoServiceImpl的userLogin");
        //检验输入的登录信息是否符合格式
        ResultVo resultVo = CheckUserUtil.checkLogin(userLogin);

        if (resultVo.getCode().equals(CodeEnum.SUCCESS.getCode())) {
            //检查验证码
            ResultVo codeResult = verifyCodeService.checkVerifyCode(request,userLogin.getVerifyCode());

            if (codeResult.getCode().equals(CodeEnum.SUCCESS.getCode())) {

                //解析请求,从request中取出authentication
                Authentication authentication = jwtLoginFilter.attemptAuthentication(userLogin);

                //验证用户名和密码
                Authentication resultAuthentication = userAuthenticationProvider.authenticate(authentication);

                //验证通过，生成token，放入response
                ResultVo resultVo1 = jwtLoginFilter.successfulAuthentication(response, resultAuthentication);

                return resultVo1;

            }else {
                //验证码出错返回
                return codeResult;
            }

        }else{
            //登录信息出错返回
            return resultVo;
        }

    }

    @Override
    public ResultVo updateUserPw(UserUpdatePw userUpdatePw,HttpServletRequest request) {

        log.info("进入UserBasicInfoServiceImpl的updateUserPw");

        //检查验证码
        ResultVo codeResult = verifyCodeService.checkVerifyCode(request,userUpdatePw.getVerifyCode());
        if (codeResult.getCode().equals(CodeEnum.SUCCESS.getCode())) {

            //从Authentication中获得userId
            String authUserId = AuthenticationUtil.getAuthUserId();

            log.info("AuthUserId={}", authUserId);
            if (!userUpdatePw.getUserId().equals(authUserId)) {
                throw new JdataExamException(ExceptionEnum.DONOT_MODIFY_OTHER);
            }

            //数据库中查找用户
            UserBasicInfo userBasicInfo1 = userBasicInfoDAO.getUserById(userUpdatePw.getUserId());

            if (!userUpdatePw.getOldPassword().equals(userBasicInfo1.getUserPassword())) {
                throw new JdataExamException(ExceptionEnum.OLD_PASSWORD_ERROR);
            }



            log.info("新密码："+userUpdatePw.getNewPassword());

            //修改密码
            userBasicInfo1.setUserPassword(userUpdatePw.getNewPassword());

            int result = userBasicInfoDAO.updatePw(userBasicInfo1);

            ResultVo resultVo = null;

            if (result == 1) {
                resultVo = ResultVoUtil.success(BackMessageEnum.PASSWORD_MODIFY_SUCCESS.getMessage());
            } else {
                resultVo = ResultVoUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }

            return resultVo;

        }else {

            return codeResult;

        }
    }

}
