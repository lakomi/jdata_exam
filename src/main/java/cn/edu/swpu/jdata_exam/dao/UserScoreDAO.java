package cn.edu.swpu.jdata_exam.dao;


import cn.edu.swpu.jdata_exam.entity.UserScore;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户成绩信息操作
 */

@Repository
@Mapper
public interface UserScoreDAO {

    //根据id查询成绩信息
    List<UserScore> getScoreById(String userId);

    //添加成绩
    int addScore(UserScore userScore);

    //更新成绩
//    int updateScore(UserScore userScore);

}
