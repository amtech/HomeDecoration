package com.giants3.hd.server.service;

import com.giants3.hd.server.entity.*;
import com.giants3.hd.server.noEntity.ProductDetail;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.AttachFileUtils;
import com.giants3.hd.server.utils.BackDataHelper;
import com.giants3.hd.server.utils.Constraints;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * quotation 业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class ProductService extends AbstractService implements InitializingBean, DisposableBean {

    @Value("${filepath}")
    private String productFilePath;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private ProductMaterialRepository productMaterialRepository;
    @Autowired
    private ProductWageRepository productWageRepository;
    @Autowired
    private ProductPaintRepository productPaintRepository;

    @Autowired
    private ProductLogRepository productLogRepository;

    @Autowired
    private XiankangRepository xiankangRepository;
    @Autowired
    private OperationLogRepository operationLogRepository;


    @Autowired
    private QuotationItemRepository quotationItemRepository;

    @Autowired
    private QuotationXKItemRepository quotationXKItemRepository;


    @Autowired
    private ProductDeleteRepository productDeleteRepository;



    @Value("${deleteProductFilePath}")
    private String deleteProductPath;
    //附件文件夹
    @Value("${attachfilepath}")
    private String attachfilepath;
    //临时文件夹
    @Value("${tempfilepath}")
    private String tempFilePath;


    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }


    public RemoteData<Product> searchProductList(String name, int pageIndex, int pageSize) {

        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        String likeValue = "%" + name.trim() + "%";
        Page<Product> pageValue = productRepository.findByNameLikeOrPVersionLikeOrderByNameAsc(likeValue, likeValue, pageable);

        List<Product> products = pageValue.getContent();


        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }


    /**
     * 根据产品id 查询产品详情
     *
     * @param productId
     * @return
     */
    public ProductDetail findProductDetailById(long productId) {


        //读取产品信息
        Product product = productRepository.findOne(productId);
        if (product == null) {
            return null;


        }

        ProductDetail detail = new ProductDetail();


        detail.product = product;

        detail.productLog = productLogRepository.findFirstByProductIdEquals(productId);


        //读取材料列表信息
        List<ProductMaterial> productMaterials = productMaterialRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);

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
        detail.packMaterials = packCost;


        //读取工资数据
        List<ProductWage> productWages = productWageRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);
        List<ProductWage> conceptusWage = new ArrayList<>();
        List<ProductWage> assemblesWage = new ArrayList<>();
        List<ProductWage> packWages = new ArrayList<>();
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
        detail.packWages = packWages;

        //读取油漆列表信息
        detail.paints = productPaintRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);


        return detail;

    }

    /**
     * 随机读取
     *
     * @param productNames 货号必须绾完全匹配。
     * @return
     */
    public RemoteData<Product> loadProductByNameRandom(String productNames, boolean withCopy) {

        String[] productNameArray = productNames.split(",|，| ");
        //保证不重复
        List<Product> products = new ArrayList<>();
        for (String s : productNameArray) {
            String trimS = s.trim();
            if (StringUtils.isEmpty(trimS))
                continue;

            if (trimS.contains("_") || trimS.contains("-")) {
                String[] dividerParts = trimS.split("_|-");
                Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(dividerParts[0], dividerParts[1]);
                if (product != null) {
                    products.add(product);
                }

            } else {

                if (withCopy) {
                    List<Product> subProductSet = productRepository.findByNameEquals(trimS);
                    products.addAll(subProductSet);
                } else {

                    Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(trimS, "");
                    if (product != null) {
                        products.add(product);
                    }
                }

            }


        }
        List<Product> result = new ArrayList<>(products.size());
        result.addAll(products);
        return wrapData(result);
    }


    /**
     * 根据产品no找到产品详细信息
     *
     * @param prdNo
     * @return
     */
    public ProductDetail findProductDetailByPrdNo(String prdNo) {


        Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(prdNo, "");
        if (product != null) {
            return findProductDetailById(product.getId());

        }
        return null;

    }

    /**
     * 查找同名产品 并更新缩略图
     *
     * @param productName
     */
    @Transactional
    public void updateProductPhoto(String productName) {


        Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(productName, "");
        if (product == null) {
            return;
        }
        boolean changed = updateProductPhotoData(product);

        if (changed) {
            productRepository.save(product);

            //   更新报价表中的图片url
            quotationItemRepository.updatePhotoAndPhotoUrlByProductId(  product.url, product.id);
            quotationXKItemRepository.updatePhotoByProductId(  product.url, product.id);
            quotationXKItemRepository.updatePhoto2ByProductId(  product.url, product.id);
            productRepository.flush();
            quotationItemRepository.flush();
            quotationXKItemRepository.flush();
        }


    }

    /**
     * 更新产品的缩略图数据
     *
     * @param product
     * @return boolean  是否修改数据
     */
    public final boolean updateProductPhotoData(Product product) {


        String filePath = FileUtils.getProductPicturePath(productFilePath, product.name, product.pVersion);

        //如果tup图片文件不存在  则 设置photo,url
        //  为空。
        final File file = new File(filePath);
        if (!file.exists()) {
            if ((  !StringUtils.isEmpty(product.url))) {

                product.setLastPhotoUpdateTime(Calendar.getInstance().getTimeInMillis());
                product.setUrl("");
                return true;
            }

        } else {
            long lastPhotoUpdateTime = FileUtils.getFileLastUpdateTime(file);
            String newUrl = FileUtils.getProductPictureURL(product.name, product.pVersion, lastPhotoUpdateTime);
            if (lastPhotoUpdateTime != product.lastPhotoUpdateTime || !newUrl.equals(product.url))


                    product.setLastPhotoUpdateTime(lastPhotoUpdateTime);
                    product.setUrl(newUrl);
                    return true;




        }


        return false;


    }


    @Transactional
    public RemoteData<Void> asyncProduct() {
        int count = 0;
        //遍历所有产品
        //一次处理20条记录

        int pageIndex = 0;
        int pageSize = 20;


        Page<Product> productPage = null;

        do {
            Pageable pageable = constructPageSpecification(pageIndex++, pageSize);
            productPage = productRepository.findAll(pageable);

            for (Product product : productPage.getContent()) {


                boolean changedPhoto = updateProductPhotoData(product);


                if (changedPhoto) {
                    count++;
                    productRepository.save(product);


                    //   更新报价表中的图片url
                    //更新报价表中的图片缩略图
                    quotationItemRepository.updatePhotoAndPhotoUrlByProductId(  product.url, product.id);
                    quotationXKItemRepository.updatePhotoByProductId(  product.url, product.id);
                    quotationXKItemRepository.updatePhoto2ByProductId(  product.url, product.id);
                }


            }
            quotationItemRepository.flush();
            quotationXKItemRepository.flush();
            productRepository.flush();


        } while (productPage.hasNext());


        return wrapMessageData(count > 0 ? "同步产品数据图片成功，共成功同步" + count + "款产品！" : "所有产品图片已经都是最新的。");
    }



    /**
     *
     * @param user  登录用户信息
     * @param productId
     * @param newProductName
     * @param version
     * @param copyPicture   是否复制图片
     * @return
     */
    @Transactional
    public RemoteData<ProductDetail> copyProductDetail(User user, long productId, String newProductName, String version, boolean copyPicture) {

        if (productRepository.findFirstByNameEqualsAndPVersionEquals(newProductName, version) != null) {


            return wrapError("货号：" + newProductName + ",版本号：" + version
                    + "已经存在,请更换");
        }

        Product product =productRepository.findOne(productId);

        if (product == null) {
            return wrapError("未找到当前产品信息");
        }

        //深度复制对象， 重新保存数据， 能直接使用源数据保存，会报错。
        Product newProduct = (Product) ObjectUtils.deepCopy(product);
        Xiankang xiankang = (Xiankang) ObjectUtils.deepCopy(product.xiankang);

        if (xiankang != null) {
            xiankang.setId(-1);
            if (xiankang.xiankang_dengju != null) {
                xiankang.xiankang_dengju.setId(-1);
            }
            if (xiankang.xiankang_jiaju != null) {
                xiankang.xiankang_jiaju.setId(-1);
            }
            if (xiankang.xiankang_jingza != null) {
                xiankang.xiankang_jingza.setId(-1);
            }
            newProduct.xiankang = xiankang;
        }
        newProduct.id = -1;
        newProduct.name = newProductName;
        newProduct.pVersion = version;




        if(copyPicture) {
            //复制产品图片

            String newproductPicturePath = FileUtils.getProductPicturePath(productFilePath, newProductName, version);
            File newProductFile = new File(newproductPicturePath);
            //如果文件不存在 则将原样图片复制为新图片
            if (!newProductFile.exists()) {

                String oldProductFilePath = FileUtils.getProductPicturePath(productFilePath, product.name, product.pVersion);

                File oldProductFile = new File(oldProductFilePath);
                if (oldProductFile.exists()) {
                    try {
                        org.apache.commons.io.FileUtils.copyFile(oldProductFile, newProductFile);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }





            }


            //更新缩略图信息
            boolean changPhoto = updateProductPhotoData(newProduct);
        }
//        else
//        {
//            newProduct.photo=null;
//            newProduct.url="";
//            newProduct.lastPhotoUpdateTime=Calendar.getInstance().getTimeInMillis();
//        }



        //保存新产品信息
        newProduct = productRepository.save(newProduct);

        long newProductId = newProduct.id;

        updateProductLog(newProduct, user);


        //更新产品材料信息
        List<ProductMaterial> materials = productMaterialRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);
        //深度复制对象， 重新保存数据， 不能能直接使用源数据保存，会报错。
        List<ProductMaterial> newMaterials = (List<ProductMaterial>) ObjectUtils.deepCopy(materials);
        for (ProductMaterial material : newMaterials) {


            material.id = -1;
            material.productId = newProductId;
            productMaterialRepository.save(material);

        }


        //更新油漆表信息
        //更新产品材料信息
        List<ProductPaint> productPaints = productPaintRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);
        //深度复制对象， 重新保存数据， 不能直接使用源数据保存，会报错。
        List<ProductPaint> newProductPaints = (List<ProductPaint>) ObjectUtils.deepCopy(productPaints);


        for (ProductPaint productPaint : newProductPaints) {
            productPaint.id = -1;
            productPaint.productId = newProductId;
            productPaintRepository.save(productPaint);

        }

        //复制工资

        List<ProductWage> productWages = productWageRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);
        //深度复制对象， 重新保存数据， 能直接使用源数据保存，会报错。
        List<ProductWage> newProductWages = (List<ProductWage>) ObjectUtils.deepCopy(productWages);
        for (ProductWage productWage
                : newProductWages) {
            productWage.id = -1;
            productWage.productId = newProductId;
            productWageRepository.save(productWage);

        }


        return returnFindProductDetailById(newProductId);
    }

    /**
     * 查询产品详情
     * @param productId
     * @return
     */
    RemoteData<ProductDetail> returnFindProductDetailById( long productId) {


        ProductDetail detail = findProductDetailById(productId);

        if (detail == null) {
            return wrapError("未能根据id找到产品");
        }


        return wrapData(detail);


    }
    /**
     * 记录产品修改信息
     */
    public  void updateProductLog(Product product, User user) {

        //记录数据当前修改着相关信息
        ProductLog productLog = productLogRepository.findFirstByProductIdEquals(product.id);
        if (productLog == null) {
            productLog = new ProductLog();
            productLog.productId = product.id;

        }
        productLog.productName = product.name;
        productLog.pVersion = product.pVersion;
        productLog.setUpdator(user);
        productLogRepository.save(productLog);


        //增加历史操作记录
        OperationLog operationLog = OperationLog.createForProductModify(product, user);
        operationLogRepository.save(operationLog);


    }


    /**
     * 保存产品详情
     * @param user
     * @param productDetail
     * @return
     */
    @Transactional
    public RemoteData<ProductDetail> saveProductDetail( User user,ProductDetail productDetail) {


        long productId = productDetail.product.id;


        //新增加的产品数据
        Product newProduct = productDetail.product;


        Product  sameNameAndVersionProduct=productRepository.findFirstByNameEqualsAndPVersionEquals(newProduct.name, newProduct.pVersion);
        //新增加数据
        //检查唯一性 货号版本号形成唯一的索引
        if ( sameNameAndVersionProduct!= null&&sameNameAndVersionProduct.id!=newProduct.id) {
            return wrapError("货号：" + newProduct.name + ",版本号：" + newProduct.pVersion
                    + "已经存在,请更换");
        }

        /**
         * 未生成id 添加记录
         */
        if (!productRepository.exists(productId)) {

            //更新缩略图
            updateProductPhotoData(newProduct);
        } else {
            //找出旧的记录
            Product oldData = productRepository.findOne(productId);


            //检查一致性  最近更新时间是否一致。
            if (oldData.lastUpdateTime != newProduct.lastUpdateTime) {
                return wrapError("货号：" + newProduct.name + ",版本号：" + newProduct.pVersion
                        + " 已经被改变，请刷新数据重新保存。");
            }


            //如果产品名称修改  则修正缩略图
            if (!(oldData.name.equals(newProduct.name) && oldData.pVersion.equals(newProduct.pVersion))) {
                updateProductPhotoData(newProduct);
            }
        }


        //赋于最新的 更新时间
        newProduct.lastUpdateTime = Calendar.getInstance().getTimeInMillis();


       // updateProductPhotoTime(newProduct);


        //检查附件数据是否有改变
        Product oldProduct = productRepository.findOne(productId);
        String oldAttachFiles = oldProduct == null ? "" : oldProduct.attaches;
        if (!newProduct.attaches.equals(oldAttachFiles)) {


            //更新附件文件
            String filePrefix
                    ="Product_"+newProduct.name + "_" + newProduct.pVersion + "_";
            newProduct.attaches= AttachFileUtils.updateProductAttaches(newProduct.attaches,oldAttachFiles,filePrefix,attachfilepath,tempFilePath);

        }
        String oldPackAttaches = oldProduct == null ? "" : oldProduct.packAttaches;
        if (!newProduct.packAttaches.equals(oldPackAttaches)) {


            //更新包装附件文件
            String filePrefix
                    ="Product_"+"Pack_"+newProduct.name + "_" + newProduct.pVersion + "_";
            newProduct.packAttaches= AttachFileUtils.updateProductAttaches(newProduct.packAttaches,oldPackAttaches,filePrefix,attachfilepath,tempFilePath);

        }

        //最新product 数据
        Product product = productRepository.save(newProduct);


        productId = product.id;


        //更新修改记录
        updateProductLog(product, user);


        //将此次修改记录插入历史修改记录表中。


        if (productDetail.paints != null) {
            //保存油漆数据
            List<ProductPaint> oldPaints = productPaintRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);
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
            int newIndex = 0;
            for (ProductPaint newPaint : productDetail.paints) {
                //过滤空白记录 工序编码 跟名称都为空的情况下 也为设定材料情况下  为空记录。
                if (newPaint.isEmpty()) {
                    continue;
                }

                newPaint.setProductId(product.id);
                newPaint.setProductName(product.name);
                newPaint.setFlowId(Flow.FLOW_PAINT);
                newPaint.itemIndex = newIndex++;
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


        int newItemIndex = 0;

        //添加或者修改记录
        for (ProductWage newData
                : wages) {

            if (newData.isEmpty()) {
                continue;
            }

            newData.setProductId(productId);
            newData.setFlowId(flowId);
            newData.itemIndex = newItemIndex++;
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
        int newItemIndex = 0;
        for (ProductMaterial newData
                : materials) {

            //无设定材料  数据为空
            if (newData.isEmpty()) {
                continue;
            }
            newData.setProductId(productId);
            newData.setFlowId(flowId);
            newData.itemIndex = newItemIndex++;
            productMaterialRepository.save(newData);
        }


    }

    @Transactional
   public  RemoteData<ProductDetail> resumeDelete(  User user,  long deleteProductId) {


        ProductDelete productDelete = productDeleteRepository.findOne(deleteProductId);

        if (productDelete == null) {
            return wrapError("该产品已经不在删除列表中");
        }


        ProductDetail detail = BackDataHelper.getProductDetail(deleteProductPath, productDelete);

        if (detail == null)
            return wrapError("备份文件读取失败");


        //新增记录
        //移除id
        detail.product.id = 0;
        detail.product.xiankang.setId(0);

        RemoteData<ProductDetail> result = saveProductDetail(user, detail);

        if (result.isSuccess()) {


            ProductDetail newProductDetail = result.datas.get(0);
            long newProductId = newProductDetail.product.id;
            //更新修改记录中所有旧productId 至新id；

            if (detail.productLog != null) {
                detail.productLog.productId = newProductId;
                productLogRepository.save(detail.productLog);
                newProductDetail.productLog = detail.productLog;
            }

            //更新修改记录中所有旧productId 至新id；
            operationLogRepository.updateProductId(productDelete.productId, Product.class.getSimpleName(), newProductId);

            //添加恢复消息记录。
            OperationLog operationLog = OperationLog.createForProductResume(newProductDetail.product, user);
            operationLogRepository.save(operationLog);


            //移除删除记录
            productDeleteRepository.delete(deleteProductId);

            //移除备份的文件
            BackDataHelper.deleteProduct(deleteProductPath, productDelete);

        }


        return wrapData(detail);

    }


    /**
     * 删除产品信息
     *
     * @param productId
     * @return
     */
    @Transactional
    public
    @ResponseBody
    RemoteData<Void> logicDelete( User user, long productId) {


        ProductDetail detail = findProductDetailById(productId);


        //查询是否有关联的报价单

        QuotationItem quotationItem = quotationItemRepository.findFirstByProductIdEquals(productId);
        if (quotationItem != null) {
            Quotation quotation = quotationRepository.findOne(quotationItem.quotationId);

            return wrapError("该货号在报价单：[" + (quotation == null ? "" : quotation.qNumber)
                    + "]有使用记录，不能删除");
        }

        Product product = productRepository.findOne(productId);
        if (product == null) {
            return wrapError("该产品已经删除， 请更新 ！");
        }

        productRepository.delete(productId);
        //增加历史操作记录
        operationLogRepository.save(OperationLog.createForProductDelete(product, user));


        int affectedRow = 0;
        affectedRow = productWageRepository.deleteByProductIdEquals(productId);
        // logger.info("productWageRepository delete affectedRow:" + affectedRow);
        affectedRow = productMaterialRepository.deleteByProductIdEquals(productId);
        //  logger.info("productMaterialRepository delete affectedRow:" + affectedRow);
        affectedRow = productPaintRepository.deleteByProductIdEquals(productId);
        //  logger.info("productPaintRepository delete affectedRow:" + affectedRow);

        //TODO   save the delete item to the wareHouse .


        //备份产品数据

        ProductDelete productDelete = new ProductDelete();
        productDelete.setProductAndUser(product, user);
        ProductDelete newDelete = productDeleteRepository.save(productDelete);
        BackDataHelper.backProduct(detail, deleteProductPath, newDelete);


        return wrapData();
    }








    public
    @ResponseBody
    RemoteData<ProductDetail> detailDelete(  long productDeleteId) {


        ProductDelete productDelete = productDeleteRepository.findOne(productDeleteId);

        if (productDelete == null) {
            return wrapError("该产品已经不在删除列表中");
        }


        ProductDetail detail = null;

        detail = BackDataHelper.getProductDetail(deleteProductPath, productDelete);

        if (detail == null)
            return wrapError("备份文件读取失败");


        return wrapData(detail);

    }




    public
    @ResponseBody
    RemoteData<ProductDelete> listDelete( String prd_name,  int pageIndex,   int pageCount) {


        Pageable pageable = constructPageSpecification(pageIndex, pageCount);
        Page<ProductDelete> pageValue = productDeleteRepository.findByProductNameLikeOrderByTimeDesc("%" + prd_name.trim() + "%", pageable);

        List<ProductDelete> products = pageValue.getContent();


        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);


    }
}
