package cn.edu.swpu.jdata_exam.service.impl;

import cn.edu.swpu.jdata_exam.enums.BackMessageEnum;
import cn.edu.swpu.jdata_exam.enums.ExceptionEnum;
import cn.edu.swpu.jdata_exam.exception.JdataExamException;
import cn.edu.swpu.jdata_exam.service.FileService;
import cn.edu.swpu.jdata_exam.utils.AuthenticationUtil;
import cn.edu.swpu.jdata_exam.utils.ResultVoUtil;
import cn.edu.swpu.jdata_exam.utils.util.CsvUtil;
import cn.edu.swpu.jdata_exam.utils.util.DateTimeUtil;
import cn.edu.swpu.jdata_exam.utils.util.LocalExecute;
import cn.edu.swpu.jdata_exam.utils.util.SshUtil;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public ResultVo uploadFile(HttpServletRequest request, MultipartFile file) {

//        String AuthUserId = AuthenticationUtil.getAuthUserId();
//
//        if(redisTemplate.hasKey(AuthUserId)){
//            throw new JdataExamException(ExceptionEnum.FILE_HAS_UPLOADED);
//        }


        /**判断是否为空**/
        if (file.isEmpty()) {
            throw new JdataExamException(ExceptionEnum.FILE_EMPTY);
        }
        String filePath = CsvUtil.UPLOADED_LOCAL_FOLDER + file.getOriginalFilename();
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath);

            System.out.println(file.getOriginalFilename());

            Files.write(path, bytes);
        } catch (Exception e) {

            System.out.println("上传本地失败！");

            e.printStackTrace();

            throw new JdataExamException(ExceptionEnum.FILE_UPLOAD_FAILED);

        }

        if(!CsvUtil.read(filePath,file.getOriginalFilename())){

            log.info("csv验证不通过。格式内容错误");

            throw new JdataExamException(ExceptionEnum.FILE_FORMAT_ERROR);
        }

        log.info("本地上传成功");
//        System.out.println("上传本地成功！");

        try{

            if (!SshUtil.putFile(filePath)){

                log.info("转发服务器出错");

                throw new JdataExamException(ExceptionEnum.FILE_UPLOAD_FAILED);

            }
            log.info("ssh上传文件成功,删除本地临时文件...");

            LocalExecute.removeFile(filePath);

        }catch (Exception e){

            log.error("ssh上传文件失败,或删除文件失败");

            throw new JdataExamException(ExceptionEnum.SSH_UPLOAD_FAILED);
        }



//        Calendar calendar = Calendar.getInstance();
//        int hours = calendar.get(Calendar.HOUR_OF_DAY);
//        int minutes = calendar.get(Calendar.MINUTE);
//        //距离第二天的时间，作为redis中用户失效的时间
//        int time = (23-hours) * 60 + (59-minutes);
//        redisTemplate.opsForValue().set(AuthUserId,1,time,TimeUnit.MINUTES);

        return ResultVoUtil.success(BackMessageEnum.FILE_UPLOAD_SUCCESS);

    }

    @Override
    public ResultVo submitFile(HttpServletRequest request, MultipartFile file) {

//        Calendar calendar = Calendar.getInstance();
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
        boolean flag = checkTime();
        if (flag == false){
            throw new JdataExamException(ExceptionEnum.SUBMIT_NOT_OPEN);
        }

        String filePath = CsvUtil.UPLOADED_LOCAL_FOLDER + file.getOriginalFilename();
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath);

            System.out.println(file.getOriginalFilename());

            Files.write(path, bytes);
        } catch (Exception e) {
            System.out.println("上传本地失败！");

            e.printStackTrace();

            throw new JdataExamException(ExceptionEnum.FILE_UPLOAD_FAILED);

        }
        System.out.println("上传本地成功！");

        try {

            if (!SshUtil.putFile(filePath)){

                throw new JdataExamException(ExceptionEnum.FILE_UPLOAD_FAILED);

            }
            log.info("ssh上传文件成功,删除本地临时文件...");

            LocalExecute.removeFile(filePath);

        }catch (Exception e){

            log.error("ssh上传文件失败,或删除文件失败");

            throw new JdataExamException(ExceptionEnum.SSH_UPLOAD_FAILED);
        }


        return ResultVoUtil.success(BackMessageEnum.FILE_UPLOAD_SUCCESS);
    }




    public boolean checkTime(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if (day == 23){
            if (hours >= 18){
                return true;
            }else {
                return false;
            }
        }else if (day == 24){
            return true;
        }else {
            return false;
        }
    }
}
