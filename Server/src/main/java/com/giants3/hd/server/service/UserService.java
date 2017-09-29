package com.giants3.hd.server.service;

import com.giants3.hd.utils.DigestUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.Authority;
import com.giants3.hd.entity.QuoteAuth;

import com.giants3.hd.entity.User;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.exception.HdException;
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

    @Autowired
    OrderAuthRepository orderAuthRepository;
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
    public void saveUserList(List<User> newUsers) throws HdException {
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
                if(StringUtils.isEmpty(user.password))//没有修改密码
                {
                    //复制出旧密码，保证密码不变
                    user.password= oldData.password;
                    user.passwordMD5=oldData.passwordMD5;
                }else
                {
                    user.passwordMD5= DigestUtils.md5(user.password);
                }

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



    }



    /**
     * 查询用户列表
     * @return
     */
    public List<User> list()
    {


        final List<User> users = userRepository.findByDeletedEqualsOrderByCode(false);
        removePassword(users);

        return users;
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


    /**
     * 根据saleId 来获取用户code  如果saleid=-1 则获取该用户所关联的所有业务员codes
     * @param loginUserId
     * @param salesId
     * @return
     */
    public   List<String>  extractUserCodes(long  loginUserId, long salesId,String relateSales)
    {

        List<String > salesNos=new ArrayList<>();
        if(salesId==-1)
        {


                String[] ids = relateSales.split(",");
                for (String id : ids) {
                    try {
                        long longId = Long.valueOf(id);
                        User user = userRepository.findOne(longId);
                        if (user != null)
                            salesNos.add(user.code);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }


            }

        }else
        {

            User user = userRepository.findOne(salesId);
            if(user!=null)
            {

                salesNos.add(user.code);
            }
        }

        return salesNos;
    }

    public List<User> getSalesman() {

        final List<User> byIsSalesman = userRepository.findByIsSalesman(true);
        removePassword(byIsSalesman);
        return byIsSalesman;
    }


    private  void removePassword(List<User> users)
    {
        //清除password
        for(User user:users)
        {
            user.password=null;
            user.passwordMD5=null;
        }
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @Transactional
    public RemoteData<Void> updatePassword(User loginUser,String oldPassword, String newPassword) {

            String md5Old= DigestUtils.md5(loginUser.password);
        if(oldPassword.equals(md5Old)|| oldPassword.equals(loginUser.passwordMD5))
        {

            loginUser.passwordMD5=newPassword;
            userRepository.save(loginUser);

            return wrapData();

        }else
        {
            return  wrapError("旧密码不正确");
        }



    }

    @Transactional
    public void adjustPasswordToMd5() {

        List<User> users=userRepository.findAll();
        List<User> updateUsers=new ArrayList<>();
        for(User user:users)
        {
            if(StringUtils.isEmpty(user.passwordMD5)&&!StringUtils.isEmpty(user.password))
            {
                user.passwordMD5=DigestUtils.md5(user.password);
                updateUsers.add(user);
            }
        }

        if(updateUsers.size()>0)
        {
            userRepository.save(updateUsers);
        }
    }
}
