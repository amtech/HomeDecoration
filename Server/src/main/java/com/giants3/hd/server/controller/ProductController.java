package com.giants3.hd.server.controller;


import com.giants3.hd.server.repository.ProductMaterialRepository;
import com.giants3.hd.server.repository.ProductPackRepository;
import com.giants3.hd.server.repository.ProductPaintRepository;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.server.repository.ProductRepository;

import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

/**
* 产品信息
*/
@Controller
@RequestMapping("/product")
public class ProductController  extends BaseController{


    @Autowired
    private ProductRepository productRepository;



    @Autowired
    private ProductPackRepository productPackRepository;
    @Autowired
    private ProductMaterialRepository productMaterialRepository;

    @Autowired
    private ProductPaintRepository productPaintRepository;
    @Value("${filepath}")
    private String rootFilePath;





    @RequestMapping(value="/list", method = RequestMethod.GET)
    public
    @ResponseBody
    RemoteData<Product> listPrdtJson()   {


        return wrapData(productRepository.findAll());
    }


    @RequestMapping(value = "/search", method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<Product>   listPrdtJson(@RequestParam(value = "proName",required = false,defaultValue ="") String prd_name
    ,@RequestParam(value = "pageIndex",required = false,defaultValue ="0") int pageIndex,@RequestParam(value = "pageSize",required = false,defaultValue =  "20") int pageSize

    ) throws UnsupportedEncodingException {


        Pageable pageable=constructPageSpecification(pageIndex, pageSize);
          Page<Product> pageValue=  productRepository.findByNameLike("%" + prd_name + "%", pageable);

        List<Product> products=pageValue.getContent();

        //读取包装信息
        for(Product product:products)
        {

            List<ProductPack> packs=productPackRepository.findByProductIdEquals(product.id);
            product.packs=packs;
        }








        return  wrapData(pageIndex,pageable.getPageSize(),pageValue.getTotalPages(), (int) pageValue.getTotalElements(),products);









    }

    @RequestMapping( value = "/find", method = RequestMethod.GET)
    public
    @ResponseBody
    Product findProdcutById(@RequestParam("id") long productId)   {

        Product product= productRepository.findOne(productId);
        List<ProductPack> packs=productPackRepository.findByProductIdEquals(product.id);
        product.packs=packs;
        return product;
    }


    /**
     * 查询产品的详细信息
     * 包括 包装信息
     *      物料清单（胚体，油漆，包装）
     *
     * @param productId
     * @return
     */
    @RequestMapping( value = "/detail", method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    RemoteData<ProductDetail> findProductDetailById(@RequestParam("id") long productId)   {





        RemoteData<ProductDetail> remoteData=new RemoteData<>();

        //读取产品信息
        Product product= productRepository.findOne(productId);
        if(product==null)
        {

                remoteData.code=RemoteData.CODE_FAIL;
            remoteData.message="未能根据id找到产品";
            return
                    remoteData;

        }

        ProductDetail detail=new ProductDetail();


        detail.product=product;

        //读取包装信息
        List<ProductPack> packs=productPackRepository.findByProductIdEquals(product.id);
        product.packs=packs;


        //读取材料列表信息

      List<ProductMaterial> productMaterials=  productMaterialRepository.findByProductIdEquals(productId);
    List<ProductMaterial> conceptus=new ArrayList<>();
        List<ProductMaterial> assembles=new ArrayList<>();
    for(ProductMaterial productMaterial:productMaterials)
    {

        switch ((int)productMaterial.flowId)
        {
            case Flow.FLOW_CONCEPTUS:
                conceptus.add(productMaterial);
                break;

            case Flow.FLOW_PAINT:
                assembles.add(productMaterial);
                break;

            case Flow.FLOW_ASSEMBLE:
                break;

            case Flow.FLOW_PACK:
               // assembles.add(productMaterial);
                break;
        }

    }

       detail.conceptusMaterials=conceptus;
        detail.assembleMaterials=assembles;



        //读取油漆列表信息
        detail.paints=  productPaintRepository.findByProductIdEquals(productId);












        remoteData.datas.add(detail);
        return remoteData;
    }


    /**
     * 产品完整信息的保存
     *
     *  @param productDetail 产品全部信息
     * @return
     */
    @RequestMapping( value = "/save", method = RequestMethod.POST)
    public    @ResponseBody     RemoteData<ProductDetail> saveProductDetail(@RequestBody ProductDetail productDetail)   {



        long productId=productDetail.product.id;

        //新增加的产品数据
        Product newProduct=productDetail.product;

        /**
         * 未生成id 添加记录
          */
     if(!productRepository.exists(productId))
     {
         //更新缩略图
           updateProductPhotoData(newProduct);
     }else
     {
        //找出旧的记录
         Product oldData=productRepository.findOne(productId);
         //如果产品名称修改  则修正缩略图
         if(!oldData.name.equals(newProduct.name))
         {
              updateProductPhotoData(newProduct);
         }


     }


     //更新图片更新字段 记录图片的最后更新时间
       updateProductPhotoTime(newProduct);


        //最新product 数据
      Product product=     productRepository.save(newProduct);








        if(productDetail.paints!=null) {
            //保存油漆数据
            List<ProductPaint> oldPaints = productPaintRepository.findByProductIdEquals(product.id);
            //查找就记录是否存在新纪录中  如果不存在就删除。删除旧记录操作。
            for (ProductPaint oldPaint : oldPaints) {
                boolean exist=false;
                for (ProductPaint newPaint:productDetail.paints)
                {

                    if(oldPaint.getId()==newPaint.id)
                    {
                        exist=true;
                        break;
                    }
                }
                if(!exist)
                {
                    //不存在新纪录中 删除
                    productPaintRepository.delete(oldPaint.id);
                }

            }

            //添加或者修改记录
            for (ProductPaint newPaint:productDetail.paints)
            {
                newPaint.setProductId(product.id);
                newPaint.setProductName(product.name);
                productPaintRepository.save(newPaint);
            }




        }





       RemoteData data=new RemoteData();



        return data;
    }





    /**
     * Returns a Sort object which sorts persons in ascending order by using the last name.
     * @return
     */
    protected Sort sortByLastNameAsc() {
        return new Sort(Sort.Direction.ASC, "name");
    }





    /**
     * 更新产品的缩略图数据
     * @param product
     */
    private final void updateProductPhotoData(Product product)
    {


        String filePath= FileUtils.getProductPicturePath(rootFilePath, product.name,product.pVersion);

        //如果tup图片文件不存在  则 设置photo为空。
        if(!new File(filePath).exists())
        {
            product.setPhoto(null);

        }else
        {
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
    private final void updateProductPhotoTime(Product product)
    {


        String filePath=FileUtils.getProductPicturePath(rootFilePath,product.name,product.pVersion);

        BasicFileAttributes attributes =
                null;
        try {
            attributes = Files.readAttributes(new File(filePath).toPath(), BasicFileAttributes.class);

        } catch (IOException e) {
            e.printStackTrace();

        }


            if(null!=attributes)
            {

                FileTime lastModifiedTime = attributes.lastModifiedTime();
                product.setLastPhotoUpdateTime(lastModifiedTime.toMillis());
            }



    }


}
