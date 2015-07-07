package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.RemoteData;

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
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
    @Value("${filepath}")
    private String rootFilePath;

    @Autowired
    private QuotationItemRepository quotationItemRepository;

    @Autowired
    private QuotationRepository quotationRepository;
    //@PersistenceContext
    //private EntityManager entityManager;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Product> listPrdtJson() {


        return wrapData(productRepository.findAll());
    }


    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Product> search(@RequestParam(value = "proName", required = false, defaultValue = "") String prd_name
            , @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize

    ) throws UnsupportedEncodingException {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<Product> pageValue = productRepository.findByNameLikeOrderByNameAsc("%" + prd_name + "%", pageable);

        List<Product> products = pageValue.getContent();




        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


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
    RemoteData<ProductDetail> saveProductDetail(@RequestBody ProductDetail productDetail) {


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

                if (oldData.getId() == newData.id) {
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


        String filePath = FileUtils.getProductPicturePath(rootFilePath, product.name, product.pVersion);

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


        String filePath = FileUtils.getProductPicturePath(rootFilePath, product.name, product.pVersion);

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
    RemoteData<ProductDetail> copyProduct(@RequestParam("id") long productId,@RequestParam("name") String newProductName,@RequestParam("version") String version) {

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


        newProduct.id= -1;
        newProduct.name=newProductName;
        newProduct.pVersion=version;

        updateProductPhotoData(newProduct);
        updateProductPhotoTime(newProduct);


        //保存新产品信息
         newProduct= productRepository.save(newProduct);

        long newProductId=newProduct.id;
        //更新产品材料信息
    List<ProductMaterial> materials=  productMaterialRepository.findByProductIdEquals(productId);
        //深度复制对象， 重新保存数据， 能直接使用源数据保存，会报错。
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

        return returnFindProductDetailById(newProductId);
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
    RemoteData< Void> logicDelete(@RequestParam("id") long productId ) {




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

        int affectedRow=0;
        affectedRow=productWageRepository.deleteByProductIdEquals(productId);
        Logger.getLogger("TEST").info("productWageRepository delete affectedRow:"+affectedRow);
        affectedRow= productMaterialRepository.deleteByProductIdEquals(productId);
        Logger.getLogger("TEST").info("productMaterialRepository delete affectedRow:" + affectedRow);
        affectedRow=  productPaintRepository.deleteByProductIdEquals(productId);
        Logger.getLogger("TEST").info("productPaintRepository delete affectedRow:" + affectedRow);

         //TODO   save the delete item to the wareHouse .




        return wrapData( );
    }



    /**
     * 同步产品图片数据
     * @return
     */

    @RequestMapping(value="/syncPhoto", method={RequestMethod.GET,RequestMethod.POST})

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


                    String filePath=FileUtils.getProductPicturePath(rootFilePath,product.name,product.pVersion);
                    long lastUpdateTime=FileUtils.getFileLastUpdateTime(new File(filePath));
                    if(lastUpdateTime>0 )
                    {
                        if(lastUpdateTime!=product.lastPhotoUpdateTime)
                        {
                            updateProductPhotoData(product);
                            product.lastPhotoUpdateTime=lastUpdateTime;

                            productRepository.save(product);
                            count++;
                        }


                    }else
                    {
                        if(product.photo!=null) {
                            product.photo = null;
                            product.lastPhotoUpdateTime = lastUpdateTime;
                            productRepository.save(product);
                            count++;
                        }

                    }



                }




        }while (productPage.hasNext());




        return wrapMessageData(count>0?"同步产品数据图片成功，共成功同步"+count+"款产品！":"所有产品图片已经都是最新的。");
    }




}
