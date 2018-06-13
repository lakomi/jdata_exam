package cn.edu.swpu.jdata_exam.enums;


import lombok.Getter;

@Getter
public enum ExceptionEnum {

    USER_NOT_EXIT(1,"用户名不存在"),
    PASSWORD_ERROR(9,"密码错误"),
    GET_USERDETAILS_ERROR(3,"登录中获取用户详情出错"),
    UPDATE_SCORE_FAIL(7,"更新成绩失败"),
    CANNOT_ACCESS_OTHER(8,"你不能查看他人的成绩"),
    REPEAT_LOGIN(2,"请重新登录"),
    FILE_EMPTY(10,"文件为空,请重新上传"),
    FILE_UPLOAD_FAILED(11,"文件上传失败,请重新上传"),
    FILE_FORMAT_ERROR(12,"文件命名格式或内容格式不正确,请重新上传"),
    SSH_UPLOAD_FAILED(14,"ssh上传失败,请重新上传"),
    FILE_NAME_ERROR(15,"文件命名错误"),
    PLEASE_LOGIN_FIRST(16,"请先登录"),
    GET_CODE_ERROR(17,"获取验证码失败"),
    VERIFY_CODE_ERROR(19,"验证码错误"),
    FILE_HAS_UPLOADED(21,"每天只能提交一次结果，你今天已经提交过了"),
    NOT_PERMISSION(22,"权限不够，无法访问"),
    SUBMIT_NOT_OPEN(23,"该通道尚未开通"),
    DONOT_MODIFY_OTHER(24,"你不能修改别人的密码"),
    OLD_PASSWORD_ERROR(25,"旧密码错误"),




    ;

    private Integer code;

    private String message;

    ExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
