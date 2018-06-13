package cn.edu.swpu.jdata_exam.dao;

import cn.edu.swpu.jdata_exam.entity.UserBasicInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 *用户信息操作
 */
@Repository
@Mapper
public interface UserBasicInfoDAO {

    //根据id查询信息
    UserBasicInfo getUserById(String userId);

    //添加用户
    int addUser(UserBasicInfo userBasicInfo);

    //修改密码
    int updatePw(UserBasicInfo userBasicInfo);



}
