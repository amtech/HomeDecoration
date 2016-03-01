package com.giants3.hd.server.service;

import com.giants3.hd.server.repository.*;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.noEntity.ProductDetail;
import com.giants3.hd.utils.noEntity.QuotationDetail;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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


    public  RemoteData<Product>  searchProductList(String name, int pageIndex, int pageSize)
    {

        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        String likeValue="%" + name.trim() + "%";
        Page<Product> pageValue = productRepository.findByNameLikeOrPVersionLikeOrderByNameAsc(likeValue,likeValue, pageable);

        List<Product> products = pageValue.getContent();


        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), products);



    }


    /**
     * 根据产品id 查询产品详情
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

        detail.productLog=productLogRepository.findFirstByProductIdEquals(productId);




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
        detail.packMaterials=packCost;




        //读取工资数据
        List<ProductWage> productWages = productWageRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);
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
        detail.paints = productPaintRepository.findByProductIdEqualsOrderByItemIndexAsc(productId);


        return detail;

    }

}
