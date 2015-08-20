package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.QuoteAuth;
import com.giants3.hd.utils.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 报价权限
* Created by davidleen29 on 2014/9/17.
*/
public interface QuoteAuthRepository extends JpaRepository<QuoteAuth,Long> {





    public  QuoteAuth findFirstByUser_IdEquals(long userId);
}