package cn.edu.swpu.jdata_exam.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户分数成绩
 */

@Data
public class UserScore implements Serializable {

    private Integer id;

    private String userId;

    private float userScore;

    private Date dateTime;


}
