package com.giants3.hd.server.parser;

import com.giants3.hd.appdata.AUser;
import com.giants3.hd.utils.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**  桌面端User 转换成app 端User 数据
 *
 * Created by david on 2016/1/2.
 */
@Component("CustomImplName")
//  qualifier     CustomImplName
public class UserParser implements DataParser<User,AUser> {


    @Override
    public AUser parse(User data) {
        AUser aUser=new AUser();
        aUser.chineseName=data.chineseName;
        aUser.name=data.name;
        aUser.id=data.id;
        aUser.code=data.code;

        aUser.isSalesman=data.isSalesman;
        aUser.tel=data.tel;
        aUser.email=data.email;
        return aUser;
    }
}
