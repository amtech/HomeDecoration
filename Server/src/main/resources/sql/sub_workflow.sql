
SELECT A.TZ_NO ,A.MO_NO,C.SO_NO AS OS_NO,C.EST_ITM AS ITM,A.MRP_NO,D.workFlowName,D.prd_no, D.completeDate,A.ZC_NO,B.NAME AS ZC_NAME,A.QTY FROM  MF_TZ   A

INNER JOIN (SELECT ZC_NO ,NAME FROM ZC_NO) AS B  ON A.ZC_NO=B.ZC_NO


INNER JOIN (SELECT MO_NO, SO_NO,EST_ITM FROM MF_MO where bil_Id = upper('MP'))  AS C ON A.MO_NO =C.MO_NO

INNER JOIN (

    select aa.* ,bb.mrpNo from (
        SELECT OSNO,ITM,prdNo as prd_no,workflowStep,workFlowName ,endDateString as completeDate,PERCENTAGE FROM [yunfei].[dbo].[T_eRPWORKFLOWREPORT]
            WHERE  WORKFLOWSTEP<>1000 AND WORKFLOWSTEP<>6000
            AND ( (endDate >= :para_start and endDate<= :para_end) or (endDate=0 and WORKFLOWSTEP=2000) ) --白胚不限制要完成（完成了 enddate才有值）
            ) as aa inner  join (
            --关联找出mrp_no
         select distinct osNo,  itm ,mrpNo,currentWorkFlowStep from   [yunfei].[dbo].[T_ErpOrderItemProcess]
        )as bb  on aa.OSNO=bb.osNo    and aa.itm=bb.itm and aa.workflowStep=bb.currentWorkFlowStep

) AS D

ON C.SO_NO=D.OSNO  collate Chinese_PRC_90_CI_AI  AND C.EST_ITM=D.ITM and A.MRP_NO=D.mrpNo collate Chinese_PRC_90_CI_AI and (D.PERCENTAGE>=1 or A.ZC_NO=upper('A07') or  A.ZC_NO=upper('A09')   )



where (A.MRP_NO like :para_name or  b.NAME like  :para_name or A.ZC_NO LIKE  :para_name  or  c.SO_NO  like :para_name or  d.prd_no like :para_name     )  and TZ_NO NOT IN (SELECT TZ_NO FROM TF_WR)

order by c.SO_NO DESC