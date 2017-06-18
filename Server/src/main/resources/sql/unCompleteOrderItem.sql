select a.os_no,a.itm,a.bat_no,a.prd_no,a.prd_name,a.id_no, a.up,a.qty,a.amt ,b.workFlowDescribe, isnull(b.workflowState,0) as workflowState  from (select * from  tf_pos  where os_id='SO'
--订单起止日期  降低查询范围
and  os_dd >'2017-01-01' and (os_no like :os_no or prd_no like :prd_no)
 ) as  a
left outer join
(
--9 表示 订单生产中
select * from  [yunfei].[dbo].[T_OrderItem] where workflowstate<>0  and (osNo like :os_no or prdNo like :prd_no)

) as b on a.os_no=b.OsNo collate Chinese_PRC_90_CI_AI   and  a.itm=b.itm and b.workflowstate<>VALUE_COMPLETE_STATE

order by a.os_no DESC
