select a.os_no,a.os_dd,a.itm,a.bat_no,a.prd_no,a.prd_name,a.id_no, a.up,isnull(d.ut,'') as ut,a.qty,a.amt ,

b.workFlowDescribe, isnull(b.workflowState,0) as workflowState,
 isnull(b.maxWorkFlowStep,0 )  as maxWorkFlowStep , isnull(b.maxWorkFlowName,'') as maxWorkFlowName,
 isnull(b.maxWorkFlowCode,'' ) as maxWorkFlowCode  ,
--isnull(c.modify_dd,0) as photoUpdateTime
 0  as photoUpdateTime
,f.so_data
,g.cus_no
,e.hpgg,e.khxg, isnull(e.so_zxs,0) as  so_zxs    from (


select   os_no,os_dd,itm,bat_no,prd_no,prd_name,id_no, up,qty,amt  from  tf_pos  where os_id='SO'
--订单起止日期  降低查询范围
and  os_dd >'2017-07-01' and (os_no like :os_no or prd_no like :prd_no)
 ) as  a
left outer join
(
--9 表示 订单生产中
select osNo,itm,workflowstate,maxWorkFlowStep,maxWorkFlowName, maxWorkFlowCode,workFlowDescribe from  [yunfei].[dbo].[T_OrderItemWorkState] where workflowstate<>0  and (osNo like :os_no or prdNo like :prd_no)

) as b on a.os_no=b.osNo collate Chinese_PRC_90_CI_AI   and  a.itm=b.itm and b.workflowstate<>VALUE_COMPLETE_STATE

-- left outer JOIN
-- --图片抓取关闭图片修改日期的抓取， ERP 图片改动时候， 客户端是无法感知的。
-- (select  bom_no,modify_dd from mf_bom) as c on a.id_no=c.bom_no


  left outer join  (
                            --单位抓取
                            select prd_no, ut from  prdt where   knd='2'

                            ) d  on  a.prd_no=d.prd_no


            --货品规格 箱规 生产交期数据
             left outer join (
                            select bom_no,hpgg,khxg,so_zxs from  mf_bom_z

                            ) e  on  a.id_no=e.bom_no

                                 -- 生产交期数据
              left outer join (
                            select os_no, so_data ,itm from  tf_pos_z where OS_ID='SO'

                            ) f  on  a.os_no=f.os_no and a.itm=f.itm

             left outer join (

                            select os_no, cus_no   from  mf_pos where OS_ID='SO'   and  os_dd >'2017-01-01'

                            ) g  on  a.os_no=g.os_no


order by a.os_no DESC



