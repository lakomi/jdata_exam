package cn.edu.swpu.jdata_exam.service.impl;

import cn.edu.swpu.jdata_exam.dao.UserScoreDAO;
import cn.edu.swpu.jdata_exam.entity.UserScore;
import cn.edu.swpu.jdata_exam.entity.dto.ResponseUserScore;
import cn.edu.swpu.jdata_exam.enums.ExceptionEnum;
import cn.edu.swpu.jdata_exam.exception.JdataExamException;
import cn.edu.swpu.jdata_exam.service.UserScoreService;
import cn.edu.swpu.jdata_exam.utils.AuthenticationUtil;
import cn.edu.swpu.jdata_exam.utils.ResultVoUtil;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
public class UserScoreServiceImpl implements UserScoreService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserScoreDAO userScoreDAO;

    //成绩缓存时间 30分钟
    private long expirationTime = 30*60;


    /**
     * 获取成绩：
     *      如果该id缓存存在，则从缓存中获取信息
     *      如果缓存中不存在，则从数据库中获取，然后存入缓存
     */
    @Override
    public ResultVo getScoreById() {

        log.info("进入UserScoreServiceImpl的getScoreById");

        String userId = AuthenticationUtil.getAuthUserId();

        ValueOperations<String, UserScore> operations = redisTemplate.opsForValue();
        UserScore userScore = null;
        ResponseUserScore responseUserScore = new ResponseUserScore();

        String key = userId + "_score";

        //判断缓存中是否存在该id的信息
        if (redisTemplate.hasKey(key)) {
            userScore = operations.get(key);
            log.info("从缓存中取值");
            BeanUtils.copyProperties(userScore, responseUserScore);

        } else {
            //根据userId查出成绩信息
            List<UserScore> userScoreList = userScoreDAO.getScoreById(userId);
            if (userScoreList.isEmpty()) {

                responseUserScore.setUserId(userId);
                responseUserScore.setUserScore(0);
            } else {

                userScore = (UserScore) userScoreList.toArray()[0];
                BeanUtils.copyProperties(userScore, responseUserScore);
                log.info("userId={}的最好成绩是{}", userId, userScore);
                //存入缓存
                operations.set(userId + "_score", userScore, expirationTime, TimeUnit.SECONDS);
                log.info("userId={}已存入缓存", userId);
            }
        }

        return ResultVoUtil.success(responseUserScore);
    }

    //检验用户token中的userId与查询的userId是否一致。
    public boolean checkId(String userId){
        String authUserId = AuthenticationUtil.getAuthUserId();

        log.info("AuthUserId={}",authUserId);
        log.info("userId={}",userId);

        //不一致
        if (!userId.equals(authUserId)){
            return false;
        }

        return true;
    }


}
