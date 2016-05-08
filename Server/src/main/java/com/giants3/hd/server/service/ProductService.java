package com.giants3.hd.server.service;

import com.giants3.hd.server.entity.Flow;
import com.giants3.hd.server.entity.Product;
import com.giants3.hd.server.entity.ProductMaterial;
import com.giants3.hd.server.entity.ProductWage;
import com.giants3.hd.server.noEntity.ProductDetail;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.utils.FileUtils;
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

import java.io.File;
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
        Set<Product> products = new HashSet<>();
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
            quotationItemRepository.updatePhotoAndPhotoUrlByProductId(product.photo, product.url, product.id);
            quotationXKItemRepository.updatePhotoByProductId(product.photo, product.url, product.id);
            quotationXKItemRepository.updatePhoto2ByProductId(product.photo, product.url, product.id);
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
            if ((product.photo != null || !StringUtils.isEmpty(product.url))) {
                product.setPhoto(null);
                product.setLastPhotoUpdateTime(Calendar.getInstance().getTimeInMillis());
                product.setUrl("");
                return true;
            }

        } else {
            long lastPhotoUpdateTime = FileUtils.getFileLastUpdateTime(file);
            String newUrl = FileUtils.getProductPictureURL(product.name, product.pVersion, lastPhotoUpdateTime);
            if (lastPhotoUpdateTime != product.lastPhotoUpdateTime || !newUrl.equals(product.url))
                try {
                    product.setPhoto(ImageUtils.scaleProduct(filePath));
                    product.setLastPhotoUpdateTime(lastPhotoUpdateTime);
                    product.setUrl(newUrl);
                    return true;

                } catch (HdException e) {
                    e.printStackTrace();
                    Logger.getLogger("ProductService").info(e.getMessage());

                }


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
                    quotationItemRepository.updatePhotoAndPhotoUrlByProductId(product.photo, product.url, product.id);
                    quotationXKItemRepository.updatePhotoByProductId(product.photo, product.url, product.id);
                    quotationXKItemRepository.updatePhoto2ByProductId(product.photo, product.url, product.id);
                }


            }
            quotationItemRepository.flush();
            quotationXKItemRepository.flush();
            productRepository.flush();


        } while (productPage.hasNext());


        return wrapMessageData(count > 0 ? "同步产品数据图片成功，共成功同步" + count + "款产品！" : "所有产品图片已经都是最新的。");
    }

}
