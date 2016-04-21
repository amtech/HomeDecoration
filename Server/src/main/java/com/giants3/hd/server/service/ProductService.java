package com.giants3.hd.server.service;

import com.giants3.hd.server.entity.Flow;
import com.giants3.hd.server.entity.Product;
import com.giants3.hd.server.entity.ProductMaterial;
import com.giants3.hd.server.entity.ProductWage;
import com.giants3.hd.server.noEntity.ProductDetail;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * quotation 业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class ProductService extends AbstractService implements InitializingBean, DisposableBean {


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
     * @param productNames  货号必须绾完全匹配。
     * @return
     */
    public RemoteData<Product> loadProductByNameRandom(String productNames, boolean withCopy) {

        String[] productNameArray = productNames.split(",|，");
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
}
