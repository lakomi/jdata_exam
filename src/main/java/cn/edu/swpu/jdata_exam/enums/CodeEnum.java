package cn.edu.swpu.jdata_exam.enums;

import lombok.Getter;

@Getter
public enum CodeEnum {
    SUCCESS(0,"成功"),
    ERROR(1,"失败"),

    ;
    private Integer code;

    private String message;

    CodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
