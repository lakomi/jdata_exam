package cn.edu.swpu.jdata_exam.controller;

import cn.edu.swpu.jdata_exam.config.CorsConfig;
import cn.edu.swpu.jdata_exam.entity.dto.UserLogin;
import cn.edu.swpu.jdata_exam.entity.dto.UserUpdatePw;
import cn.edu.swpu.jdata_exam.service.UserBasicInfoService;
import cn.edu.swpu.jdata_exam.service.UserScoreService;
import cn.edu.swpu.jdata_exam.service.VerifyCodeService;
import cn.edu.swpu.jdata_exam.utils.AuthenticationUtil;
import cn.edu.swpu.jdata_exam.utils.ResultVoUtil;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@RequestMapping("/user")
@RestController
@CrossOrigin(allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserBasicInfoService userBasicInfoService;

    @Autowired
    private UserScoreService userScoreService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    /**
     * 用户登录
     * @param userLogin
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResultVo login(@RequestBody UserLogin userLogin, HttpServletRequest request, HttpServletResponse response){
        log.info("进入/login服务");
        ResultVo resultVo = userBasicInfoService.userLogin(userLogin,request,response);
        return resultVo;
    }


    /**
     * 用户修改密码
     * @param userUpdatePw
     * @return
     */
    @RequestMapping("/updatePw")
    public ResultVo updatePw(@RequestBody UserUpdatePw userUpdatePw,HttpServletRequest request){

        log.info("进入/updatePw");

        ResultVo resultVo = userBasicInfoService.updateUserPw(userUpdatePw,request);

        return resultVo;



    }


    /**
     * 用户查询个人最好成绩
     * @param
     * @return
     */
    @RequestMapping("/select/score")
    public ResultVo selectScore(){
        log.info("进入/select/score服务");

        ResultVo resultVo = userScoreService.getScoreById();

        return resultVo;

    }


    /**
     * 获取验证码接口
     * @param request
     * @param response
     */
    @GetMapping("/getCode")
    public ResultVo  getVerifyCode(HttpServletRequest request,HttpServletResponse response){

        ResultVo result = verifyCodeService.createVerifyCode(request,response);

        return result;

    }









}
