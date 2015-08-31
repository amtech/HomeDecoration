package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.BackDataHelper;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.RemoteData;

import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * 产品信息
 */
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {


    @Autowired
    private ProductRepository productRepository;



    @Autowired
    private ProductMaterialRepository productMaterialRepository;
    @Autowired
    private ProductWageRepository productWageRepository;
    @Autowired
    private ProductPaintRepository productPaintRepository;

    @Autowired
    private ProductLogRepository productLogRepository;


    @Autowired
    private OperationLogRepository operationLogRepository;
    @Value("${filepath}")
    private String productFilePath;


    @Value("${deleteProductFilePath}")
    private String deleteProductPath;

    @Autowired
    private QuotationItemRepository quotationItemRepository;

    @Autowired
    private QuotationXKItemRepository quotationXKItemRepository;

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private ProductDeleteRepository productDeleteRepository;
    //@PersistenceContext
    //private EntityManager entityManager;





    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Product> search(@RequestParam(value = "proName", required = false, defaultValue = "") String prd_name
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize

    ) throws UnsupportedEncodingException {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<Product> pageValue = productRepository.findByNameLikeOrderByNameAsc("%" + prd_name.trim() + "%", pageable);

        List<Product> products = pageValue.getContent();


        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }




    @RequestMapping(value = "/searchByName", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Product> searchByName(@RequestParam(value = "proName" ) String prd_name
            , @RequestParam(value = "productId") int productId

    ) throws UnsupportedEncodingException {



        List<Product> pageValue = productRepository.findByNameEquals(prd_name);

        int size = pageValue.size();
        for (int i = 0; i < size; i++) {
            Product product=pageValue.get(i);
            if(product.id==productId)
            {
                pageValue.remove(i);
                break;
            }
        }





        return wrapData( pageValue);


    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public
    @ResponseBody
    Product findProdcutById(@RequestParam("id") long productId) {

        Product product = productRepository.findOne(productId);
        return product;
    }




    /**
     * 查询产品的详细信息
     * 包括 包装信息
     * 物料清单（胚体，油漆，包装）
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/detail", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<ProductDetail> returnFindProductDetailById(@RequestParam("id") long productId) {



        ProductDetail detail=findProductDetailById(productId);

        if(detail==null)
        {
            return  wrapError("未能根据id找到产品");
        }


        return wrapData(detail);


    }


    /**
     * 根据产品id 查询产品详情
     * @param productId
     * @return
     */
    public   ProductDetail  findProductDetailById(  long productId) {



        //读取产品信息
        Product product = productRepository.findOne(productId);
        if (product == null) {
            return null;


        }

        ProductDetail detail = new ProductDetail();


        detail.product = product;

        detail.productLog=productLogRepository.findFirstByProductIdEquals(productId);




        //读取材料列表信息
        List<ProductMaterial> productMaterials = productMaterialRepository.findByProductIdEquals(productId);

        List<ProductMaterial> conceptusCost = new ArrayList<>();
        List<ProductMaterial> assemblesCost = new ArrayList<>();
        List<ProductMaterial> packCost = new ArrayList<>();


        for (ProductMaterial productMaterial : productMaterials) {

            switch ((int) productMaterial.flowId) {
                case Flow.FLOW_CONCEPTUS:
                    conceptusCost.add(productMaterial);
                    break;

                case Flow.FLOW_PAINT:

                    break;

                case Flow.FLOW_ASSEMBLE:
                    assemblesCost.add(productMaterial);
                    break;

                case Flow.FLOW_PACK:
                    packCost.add(productMaterial);
                    break;
            }

        }
        detail.conceptusMaterials = conceptusCost;
        detail.assembleMaterials = assemblesCost;
        detail.packMaterials=packCost;




        //读取工资数据
        List<ProductWage> productWages = productWageRepository.findByProductIdEquals(productId);
        List<ProductWage> conceptusWage = new ArrayList<>();
        List<ProductWage> assemblesWage = new ArrayList<>();
        List<ProductWage> packWages=new ArrayList<>();
        for (ProductWage productWage : productWages) {

            switch ((int) productWage.flowId) {
                case Flow.FLOW_CONCEPTUS:
                    conceptusWage.add(productWage);
                    break;

                case Flow.FLOW_PAINT:

                    break;

                case Flow.FLOW_ASSEMBLE:
                    assemblesWage.add(productWage);
                    break;

                case Flow.FLOW_PACK:
                    packWages.add(productWage);
                    break;
            }

        }


        detail.conceptusWages = conceptusWage;
        detail.assembleWages = assemblesWage;
        detail.packWages=packWages;

        //读取油漆列表信息
        detail.paints = productPaintRepository.findByProductIdEquals(productId);


        return detail;

    }


    /**
     * 产品完整信息的保存
     *
     * @param productDetail 产品全部信息
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @Transactional
    public
    @ResponseBody
    RemoteData<ProductDetail> saveProductDetail(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,@RequestBody ProductDetail productDetail) {


        long productId = productDetail.product.id;

        //新增加的产品数据
        Product newProduct = productDetail.product;

        /**
         * 未生成id 添加记录
         */
        if (!productRepository.exists(productId)) {


            //检查唯一性
            if(productRepository.findFirstByNameEqualsAndPVersionEquals(newProduct.name,newProduct.pVersion)!=null)
            {
                 return   wrapError("货号："+newProduct.name +",版本号："+newProduct.pVersion
                            +"已经存在,请更换");
            }



            //更新缩略图
            updateProductPhotoData(newProduct);
        } else {
            //找出旧的记录
            Product oldData = productRepository.findOne(productId);
            //如果产品名称修改  则修正缩略图
            if (!(oldData.name.equals(newProduct.name)&&oldData.pVersion.equals(newProduct.pVersion))) {
                updateProductPhotoData(newProduct);
            }
        }


        updateProductPhotoTime(newProduct);






        //最新product 数据
        Product product = productRepository.save(newProduct);


        productId = product.id;


        //更新修改记录
        updateProductLog(product, user);





        //将此次修改记录插入历史修改记录表中。




        if (productDetail.paints != null) {
            //保存油漆数据
             List<ProductPaint> oldPaints = productPaintRepository.findByProductIdEquals(productId);
            //查找旧记录是否存在新纪录中  如果不存在就删除。删除旧记录操作。
            for (ProductPaint oldPaint : oldPaints) {
                boolean exist = false;
                for (ProductPaint newPaint : productDetail.paints) {

                    if (oldPaint.getId() == newPaint.id) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    //不存在新纪录中 删除
                    productPaintRepository.delete(oldPaint.id);
                }

            }

            //添加或者修改记录
            for (ProductPaint newPaint : productDetail.paints) {
                //过滤空白记录 工序编码 跟名称都为空的情况下 也为设定材料情况下  为空记录。
                if(newPaint.isEmpty())
                {
                    continue;
                }

                newPaint.setProductId(product.id);
                newPaint.setProductName(product.name);
                newPaint.setFlowId(Flow.FLOW_PAINT);
                productPaintRepository.save(newPaint);
            }


        }

        //保存白胚数据
        if (productDetail.conceptusMaterials != null) {

            saveProductMaterial(productDetail.conceptusMaterials, productId, Flow.FLOW_CONCEPTUS);
        }

        //保存组装数据
        if (productDetail.assembleMaterials != null) {

            saveProductMaterial(productDetail.assembleMaterials, productId, Flow.FLOW_ASSEMBLE);
        }

        //保存包装数据
        if (productDetail.packMaterials != null) {

            saveProductMaterial(productDetail.packMaterials, productId, Flow.FLOW_PACK);
        }



        if (productDetail.assembleWages != null)
            saveProductWage(productDetail.assembleWages, productId, Flow.FLOW_ASSEMBLE);

        if (productDetail.conceptusWages != null)
            saveProductWage(productDetail.conceptusWages, productId, Flow.FLOW_CONCEPTUS);

        if (productDetail.packWages != null)
            saveProductWage(productDetail.packWages, productId, Flow.FLOW_PACK);





        return returnFindProductDetailById(productId);
    }


    /**
     * 更新产品的工资信息
     *
     * @param wages
     * @param flowId
     */
    private void saveProductWage(List<ProductWage> wages, long productId, long flowId) {

        //保存油漆数据
        List<ProductWage> oldDatas = productWageRepository.findByProductIdEqualsAndFlowIdEquals(productId, flowId);
        //查找就记录是否存在新纪录中  如果不存在就删除。删除旧记录操作。
        for (ProductWage oldData : oldDatas) {
            boolean exist = false;
            for (ProductWage newData : wages) {

                if (oldData.getId() == newData.id) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                //不存在新纪录中 删除
                productWageRepository.delete(oldData.id);
            }

        }

        //添加或者修改记录
        for (ProductWage newData
                : wages) {

            if(newData.isEmpty())
            {
                continue;
            }

            newData.setProductId(productId);
            newData.setFlowId(flowId);
            productWageRepository.save(newData);
        }


    }


    /**
     * 更新产品的材料数据， 不同流程 不同处理。
     *
     * @param materials
     * @param productId
     * @param flowId
     */
    private void saveProductMaterial(List<ProductMaterial> materials, long productId, long flowId) {

        //保存油漆数据
        List<ProductMaterial> oldDatas = productMaterialRepository.findByProductIdEqualsAndFlowIdEquals(productId, flowId);
        //查找就记录是否存在新纪录中  如果不存在就删除。删除旧记录操作。
        for (ProductMaterial oldData : oldDatas) {
            boolean exist = false;
            for (ProductMaterial newData : materials) {

                if (oldData.getId() == newData.id ) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                //不存在新纪录中 删除
                productMaterialRepository.delete(oldData.id);
            }

        }

        //添加或者修改记录
        for (ProductMaterial newData
                : materials) {

            //无设定材料  数据为空
            if(newData.isEmpty() )
            {continue;}
            newData.setProductId(productId);
            newData.setFlowId(flowId);
            productMaterialRepository.save(newData);
        }


    }


    /**
     * Returns a Sort object which sorts persons in ascending order by using the last name.
     *
     * @return
     */
    protected Sort sortByLastNameAsc() {
        return new Sort(Sort.Direction.ASC, "name");
    }


    /**
     * 更新产品的缩略图数据
     *
     * @param product
     */
    private final void updateProductPhotoData(Product product) {


        String filePath = FileUtils.getProductPicturePath(productFilePath, product.name, product.pVersion);

        //如果tup图片文件不存在  则 设置photo为空。
        if (!new File(filePath).exists()) {
            product.setPhoto(null);
            product.setLastPhotoUpdateTime(Calendar.getInstance().getTimeInMillis());

        } else {
            try {
                product.setPhoto(ImageUtils.scaleProduct(filePath));

            } catch (HdException e) {
                e.printStackTrace();
            }


        }


    }

    /**
     * 更新图片的最后一次修改的时间
     */
    private final void updateProductPhotoTime(Product product) {


        String filePath = FileUtils.getProductPicturePath(productFilePath, product.name, product.pVersion);

        long newLastUpdateTime=FileUtils.getFileLastUpdateTime(new File(filePath));
        product.setLastPhotoUpdateTime(newLastUpdateTime);


    }


    /**
     *复制产品信息   即翻单
     * 检查是否存在同款记录  （product +version）
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/copy", method = {RequestMethod.GET, RequestMethod.POST})
    @Transactional
    public

    @ResponseBody
    RemoteData<ProductDetail> copyProduct( @ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,  @RequestParam("id") long productId,@RequestParam("name") String newProductName,@RequestParam("version") String version) {

        if(productRepository.findFirstByNameEqualsAndPVersionEquals(newProductName, version)!=null)
        {


           return     wrapError("货号："+newProductName +",版本号："+version
                +"已经存在,请更换");
        }

        Product product=findProdcutById(productId);

        if(   product==null  )
        {
            return wrapError("未找到当前产品信息");
        }

        //深度复制对象， 重新保存数据， 能直接使用源数据保存，会报错。
        Product newProduct= (Product)  ObjectUtils.deepCopy(product);
        Xiankang xiankang=(Xiankang)ObjectUtils.deepCopy(product.xiankang);
        xiankang.setId(-1);
        newProduct.xiankang=xiankang;
        newProduct.id= -1;
        newProduct.name=newProductName;
        newProduct.pVersion=version;

        //复制产品图片

        String newproductPicturePath=FileUtils.getProductPicturePath(productFilePath,newProductName,version);
        File newProductFile=new File(newproductPicturePath);
        //如果文件不存在 则将原样图片复制为新图片
        if(!newProductFile.exists())
        {

            String oldProductFilePath=FileUtils.getProductPicturePath(productFilePath,product.name,product.pVersion);

            File oldProductFile=new File(oldProductFilePath);
            if(oldProductFile.exists())
            {
                try {
                    org.apache.commons.io.FileUtils.copyFile(oldProductFile,newProductFile);
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }


        }



        updateProductPhotoData(newProduct);
        updateProductPhotoTime(newProduct);


        //保存新产品信息
         newProduct= productRepository.save(newProduct);

        long newProductId=newProduct.id;

        updateProductLog(newProduct,user);



        //更新产品材料信息
    List<ProductMaterial> materials=  productMaterialRepository.findByProductIdEquals(productId);
        //深度复制对象， 重新保存数据， 不能能直接使用源数据保存，会报错。
        List<ProductMaterial> newMaterials= (List<ProductMaterial>) ObjectUtils.deepCopy(materials);
        for(ProductMaterial material:newMaterials)
        {


            material.id=-1;
            material.productId=newProductId;
            productMaterialRepository.save(material);

        }


        //更新油漆表信息
        //更新产品材料信息
        List<ProductPaint> productPaints=   productPaintRepository.findByProductIdEquals(productId);
        //深度复制对象， 重新保存数据， 能直接使用源数据保存，会报错。
        List<ProductPaint> newProductPaints = (List<ProductPaint>) ObjectUtils.deepCopy(productPaints);


        for(ProductPaint productPaint:newProductPaints)
        {
            productPaint.id=-1;
            productPaint.productId=newProductId;
            productPaintRepository.save(productPaint);

        }

        //复制工资

        List<ProductWage> productWages=   productWageRepository.findByProductIdEquals(productId);
        //深度复制对象， 重新保存数据， 能直接使用源数据保存，会报错。
        List<ProductWage> newProductWages= (List<ProductWage>)  ObjectUtils.deepCopy(productWages);
        for(ProductWage productWage
                :newProductWages)
        {
            productWage.id=-1;
            productWage.productId=newProductId;
            productWageRepository.save(productWage);

        }




        //chap



        return returnFindProductDetailById(newProductId);
    }


    /**
     * 记录产品修改信息
     */
    private void updateProductLog(Product product,User user)
    {

        //记录数据当前修改着相关信息
        ProductLog productLog=productLogRepository.findFirstByProductIdEquals(product.id);
        if(productLog==null)
        {
            productLog=new ProductLog();
            productLog.productId=product.id;

        }
        productLog.productName=product.name;
        productLog.pVersion=product.pVersion;
        productLog.setUpdator(user);
        productLogRepository.save(productLog);




        //增加历史操作记录
        OperationLog   operationLog= OperationLog.createForProductModify(product,user);
          operationLogRepository.save(operationLog);







    }




    /**
     *删除产品信息
     *
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/logicDelete", method = {RequestMethod.GET, RequestMethod.POST})
    @Transactional
    public
    @ResponseBody
    RemoteData< Void> logicDelete(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,@RequestParam("id") long productId ) {




        ProductDetail detail=findProductDetailById(productId);


        //查询是否有关联的报价单

        QuotationItem quotationItem=quotationItemRepository.findFirstByProductIdEquals(productId);
        if(quotationItem!=null)
        {
            Quotation quotation=    quotationRepository.findOne(quotationItem.quotationId);

            return     wrapError("该货号在报价单：["+(quotation==null?"":quotation.qNumber)
                    +"]有使用记录，不能删除");
        }

        Product product=productRepository.findOne(productId);
        if(product==null)
        {
            return     wrapError("该产品已经删除， 请更新 ！");
        }

        productRepository.delete(productId);
        //增加历史操作记录
        operationLogRepository.save(OperationLog.createForProductDelete(product, user));


        int affectedRow=0;
        affectedRow=productWageRepository.deleteByProductIdEquals(productId);
        Logger.getLogger("TEST").info("productWageRepository delete affectedRow:"+affectedRow);
        affectedRow= productMaterialRepository.deleteByProductIdEquals(productId);
        Logger.getLogger("TEST").info("productMaterialRepository delete affectedRow:" + affectedRow);
        affectedRow=  productPaintRepository.deleteByProductIdEquals(productId);
        Logger.getLogger("TEST").info("productPaintRepository delete affectedRow:" + affectedRow);

         //TODO   save the delete item to the wareHouse .






        //备份产品数据

            ProductDelete productDelete = new ProductDelete();
            productDelete.setProductAndUser(product, user);
            ProductDelete newDelete = productDeleteRepository.save(productDelete);
            BackDataHelper.backProduct(detail, deleteProductPath, newDelete);




        return wrapData();
    }



    /**
     * 同步产品图片数据
     * @return
     */

    @RequestMapping(value="/syncPhoto", method={RequestMethod.GET,RequestMethod.POST})

    @Transactional
    @ResponseBody
    public RemoteData<Void> asyncProduct( ) {

//
//        try {
//            Thread.sleep(300000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        int count=0;
        //遍历所有产品
        //一次处理20条记录

         int pageIndex=0;
        int pageSize=20;


        Page<Product> productPage=null;

        do{
            Pageable pageable = constructPageSpecification(pageIndex++, pageSize);
              productPage= productRepository.findAll(pageable);

                for(Product product:productPage.getContent())
                {


                    String filePath=FileUtils.getProductPicturePath(productFilePath,product.name,product.pVersion);
                    long lastUpdateTime=FileUtils.getFileLastUpdateTime(new File(filePath));
                    boolean changedPhoto=false;
                    if(lastUpdateTime>0 )
                    {
                        if(product.photo==null||lastUpdateTime!=product.lastPhotoUpdateTime)
                        {
                            updateProductPhotoData(product);
                            product.lastPhotoUpdateTime=lastUpdateTime;

                            productRepository.save(product);
                            changedPhoto=true;
                            count++;
                        }


                    }else//图片不存在 设置为空。
                    {
                        if(product.photo!=null||product.lastPhotoUpdateTime>0) {
                            product.photo = null;
                            product.lastPhotoUpdateTime = lastUpdateTime;
                            productRepository.save(product);
                            changedPhoto=true;
                            count++;
                        }

                    }

                    if(changedPhoto)
                    {
                        //更新报价表中的图片缩略图
                        quotationItemRepository.updatePhotoByProductId(product.photo, product.id);
                        quotationXKItemRepository.updatePhotoByProductId(product.photo, product.id);
                        quotationXKItemRepository.updatePhoto2ByProductId(product.photo, product.id);
                    }



                }




        }while (productPage.hasNext());




        return wrapMessageData(count > 0 ? "同步产品数据图片成功，共成功同步" + count + "款产品！" : "所有产品图片已经都是最新的。");
    }






    @RequestMapping(value = "/searchDelete", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ProductDelete> listDelete(@RequestParam(value = "proName", required = false, defaultValue = "") String prd_name,@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageCount) {



        Pageable pageable = constructPageSpecification(pageIndex, pageCount);
        Page<ProductDelete> pageValue = productDeleteRepository.findByProductNameLikeOrderByTimeDesc("%" + prd_name.trim() + "%", pageable);

        List<ProductDelete> products = pageValue.getContent();


        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }


    @RequestMapping(value = "/detailDelete", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<ProductDetail> detailDelete(@RequestParam(value = "id" )  long productDeleteId ) {



        ProductDelete productDelete=productDeleteRepository.findOne(productDeleteId);

        if(productDelete==null)
        {
            return wrapError("该产品已经不在删除列表中");
        }


        ProductDetail detail  =null;

        detail= BackDataHelper.getProductDetail(deleteProductPath, productDelete);

        if(detail==null)
            return wrapError("备份文件读取失败");



        return wrapData(detail);

    }

    @RequestMapping(value = "/resumeDelete", method = RequestMethod.GET)
    @Transactional
    public
    @ResponseBody
    RemoteData<ProductDetail> resumeDelete(@ModelAttribute(Constraints.ATTR_LOGIN_USER) User user,@RequestParam(value = "deleteProductId" )  long deleteProductId ) {



        ProductDelete productDelete=productDeleteRepository.findOne(deleteProductId);

        if(productDelete==null)
        {
            return wrapError("该产品已经不在删除列表中");
        }


        ProductDetail detail  =BackDataHelper.getProductDetail(deleteProductPath,productDelete);

        if(detail==null)
            return wrapError("备份文件读取失败");


        //新增记录
        //移除id
        detail.product.id=0;
        detail.product.xiankang.setId(0);

     RemoteData<ProductDetail> result=   saveProductDetail(user,detail);

        if(result.isSuccess())
        {


            ProductDetail newProductDetail=result.datas.get(0);
            long newProductId=newProductDetail.product.id;
            //更新修改记录中所有旧productId 至新id；

            if(detail.productLog!=null) {
                detail.productLog.productId = newProductId;
                productLogRepository.save(detail.productLog);
                newProductDetail.productLog = detail.productLog;
            }

            //更新修改记录中所有旧productId 至新id；
            operationLogRepository.updateProductId(productDelete.productId, Product.class.getSimpleName(), newProductId);

            //添加恢复消息记录。
            OperationLog operationLog=  OperationLog.createForProductResume(newProductDetail.product, user);
            operationLogRepository.save(operationLog);


            //移除删除记录
            productDeleteRepository.delete(deleteProductId);

            //移除备份的文件
            BackDataHelper.deleteProduct(deleteProductPath,productDelete);

        }









        return wrapData(detail);

    }


}
