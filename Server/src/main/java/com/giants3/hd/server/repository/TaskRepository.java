package com.giants3.hd.server.repository;
//

import com.giants3.hd.utils.entity.HdTask;
import com.giants3.hd.utils.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户
* Created by davidleen29 on 2014/9/17.
*/
public interface TaskRepository extends JpaRepository<HdTask,Long> {



        Page<HdTask> findByTaskNameLikeOrderByStartDateDesc( String name, Pageable pageable);

}
