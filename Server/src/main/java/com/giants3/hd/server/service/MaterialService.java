package com.giants3.hd.server.service;

import com.giants3.hd.server.controller.MaterialController;
import com.giants3.hd.server.entity.GlobalData;
import com.giants3.hd.server.entity.Material;
import com.giants3.hd.server.entity.MaterialClass;
import com.giants3.hd.server.entity.ProductToUpdate;
import com.giants3.hd.server.entity_erp.Prdt;
import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.noEntity.ProductDetail;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.RemoteData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by david on 2016/4/16.
 */
@Service
public class MaterialService extends AbstractService {


    private static final java.lang.String TAG = "MaterialService";
    ErpPrdtRepository erpPrdtRepository;
    EntityManager manager;

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GlobalDataService globalDataService;

    @Autowired
    private MaterialClassRepository  materialClassRepository;


    @Autowired
    private ProductToUpdateRepository productToUpdateRepository;

    @Autowired
    private MaterialRepository materialRepository;
    @Resource
    private PlatformTransactionManager transactionManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        EntityManagerHelper helper = EntityManagerHelper.getErp();
        manager = helper.getEntityManager();
        erpPrdtRepository = new ErpPrdtRepository(manager);
    }


    /**
     * 判断数据是否一致
     * @param material
     * @param prdt
     * @return
     */
    public boolean compare(Material material, Prdt prdt)
    {

        if(material.id<=0) return false;
        if( !material.code.equals(prdt.prd_no ))
            return false;


        if( !material.name.equals(prdt.name ))
            return false;
        if( !material.unitName.equals(prdt.ut ))
            return false;
        if( !material.spec.equals(prdt.spec ))
            return false;
//       if(  Float.compare(material.price,prdt.price)!=0)
        if(Math.abs(material.price-prdt.price)>0.01f)
            return false;
        if(material.memo==null&&prdt.rem!=null )
        {

                return false;
        }else
        if(material.memo!=null&&prdt.rem==null )
        {
            return false;
        } else
        if (material.memo!=null &&!material.memo.equals(prdt.rem ))
                return false;

        if(  Float.compare(material.typeId,prdt.type)!=0)
            return false;
        if( material.classId==null||!material.classId.equals(prdt.classId ))
            return false;
        if(material.className==null|| !material.className.equals(prdt.className ))
            return false;




        if( material.available!=prdt.available )
            return false;





        if(  Float.compare(material.discount,prdt.discount)!=0)
            return false;


        if(  Float.compare(material.wLong,prdt.wLong)!=0)
            return false;


        if(  Float.compare(material.wWidth,prdt.wWidth)!=0)
            return false;

        if(  Float.compare(material.wHeight,prdt.wHeight)!=0)
            return false;

        if( material.outOfService!=(prdt.nouse_dd >0))
        {

            return false;
        }


        return true;


    }

    /**
     * erp 数据转换成材料数据
     * @param material
     * @param prdt
     * @param materialController
     */
    public boolean   convert(Material material, Prdt prdt, MaterialController materialController)
    {




        if(compare(material,prdt))
        {

            return true;
        }
        //todo 数据转化  更详细的数据



            material.code=prdt.prd_no;



        material.name=prdt.name;
        material.unitName=prdt.ut;

        material.spec=prdt.spec;
        material.price=prdt.price;
        material.memo=prdt.rem;
        material.typeId=prdt.type;


        material.classId=prdt.classId;
        material.className=prdt.className;

        material.available=prdt.available;
        material.discount=prdt.discount;
        material.wLong=prdt.wLong;
        material.wWidth=prdt.wWidth;
        material.wHeight=prdt.wHeight;


        //停用消息
        material.outOfService=prdt.nouse_dd >0;
        material.outOfServiceDate=prdt.nouse_dd;
        material.outOfServiceDateString=prdt.nouse_dd <=0?"": DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(prdt.nouse_dd));





        return false;




    }



    private static final int  UPDATE_PRODUCT_PAGE_SIZE=100;

    /**
     * 更新产品统计数据信息
     *
     *
     * 检查ProductToUpdateBiao是否有数据 ，有 则跟新指定的产品的统计信息
     */
    @Transactional
    public  void updateProductData( )
    {



        //读取第一百条 更新
        Pageable pageable=new PageRequest(0,UPDATE_PRODUCT_PAGE_SIZE);
        Page<ProductToUpdate> page=productToUpdateRepository.findAll(pageable);

        boolean hasNext=page.hasNext();
        if(page.getSize()>0)
        {

            List<ProductToUpdate> productIds=page.getContent();
       GlobalData globalData=globalDataService.findCurrentGlobalData();



        //修正关联产品的统计数据
        int size = productIds.size();
            long totalSize=page.getTotalElements();

        if(size >0)
        {


            for(int i=0;i<size;i++  ) {

                long productId=productIds.get(i).productId;

                ProductDetail productDetail=   productService.findProductDetailById(productId);
                if(productDetail!=null) {

                    productDetail.updateProductStatistics(globalData);
                    productRepository.save(productDetail.product);
                    Logger.getLogger(TAG).info("productId:"+productId+" has Update!");
                }



            }

        }

            Logger.getLogger(TAG).info("not update size :"+(totalSize-size));

        productToUpdateRepository.delete(productIds);


           // commit()

            if(hasNext)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateProductData();
                    }
                }).start();

            }
        }

    }

    public Material findMaterial(long materialId) {

        return materialRepository.findOne(materialId);
    }

    public void save(Material material) {

        materialRepository.save(material);
    }


    /**
     * 更新材料分类。
     * @param materialClass
     * @return
     */
    public RemoteData<MaterialClass> updateClass(MaterialClass materialClass) {




        return  wrapData(materialClassRepository.save(materialClass));



    }

    /**
     * 删除材料类型类别
     * @param materialClassId
     * @return
     */

    @Transactional
    public RemoteData<Void> deleteClass(long materialClassId) {


        MaterialClass materialClass =materialClassRepository.findOne(materialClassId);
        if(materialClass==null)
            return wrapError("材料类别不存在");



        Material material =materialRepository.findFirstByClassIdEquals(materialClass.code);
        if(material!=null)
        {

            return wrapError("该材料类别正在使用，材料："+material.code+","+material.name+" ...等");
        }


        materialClassRepository.delete(materialClassId);



        return wrapData();
    }
}
