package com.giants3.hd.server.repository;

import com.giants3.hd.utils.entity.WorkFlowWorker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by davidleen29 on 2017/4/8.
 */
public interface WorkFlowWorkerRepository extends JpaRepository<WorkFlowWorker, Long> {

      List<WorkFlowWorker>  findByUserIdEquals(long userId);

    /**
     * 查找指定流程指定工作人的记录
     * @param userId
     * @param workFlowCode
     * @return
     */
      List<WorkFlowWorker>  findByUserIdEqualsAndWorkFlowCodeEquals(long userId,String  workFlowCode);
       WorkFlowWorker   findFirstByUserIdEqualsAndWorkFlowCodeEqualsAndReceiveEquals(long userId,String  workFlowCode,boolean canReceive);
      List< WorkFlowWorker>   findByUserIdEqualsAndReceiveEquals(long userId, boolean canReceive);
     WorkFlowWorker   findFirstByUserIdEqualsAndWorkFlowCodeEqualsAndSendEquals(long userId,String  workFlowCode,boolean canSend);

    List<WorkFlowWorker> findByUserIdEqualsAndSendEquals(long userId, boolean canSend);
}
