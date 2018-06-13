package cn.edu.swpu.jdata_exam.Handle;


import cn.edu.swpu.jdata_exam.enums.CodeEnum;
import cn.edu.swpu.jdata_exam.exception.JdataExamException;
import cn.edu.swpu.jdata_exam.utils.ResultVoUtil;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class JdataExceptionHandle {

    @ResponseBody
    @ExceptionHandler(JdataExamException.class)
    public ResultVo handlerJdataException(JdataExamException je){

        log.info(je.getStackTrace().toString());

        ResultVo resultVo = null;
        if(je.getCode() == 2){
            resultVo = ResultVoUtil.error(je.getCode(),je.getMessage());
        }else {
            resultVo = ResultVoUtil.error(CodeEnum.ERROR.getCode(), je.getMessage());
        }
        return resultVo;
    }

}
