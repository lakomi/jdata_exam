package cn.edu.swpu.jdata_exam.dao;

import cn.edu.swpu.jdata_exam.entity.UserBasicInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserBasicInfoDAOTest {

    @Autowired
    private UserBasicInfoDAO userBasicInfoDAO;


    @Test
    public void getUserById() {
        UserBasicInfo userBasicInfo = userBasicInfoDAO.getUserById("201631063402");
        Assert.assertEquals("123123",userBasicInfo.getUserPassword());
    }

    @Test
    public void addUser() {
    }

    @Test
    public void updatePw() {
    }
}