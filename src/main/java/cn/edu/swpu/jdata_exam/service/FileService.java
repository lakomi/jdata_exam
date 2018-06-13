package cn.edu.swpu.jdata_exam.service;

import cn.edu.swpu.jdata_exam.vo.ResultVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileService {

    ResultVo uploadFile(HttpServletRequest request, MultipartFile multipartFile);


    ResultVo submitFile(HttpServletRequest request,MultipartFile multipartFile);
}
