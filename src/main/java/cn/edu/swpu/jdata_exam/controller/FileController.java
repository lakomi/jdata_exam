package cn.edu.swpu.jdata_exam.controller;


import cn.edu.swpu.jdata_exam.service.FileService;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

@Slf4j
@RestController
@RequestMapping("/file")
@CrossOrigin(allowedHeaders = "*")
public class FileController {

    @Autowired
    private FileService fileService;

    //(allowedHeaders="*",allowCredentials="true")

    /**
     * 服务器位置
     **/

    @RequestMapping("/upload")
    public Callable<ResultVo> singleFileUpload(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file) throws Exception {

        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");//你的编码格式
        }

        log.info("upload 主线程开始");

        Callable<ResultVo> resultVoCallable = new Callable<ResultVo>() {
            @Override
            public ResultVo call() throws Exception {

                log.info("fileService.uploadFile 副线程开始");
                ResultVo resultVo = fileService.uploadFile(request,file);
                log.info("fileService.uploadFile 副线程返回");

                return resultVo;
            }
        };

        log.info("upload 主线程返回");

        return resultVoCallable;
    }



    @RequestMapping("/submit")
    public ResultVo submitFile(HttpServletRequest request,
                               @RequestParam("file") MultipartFile file){

        ResultVo resultVo = fileService.submitFile(request,file);

        return resultVo;
    }

}
