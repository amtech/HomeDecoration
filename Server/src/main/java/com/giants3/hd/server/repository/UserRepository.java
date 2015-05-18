package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
* Created by Administrator on 2014/9/17.
*/
public interface UserRepository extends JpaRepository<User,Long> {


    public List<User> findByFirstNameLike(String firstName);

}
