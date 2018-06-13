package cn.edu.swpu.jdata_exam.service;

import cn.edu.swpu.jdata_exam.entity.dto.UserLogin;
import cn.edu.swpu.jdata_exam.entity.dto.UserUpdatePw;
import cn.edu.swpu.jdata_exam.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserBasicInfoService {

    //用户登录
    ResultVo userLogin(UserLogin userLogin, HttpServletRequest request, HttpServletResponse response);

    //用户修改密码
    ResultVo updateUserPw(UserUpdatePw userUpdatePw,HttpServletRequest request);



}
