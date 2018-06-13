package cn.edu.swpu.jdata_exam.service.impl;

import cn.edu.swpu.jdata_exam.entity.dto.VerifyCodeDTO;
import cn.edu.swpu.jdata_exam.enums.CodeEnum;
import cn.edu.swpu.jdata_exam.enums.ExceptionEnum;
import cn.edu.swpu.jdata_exam.exception.JdataExamException;
import cn.edu.swpu.jdata_exam.service.VerifyCodeService;
import cn.edu.swpu.jdata_exam.utils.ResultVoUtil;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static int verifyCodeExpire = 2 * 60; //超时时间2min

    private String codeHeader = "Verify-Code";
    /**
     * 生成验证码
     * @param response
     * @return
     */
    @Override
    public ResultVo createVerifyCode(HttpServletRequest request,HttpServletResponse response) {

        //base64编码
        BASE64Encoder encoder = new BASE64Encoder();

        //验证码返回信息
        VerifyCodeDTO verifyCodeDTO = new VerifyCodeDTO();

        //生成验证码对应的key
        String verifyCodeKey = UUID.randomUUID().toString();

        verifyCodeDTO.setVerifyKey(verifyCodeKey);

        //生成验证码文本
        String verifyCode = defaultKaptcha.createText();

        System.out.println("验证码key="+verifyCodeKey+",验证码="+verifyCode);

        //将验证码存入redis中
        redisTemplate.opsForValue().set(verifyCodeKey,verifyCode,verifyCodeExpire,TimeUnit.SECONDS);

        //将生成的验证码放入图片中生成 图片验证码
        BufferedImage verifyCodeImage = defaultKaptcha.createImage(verifyCode);

        //将redis中验证码对应的key  返回
        response.addHeader(codeHeader,verifyCodeKey);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();


        try {
            ImageIO.write(verifyCodeImage,"png",bao);
            byte[] bytes = bao.toByteArray();
            String result = encoder.encode(bytes);

            verifyCodeDTO.setVerifyCode(result);

            //返回信息
            return ResultVoUtil.success(verifyCodeDTO);

        } catch (IOException e) {
            throw new JdataExamException(ExceptionEnum.GET_CODE_ERROR);
        }

    }

    @Override
    public ResultVo checkVerifyCode(HttpServletRequest request,String userCode) {

        //从header中获取redis中验证码对应的key
        String verifyCodeKey = request.getHeader(codeHeader);

        System.out.println("verifyCodeKey="+verifyCodeKey);

        ResultVo resultVo = ResultVoUtil.success();

        //header中没有verifyCodeKey
        if (verifyCodeKey == null){
            resultVo = ResultVoUtil.error(CodeEnum.ERROR.getCode(),ExceptionEnum.VERIFY_CODE_ERROR.getMessage());
            return resultVo;
        }

        //检查redis中是否存在该key，若无则说明该验证码已过期，则重新获取
        if (!redisTemplate.hasKey(verifyCodeKey)){
            resultVo = ResultVoUtil.error(CodeEnum.ERROR.getCode(),ExceptionEnum.VERIFY_CODE_ERROR.getMessage());
            return resultVo;
        }

        //根据key从redis中取出对应的验证码
        String verifyCode = redisTemplate.opsForValue().get(verifyCodeKey);
        System.out.println("verifyCode="+verifyCode);

        //若用户输入的验证码与redis中的不匹配
        if (!verifyCode.equals(userCode)){
            resultVo = ResultVoUtil.error(CodeEnum.ERROR.getCode(),ExceptionEnum.VERIFY_CODE_ERROR.getMessage());
            return resultVo;
        }

        //若验证成功，则从redis中删除该验证码
        redisTemplate.delete(verifyCodeKey);

        return resultVo;
    }
}
