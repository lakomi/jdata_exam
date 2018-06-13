package cn.edu.swpu.jdata_exam.enums;

import lombok.Getter;

@Getter
public enum BackMessageEnum {

    LOGIN_SUCCESS(1,"登录成功"),
    UPDATE_SCORE_SUCCESS(2,"成绩更新成功"),
    PASSWORD_MODIFY_SUCCESS(3,"密码修改成功"),
    FILE_UPLOAD_SUCCESS(4,"文件上传成功"),



    ;

    private Integer code;
    private String message;

    BackMessageEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
