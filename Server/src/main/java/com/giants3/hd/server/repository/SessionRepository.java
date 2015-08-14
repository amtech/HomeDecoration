package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.Authority;
import com.giants3.hd.utils.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * 会话状态
* Created by davidleen29 on 2014/9/17.
*/
public interface SessionRepository extends JpaRepository<Session,Long> {


    public  Session  findFirstByUser_IdEqualsOrderByLoginTimeDesc(long userId);


    public  Session  findFirstByTokenEquals(String token);
}
