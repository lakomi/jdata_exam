package cn.edu.swpu.jdata_exam.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 验证码配置信息
 */
@Component
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha getDefaultKaptcha(){

        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();

        Properties properties = new Properties();

        //验证码图片边框
        properties.setProperty("kaptcha.border","yes");

        //边框颜色
        properties.setProperty("kaptcha.border.color","yellow");

        //图片宽
        properties.setProperty("kaptcha.image.width", "200");

        //图片高
        properties.setProperty("kaptcha.image.height", "60");

        //字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "40");

        //字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");

        //图片样式
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");

        //验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");

        //验证码文本字体样式
        properties.setProperty("kaptcha.textproducer.font.names","宋体,楷体,微软雅黑");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;

    }


}
