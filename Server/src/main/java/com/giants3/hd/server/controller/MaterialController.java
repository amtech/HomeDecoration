package com.giants3.hd.server.controller;


import com.giants3.hd.server.interceptor.EntityManagerHelper;
import com.giants3.hd.server.repository.*;

import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;

import com.giants3.hd.utils.entity_erp.Prdt;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import com.giants3.hd.utils.noEntity.ProductDetail;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
* 材料信息控制类。
*/
@Controller
@RequestMapping("/material")
public class MaterialController extends BaseController {

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


    @Autowired
    private ProductController productController;

 @Autowired
    private GlobalDataRepository globalDataRepository;


    ObjectPool<Material> materialObjectPool;

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
     */
    public void savePrdt(Prdt prdt)
    {






        Material oldData=materialRepository.findFirstByCodeEquals(prdt.prd_no);
        if(oldData==null)
        {
            Material material=materialObjectPool.newObject();
            material.id=-1;
            convert(material,prdt);
            materialRepository.save(material);
          materialObjectPool.release(material);

        }else
        {



            if(Float.compare(oldData.price,prdt.price)!=0)
            {
                oldData.price=prdt.price;
                updatePriceRelateData(oldData);
            }
            convert(oldData,prdt);
            materialRepository.save(oldData);


        }




    }

    /**
     * 同步erp 数据
     * @return
     */
    @RequestMapping(value = "/syncERP",method ={ RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Void> syncERP( )   {

//       EntityManager manager= Persistence.createEntityManagerFactory("erpPersistenceUnit").createEntityManager();
//       ErpPrdtRepository   repository=new ErpPrdtRepository(manager);

        EntityManagerHelper helper=EntityManagerHelper.getErp();
        EntityManager manager=helper.getEntityManager();
        ErpPrdtRepository repository=new ErpPrdtRepository(manager);
        List<Prdt>  datas=repository.list();
        helper.closeEntityManager();
        int size=datas.size();
      //  Logger.getLogger("TEST").info("syncErp total  material size :"+size);

        List<MaterialClass> materialClasses=materialClassRepository.findAll();



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
            if(!foundClass)
                Logger.getLogger("TEST").info("material :"+prdt.prd_no+" didnot found its class :"+classId);

            savePrdt(prdt);
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

                updatePriceRelateData(material);

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
     * 更新该材料相关的单价信息  必须在单价有变动的情况下调用。
     * @param material
     */
    private void updatePriceRelateData(  Material material) {
        Logger.getLogger("TEST").info("price of material:"+material.code+" has changed!");
        GlobalData globalData=globalDataRepository.findAll().get(0);


        //价格发生变动， 调整有用到该材料的费用
        long id=material.id;




        //调整材料相关的数据
        List<Long> productIds=new ArrayList<>();

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
            if(productIds.indexOf(productMaterial.productId)==-1)
            {
                productIds.add(productMaterial.productId);
            }



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
            if(productIds.indexOf(productPaint.productId)==-1)
            {
                productIds.add(productPaint.productId);
            }



        }



        //修正关联产品的统计数据
        int size = productIds.size();
        if(size >0)
        {

            for(long productId:productIds) {


             ProductDetail productDetail=   productController.findProductDetailById(productId);
                if(productDetail!=null) {
                    productDetail.updateProductStatistics(globalData);
                    productRepository.save(productDetail.product);
                }

            }

        }
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
    public RemoteData<Void> asyncProduct( ) {


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
                if(lastUpdateTime>0 )
                {
                    if(lastUpdateTime!=material.lastPhotoUpdateTime||material.photo==null)
                    {
                        updateMaterialPhoto(material);
                        material.lastPhotoUpdateTime=lastUpdateTime;

                        materialRepository.save(material);
                        count++;
                    }


                }else
                {
                    if(material.photo!=null) {
                        material.photo = null;
                        material.lastPhotoUpdateTime = lastUpdateTime;
                        materialRepository.save(material);
                        count++;
                    }

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

        //如果tup图片文件不存在  则 设置photo为空。
        if (!new File(filePath).exists()) {
            material.setPhoto(null);
            material.setLastPhotoUpdateTime(Calendar.getInstance().getTimeInMillis());

        } else {
            try {
                material.setPhoto(ImageUtils.scaleMaterial(filePath));

            } catch (HdException e) {
                e.printStackTrace();
            }


        }


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


    /**
     * erp 数据转换成材料数据
     * @param material
     * @param prdt
     */
    private  void   convert(Material material,Prdt prdt)
    {

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



    }
}
