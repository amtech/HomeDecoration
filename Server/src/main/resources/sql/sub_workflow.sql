
SELECT A.TZ_NO ,A.MO_NO,C.SO_NO AS OS_NO,C.EST_ITM AS ITM,A.MRP_NO,D.workFlowName,D.prd_no, D.completeDate,A.ZC_NO,B.NAME AS ZC_NAME,A.QTY FROM  MF_TZ   A

INNER JOIN (SELECT ZC_NO ,NAME FROM ZC_NO) AS B  ON A.ZC_NO=B.ZC_NO


INNER JOIN (SELECT MO_NO, SO_NO,EST_ITM FROM MF_MO)  AS C ON A.MO_NO =C.MO_NO

INNER JOIN (
    SELECT OSNO,ITM,prdNo as prd_no,workFlowName ,endDateString as completeDate FROM [yunfei].[dbo].[T_eRPWORKFLOWREPORT]
        WHERE PERCENTAGE>=1 AND WORKFLOWSTEP<>1000 AND WORKFLOWSTEP<>6000
        AND endDate >= :para_start and endDate<= :para_end

) AS D

ON C.SO_NO=D.OSNO  collate Chinese_PRC_90_CI_AI  AND C.EST_ITM=D.ITM


where ( b.NAME like  :para_name or b.ZC_NO LIKE  :para_name  or  c.SO_NO  like :para_name or  d.prd_no like :para_name  )  and TZ_NO NOT IN (SELECT TZ_NO FROM TF_WR)

order by c.SO_NO DESC