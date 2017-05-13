
SELECT   a.MO_DD,	a.MO_NO,	a.STA_DD,	a.END_DD	,a.SO_NO AS OS_NO	,a.EST_ITM as ITM , 	a.MRP_NO,	a.MO_NO_ADD AS PRD_NO,	a.QTY,b.jgh,b.scsx

  FROM [DB_YF01].[dbo].[MF_MO]  a inner join mf_mo_z  b on a.mo_no=b.mo_no
  where (a.mrp_no like 'A%' or  a.mrp_no like 'B%' or  a.mrp_no like 'C%' or  a.mrp_no like 'D%')
  and a.so_no =  :os_no and  a.MO_NO_ADD= :prd_no

   order by a.so_no desc