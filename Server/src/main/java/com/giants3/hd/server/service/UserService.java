package com.giants3.hd.server.service;

import com.giants3.hd.server.entity.Authority;
import com.giants3.hd.server.entity.QuoteAuth;

import com.giants3.hd.server.entity.User;
import com.giants3.hd.server.repository.AuthorityRepository;
import com.giants3.hd.server.repository.QuoteAuthRepository;
import com.giants3.hd.server.repository.SessionRepository;
import com.giants3.hd.server.repository.UserRepository;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.exception.HdException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户 业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class UserService extends AbstractService implements InitializingBean, DisposableBean {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private QuoteAuthRepository quoteAuthRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }


    /**
     * 保存用户列表
     * 添加新增的
     * 修改存在的值
     * 不存在的，删除
     *
     * @param newUsers
     * @return
     */
    @Transactional(rollbackFor = {HdException.class})
    public RemoteData<User> saveUserList(List<User> newUsers) throws HdException {
        List<User> oldUsers = userRepository.findAll();

        List<User> removed = new ArrayList<>();

        for (User oldUser : oldUsers) {
            boolean foundInNew = false;
            for (User newUser : newUsers) {

                if (oldUser.id == newUser.id) {
                    foundInNew = true;
                }


            }
            if (!foundInNew) {
                removed.add(oldUser);
            }

        }

        for (User user : newUsers) {


            User oldData = userRepository.findOne(user.id);
            if (oldData == null) {
                user.id = -1;


                //如果是空数据  略过添加
                if (user.isEmpty()) {
                    continue;
                }


            } else {


                user.id = oldData.id;

            }

            userRepository.save(user);


        }


        /**
         *  删除不存在item   标记delete
         */
        for(User deleteUser:removed)
        {

                deleteUser.deleted=true;
                userRepository.save(deleteUser );


        }


        return wrapData( list());
    }



    /**
     * 查询用户列表
     * @return
     */
    public List<User> list()
    {
        return  userRepository.findByDeletedEquals(false );
    }
//    /**
//     * 删除用户 进行关联判断
//     * @param userId
//     * @return
//     */
//    public RemoteData<User> deleteUser(long userId) {
//
//
//        return null;
//    }


    /**
     * 查询是否可以删除   是否有关联的数据
     *
     * @param userId
     * @return
     */
    public boolean canDelete(long userId) {

        List<Authority> authorities=authorityRepository.findByUser_IdEquals(userId);
        if(!ArrayUtils.isEmpty(authorities))
            return false;

        List<QuoteAuth> quoteAuths=quoteAuthRepository.findByUser_IdEquals(userId);

        if(!ArrayUtils.isEmpty(quoteAuths))
            return false;


        if(!ArrayUtils.isEmpty(quoteAuths))
            return false;
        return false
                ;
    }
}
