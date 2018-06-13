package cn.edu.swpu.jdata_exam.support;

import cn.edu.swpu.jdata_exam.dao.UserBasicInfoDAO;
import cn.edu.swpu.jdata_exam.entity.UserBasicInfo;
import cn.edu.swpu.jdata_exam.enums.ExceptionEnum;
import cn.edu.swpu.jdata_exam.exception.JdataExamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MyUserInfoService implements UserDetailsService {

    @Autowired
    private UserBasicInfoDAO userBasicInfoDAO;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("进入MyUserDetailService的loadUserByUsername方法");

        //根据用户名查找用户信息
        UserBasicInfo userBasicInfo = userBasicInfoDAO.getUserById(userId);

        if(StringUtils.isEmpty(userBasicInfo)){
            log.info("用户名不存在，userId={}",userId);
            throw new JdataExamException(ExceptionEnum.USER_NOT_EXIT);
        }

        log.info("登录用户名：userId={}",userId);

        //根据查找到的用户信息判断用户是否被冻结
        //.....

        String password = userBasicInfo.getUserPassword();


//        //GrantedAuthority是security提供的权限类，
//        List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
//
//        //获取该用户角色，并放入list中
//        getRoles(userBasicInfo,grantedAuthorityList);


        return new User(userId,password,AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
    }


    /**
     * 获取对应的角色
     * @param userBasicInfo
     * @param grantedAuthorityList
     */
//    public void getRoles(UserBasicInfo userBasicInfo,List<GrantedAuthority> grantedAuthorityList){
//        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_"+userBasicInfo.getRole()));
//    }



}
