package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.Authority;
import com.giants3.hd.utils.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *
 * 权限
* Created by davidleen29 on 2014/9/17.
*/
public interface AuthorityRepository extends JpaRepository<Authority,Long> {


    public List<Authority> findByUser_IdEquals(long userId);


   // public List<Authority> findByUser_idEqualsRightJointModule(long userId);
   public  Authority  findFirstByUser_IdEqualsAndModule_IdEquals(long userId,long moduleId);
}
