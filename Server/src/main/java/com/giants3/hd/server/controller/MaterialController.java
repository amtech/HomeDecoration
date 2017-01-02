package com.giants3.hd.server.controller;


import com.giants3.hd.server.entity.*;
import com.giants3.hd.server.entity_erp.Prdt;
import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.service.GlobalDataService;
import com.giants3.hd.server.service.MaterialService;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.pools.ObjectPool;
import com.giants3.hd.utils.pools.PoolCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;

/**
* 材料信息控制类。
*/
@Controller
@RequestMapping("/material")
public class MaterialController extends BaseController {

    private static final String TAG="MaterialController";

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialClassRepository  materialClassRepository;


    @Autowired
    private JpaRepository<MaterialType,Long> materialTypeRepository;

    @Autowired
    private JpaRepository<MaterialEquation,Long> equationRepository;



    @Autowired
    private ProductMaterialRepository productMaterialRepository;


    @Autowired
    private ProductPaintRepository productPaintRepository;



    @Autowired
    private ProductRepository productRepository;



// @Autowired
//    private GlobalDataRepository globalDataRepository;
    @Autowired
    private GlobalDataService globalDataService;


    @Autowired
    private MaterialService materialService;

    ObjectPool<Material> materialObjectPool;

    private GlobalData globalData;
    @Autowired
    private ProductToUpdateRepository productToUpdateRepository;

  //  ErpPrdtRepository repository;


    @Value("${materialfilepath}")
    private String rootFilePath;






    public MaterialController() {

        materialObjectPool= PoolCenter.getObjectPool(Material.class);

    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Material> list( )   {



        return wrapData(materialRepository.findAll());
    }






    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Material> searchMaterial(@RequestParam(value = "codeOrName",required = false,defaultValue ="") String codeOrName,@RequestParam(value = "classId",required = false,defaultValue ="") String classId
            ,@RequestParam(value = "pageIndex",required = false,defaultValue ="0") int pageIndex,@RequestParam(value = "pageSize",required = false,defaultValue =  "20") int pageSize)   {

        Pageable pageable=constructPageSpecification(pageIndex, pageSize,sortByParam(Sort.Direction.ASC,"code"));
        String searchValue="%" + codeOrName.trim() + "%";
        Page<Material>  pageValue;
        if(StringUtils.isEmpty(classId)) {

            pageValue = materialRepository.findByCodeLikeOrNameLike(searchValue, searchValue, pageable);
        }else
        {
            pageValue = materialRepository.findByCodeLikeAndClassIdOrNameLikeAndClassIdEquals(searchValue,classId, searchValue,classId, pageable);
        }

        List<Material> materials=pageValue.getContent();
        return  wrapData(pageIndex,pageable.getPageSize(),pageValue.getTotalPages(), (int) pageValue.getTotalElements(),materials);
    }
    @RequestMapping(value = "/searchInService", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Material> searchInService(@RequestParam(value = "codeOrName",required = false,defaultValue ="") String codeOrName,@RequestParam(value = "classId",required = false,defaultValue ="") String classId
            ,@RequestParam(value = "pageIndex",required = false,defaultValue ="0") int pageIndex,@RequestParam(value = "pageSize",required = false,defaultValue =  "20") int pageSize)   {

        Pageable pageable=constructPageSpecification(pageIndex, pageSize,sortByParam(Sort.Direction.ASC,"code"));
        String searchValue="%" + codeOrName.trim() + "%";
        Page<Material>  pageValue;
        if(StringUtils.isEmpty(classId)) {

            pageValue = materialRepository.findByCodeLikeAndOutOfServiceNotOrNameLikeAndOutOfServiceNot(searchValue,true, searchValue,true, pageable);
        }else
        {
            pageValue = materialRepository.findByCodeLikeAndClassIdOrNameLikeAndClassIdEquals(searchValue,classId, searchValue,classId, pageable);
        }

        List<Material> materials=pageValue.getContent();
        return  wrapData(pageIndex,pageable.getPageSize(),pageValue.getTotalPages(), (int) pageValue.getTotalElements(),materials);
    }


    @RequestMapping(value = "/saveList",method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<Void> saveList(@RequestBody  List<Material> materials )   {

        for(Material material:materials)
        {


            Material oldData=materialRepository.findFirstByCodeEquals(material.code);
            if(oldData==null)
            {
                material.id=-1;


            }else
            {
                material.id=oldData.id;

                //价格比较

            }
            save(material);



        }



        return wrapData(new ArrayList<Void>());
    }


    /**
     * 保存覆盖旧erp 的数据
     * @param prdt
     * @return 产生关联的产品数据
     */
    public void savePrdt(Prdt prdt,Set<Long> productIds)
    {






        Material oldData=materialRepository.findFirstByCodeEquals(prdt.prd_no);
        boolean isNew=oldData==null;
          boolean  isSameData=false;
        if(isNew)
        {
            Material material=materialObjectPool.newObject();
            material.id=-1;
            materialService.convert(material,prdt, this);
            materialRepository.save(material);
          //  materialObjectPool.release(material);

        }else
        {






            //单价比较调整
            boolean priceChange=Math.abs(oldData.price-prdt.price)>0.01f;
//            boolean priceChange=Float.compare(oldData.price,prdt.price)!=0;

            boolean memoChange=!StringUtils.compare(oldData.memo,prdt.rem);
            isSameData= materialService.convert(oldData,prdt, this);
            if(priceChange )
            {
                updatePriceRelateData(oldData,productIds);

            }


            /**
             * 备注是否相同，不同则更新到分析表中
             */


          if(memoChange)
            {

                productMaterialRepository.updateMemoOnMaterialId(prdt.rem,oldData.id);
                productPaintRepository.updateMemoOnMaterialId(prdt.rem,oldData.id);

            }



            if(!isSameData) {
                materialRepository.save(oldData);
            }

             }

        if(isNew||!isSameData)
        Logger.getLogger(TAG).info("material :"+prdt.prd_no+",isNew="+isNew+",isSame:"+isSameData);



    }

    /**
     * 同步erp 数据
     * @return
     */
    @RequestMapping(value = "/syncERP",method ={ RequestMethod.GET,RequestMethod.POST})
    @Transactional
    public
    @ResponseBody
    RemoteData<Void> syncERP( )   {

//       EntityManager manager= Persistence.createEntityManagerFactory("erpPersistenceUnit").createEntityManager();
//       ErpPrdtRepository   repository=new ErpPrdtRepository(manager);

        //重置全局参数
        globalData=globalDataService.findCurrentGlobalData();

        EntityManagerHelper helper=EntityManagerHelper.getErp();
        EntityManager manager=helper.getEntityManager();
        ErpPrdtRepository repository=new ErpPrdtRepository(manager);
        List<Prdt>  datas=repository.list();
        helper.closeEntityManager();
        int size=datas.size();
       Logger.getLogger(TAG).info("syncErp total  material size :"+size);

        List<MaterialClass> materialClasses=materialClassRepository.findAll();


        Set<Long> relateProductIds=new HashSet<>();


        for (int i = 0; i < size; i++) {
            Prdt prdt =datas.get(i);
            String classId;
            int length=prdt.prd_no.length();
            if(length<5 ) continue;
            if(prdt.prd_no.startsWith("C")||prdt.prd_no.startsWith("c"))
                classId  =prdt.prd_no.substring(1,5);
                    else
                classId  =prdt.prd_no.substring(0,4);


            boolean  foundClass=false;
            for(MaterialClass materialClass:materialClasses)
            {

                foundClass=false;
                if(materialClass.code.equals(classId))
                {
                    //数据附加
                    prdt.wLong=materialClass.wLong;
                    prdt.wWidth=materialClass.wWidth;
                    prdt.wHeight=materialClass.wHeight;
                    prdt.available=materialClass.available;
                    prdt.discount=materialClass.discount;
                    prdt.classId=materialClass.code;
                    prdt.className=materialClass.name;
                    prdt.type=materialClass.type;

                    foundClass=true;
                    break;
                }
            }
           // if(!foundClass)
               // Logger.getLogger(TAG).info("material :"+prdt.prd_no+" didnot found its class :"+classId);


           // Logger.getLogger(TAG).info("material :"+prdt.prd_no+",flowStep="+i);
            savePrdt(prdt,relateProductIds);

            materialRepository.flush();

            productMaterialRepository.flush();
            productPaintRepository.flush();



        }





        if(relateProductIds.size()>0)
        {

            Logger.getLogger(TAG).info("   relateProduct Count :"+relateProductIds.size());



            updateProductData(relateProductIds);
        }







        return wrapData();
    }





    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<Material> save(@RequestBody   Material  material  )   {


        Material oldData = materialRepository.findOne(material.id);
        if(oldData==null)
        {
            // return wrapError("未找到材料信息  编码："+material.code+"，名称："+material.name);
            //确定编码唯一性

            if(!isDistinctCode(material.code))
            {
                return wrapError("编码：["+material.code+"],已经存在，唯一性字段不能重复");
            }


            material.id=-1;
            //更新缩略图
            updateMaterialPhoto(material);



        }else
        {

            //价格比较
            if(Float.compare(oldData.price,material.price)!=0)
            {


                //重置全局参数
                globalData=globalDataService.findCurrentGlobalData();
                Set<Long> relateProductIds=new HashSet<>();
                    updatePriceRelateData(material,relateProductIds);

                updateProductData(relateProductIds);
            }


            //编号
            if(!oldData.code.equals(material.code))
            {

                if(!isDistinctCode(material.code))
                {
                    return wrapError("编码：["+material.code+"],已经存在，唯一性字段不能重复");
                }

                updateMaterialPhoto(material);
            }







        }



        //更新停用相关信息
         boolean outOfService=material.outOfService;
        if(outOfService&&(oldData==null||!oldData.outOfService))
        {

            Date date=    Calendar.getInstance().getTime();
            material.outOfServiceDate=date.getTime();
            material.outOfServiceDateString= DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(date);
        }





          material=  materialRepository.save(material);

        return wrapData(material);
    }


    /**
     * 检查编码唯一性
     * @param newCode
     * @return
     */
    private  boolean isDistinctCode(String newCode)
    {

      return   materialRepository.findFirstByCodeEquals(newCode)==null;
    }


    /**
     * 更新产品统计数据信息  与材料单价 数量相关
     */
    public void updateProductData(final Collection<Long> productIds)
    {

        //保存到数据库
        //去除重复数据
        if(productIds!=null) {
            ProductToUpdate productToUpdate;
            for (long productId : productIds) {

//                productToUpdate = productToUpdateRepository.findFirstByProductIdEquals(productId);
//                if (productToUpdate == null) {
                    productToUpdate = new ProductToUpdate();
//                }

                productToUpdate.productId = productId;
                productToUpdateRepository.save(productToUpdate);
            }
        }

        long count=productToUpdateRepository.count();
        if(count<=0 ) return;
        Logger.getLogger(TAG).info("product to update for statis data, count:"+count
        );

        //异步启动
        new Thread(new Runnable() {
            @Override
            public void run() {

                materialService.updateProductData();
            }
        }).start();


//        //修正关联产品的统计数据
//        int size = productIds.size();
//        if(size >0)
//        {
//
//            int flowStep=0;
//            for(long productId:productIds) {
//
//                flowStep++;
//                ProductDetail productDetail=   productService.findProductDetailById(productId);
//                if(productDetail!=null) {
//
//                    productDetail.updateProductStatistics(globalData);
//                    productRepository.save(productDetail.product);
//                    Logger.getLogger(TAG).info("productId:"+productId+" has Update! flowStep:"+flowStep
//                    +",size:"+size);
//                }
//
//
//
//            }
//
//        }

    }



    private Set<Long> materialPriceRelateProduct=new HashSet<>();
    /**
     * 更新该材料相关的单价信息  必须在单价有变动的情况下调用。
     * @param material
     * @return 与单价相关的 产品列表
     */
    private void updatePriceRelateData(  Material material,Set<Long > productIds) {
        Logger.getLogger("TEST").info("price of material:"+material.code+" has changed!");
        materialPriceRelateProduct.clear();
        if(globalData==null)
          globalData=globalDataService.findCurrentGlobalData();


        //价格发生变动， 调整有用到该材料的费用
        long id=material.id;




        //调整材料相关的数据


        List<ProductMaterial> datas=   productMaterialRepository.findByMaterialIdEquals(id);
        for (ProductMaterial productMaterial:datas)
        {
            productMaterial.materialCode=material.code;
            productMaterial.materialName=material.name;
            productMaterial.price=FloatHelper.scale(material.price,3);


            productMaterial.amount= FloatHelper.scale(productMaterial.quota *material.price);
            //更新
            productMaterialRepository.save(productMaterial);
            //搜集关联的产品
            materialPriceRelateProduct.add(productMaterial.productId);





        }

        //调整油漆相关的材料数据

        List<ProductPaint>  productPaints=productPaintRepository.findByMaterialIdEquals(id);
        for (ProductPaint productPaint :productPaints)
        {
            productPaint.materialCode=material.code;
            productPaint.materialName=material.name;
            productPaint.materialPrice=material.price;

            productPaint.updatePriceAndCostAndQuantity(globalData);

            //更新
            productPaintRepository.save(productPaint);
            //搜集关联的产品
            materialPriceRelateProduct.add(productPaint.productId);




        }


        Logger.getLogger("TEST").info(" material:"+material.code+",new Price : "+material.price+" has related product count:"+materialPriceRelateProduct.size());

        productIds.addAll(materialPriceRelateProduct);
        materialPriceRelateProduct.clear();



    }

    @RequestMapping(value = "/findListByCodes",method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Material> findListByCodes(@RequestBody  List<String> codes )   {



        List<Material> materials=new ArrayList<>();
        for(String code:codes)
        {


            Material data=materialRepository.findFirstByCodeEquals(code);
            if(null!=data)
                materials.add(data);



        }



        return wrapData(materials);
    }


    @RequestMapping(value = "/findListByNames",method = RequestMethod.POST)
    public
    @ResponseBody
    RemoteData<Material> findListByNames(@RequestBody  List<String> names )   {



        List<Material> materials=new ArrayList<>();
        for(String name:names)
        {
             Material data=materialRepository.findFirstByNameEquals(name);
            if(null!=data)
                materials.add(data);
        }



        return wrapData(materials);
    }





    /**
     * 获取材料类型类别
     * @return
     */

    @RequestMapping(value = "/listClass",method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<MaterialClass> listClass( )   {
        return wrapData(materialClassRepository.findAll());
    }



    /**
     * 获取材料类型类别
     * @return
     */

    @RequestMapping(value = "/listType",method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<MaterialType> listType( )   {
        return wrapData(materialTypeRepository.findAll());
    }


    /**
     * 获取材料计算公式。
     * @return
     */

    @RequestMapping(value = "/listEquation",method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<MaterialEquation> listEquation( )   {

        return wrapData(equationRepository.findAll());
    }



    /**
     *删除产品信息
     *
     *
     * @param materialId
     * @return
     */
    @RequestMapping(value = "/logicDelete", method = {RequestMethod.GET, RequestMethod.POST})
    @Transactional
    public
    @ResponseBody
    RemoteData< Void> logicDelete(@RequestParam("id") long materialId ) {




        //查询是否有产品使用该物料。
        if(productMaterialRepository.findFirstByMaterialIdEquals(materialId)!=null )
        {


            return     wrapError("该材料在产品材料中有使用 ，不能删除 ！");
        }
        //查询是否有产品使用该物料。
        if(productPaintRepository.findFirstByMaterialIdEquals(materialId)!=null )
        {


            return     wrapError("该材料在产品油漆中有使用 ，不能删除 ！");
        }



        Material material=materialRepository.findOne(materialId);
        if(material==null)
        {
            return     wrapError("该材料已经删除， 请更新 ！");
        }

        materialRepository.delete(materialId);

        //TODO   save the delete item to the wareHouse .




        return wrapData();
    }




    /**
     * 同步材料图片数据
     * @return
     */

    @RequestMapping(value="/syncPhoto", method={RequestMethod.GET,RequestMethod.POST})
    @Transactional
    @ResponseBody
    public RemoteData<Void> asyncMaterial( ) {


        int count=0;
        //遍历所有产品
        //一次处理20条记录

        int pageIndex=0;
        int pageSize=20;


        Page<Material> materialPage=null;

        do{
            Pageable pageable = constructPageSpecification(pageIndex++, pageSize);
            materialPage= materialRepository.findAll(pageable);

            for(Material material
                :materialPage.getContent())
                    {


                        String filePath= FileUtils.getMaterialPicturePath(rootFilePath, material.code,material.classId);
                        long lastUpdateTime=FileUtils.getFileLastUpdateTime(new File(filePath));
                        boolean isModified=false;
                        if(lastUpdateTime>0 )
                        {
                    if(lastUpdateTime!=material.lastPhotoUpdateTime )
                    {
                        updateMaterialPhoto(material);
                        material.lastPhotoUpdateTime=lastUpdateTime;
                        isModified=true;
                        count++;
                    }


                }else
                {

                        material.lastPhotoUpdateTime = lastUpdateTime;
                        isModified=true;
                        count++;


                }

                       String newUrl=FileUtils.getMaterialPictureURL(material.code,material.classId,lastUpdateTime);
                        if(!newUrl.equals(material.url))
                        {
                            material.url=newUrl;
                            isModified=true;

                        }


                        //图片改变  或者 url 未赋予初值  则 更新新数据
                if(isModified )
                {

                    materialRepository.save(material);
                }



            }




        }while (materialPage.hasNext());




        return wrapMessageData(count > 0 ? "同步材料数据图片成功，共成功同步" + count + "款材料！" : "所有材料图片已经都是最新的。");
    }




    /**
     * 更新材料的缩略图数据
     *
     * @param material
     */
    private final void updateMaterialPhoto(Material material) {


        String filePath = FileUtils.getMaterialPicturePath(rootFilePath, material.code,material.classId);


        String url;
        //如果tup图片文件不存在  则 设置photo为空。
        if (!new File(filePath).exists()) {
            material.setLastPhotoUpdateTime(Calendar.getInstance().getTimeInMillis());

        } else {




        }

        url=FileUtils.getMaterialPictureURL(material.code,material.classId,material.lastPhotoUpdateTime);
        material.url=url;


    }



    @RequestMapping(value = "/saveClassList",method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<MaterialClass> saveClassList(@RequestBody  List<MaterialClass> materialClasses)   {

        for(MaterialClass materialClass: materialClasses)
        {


            MaterialClass oldData=materialClassRepository.findFirstByCodeEquals(materialClass.code);
            if(oldData==null)
            {
                materialClass.id=-1;


            }else
            {
                materialClass.id=oldData.id;



            }
            materialClassRepository.save(materialClass);



        }



        return listClass();
    }


}
