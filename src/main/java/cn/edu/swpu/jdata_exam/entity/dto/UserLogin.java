package cn.edu.swpu.jdata_exam.entity.dto;

import lombok.Data;

@Data
public class UserLogin {

    private String userId;

    private String userPassword;

    private String verifyCode;
}
