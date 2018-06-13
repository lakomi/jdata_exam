package cn.edu.swpu.jdata_exam.service;


import cn.edu.swpu.jdata_exam.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface VerifyCodeService {

    ResultVo createVerifyCode(HttpServletRequest request,HttpServletResponse response);

    ResultVo checkVerifyCode(HttpServletRequest request,String userCode);



}
