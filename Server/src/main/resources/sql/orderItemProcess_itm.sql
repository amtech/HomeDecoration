
SELECT   a.MO_DD as moDd,	a.MO_NO as moNo,	a.STA_DD as staDd,	a.END_DD as endDd	,a.SO_NO AS osNo	,a.EST_ITM as ITM , 	a.MRP_NO as mrpNo,	a.MO_NO_ADD AS prdNo,a.id_no as idNo,	a.QTY,b.jgh,b.scsx,b.so_zxs

  FROM [DB_YF01].[dbo].[MF_MO]  a inner join mf_mo_z  b on a.mo_no=b.mo_no
  where
   --(a.mrp_no like 'A%' or  a.mrp_no like 'B%' or  a.mrp_no like 'C%' or  a.mrp_no like 'D%')  and

   a.so_no =  :os_no and  a.EST_ITM= :itm

   order by a.so_no desc,a.mrp_no ASC