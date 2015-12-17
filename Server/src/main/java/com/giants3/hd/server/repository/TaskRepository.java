package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.HdTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户
* Created by davidleen29 on 2014/9/17.
*/
public interface TaskRepository extends JpaRepository<HdTask,Long> {


    //List<HdTask> findByTaskTypeEqualsAndDateBiggerThan(int  type, long time);
    List<HdTask> findByDateGreaterThan(long time);


}
