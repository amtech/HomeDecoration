package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants.hd.desktop.utils.RandomUtils;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * 包装 输入表格 模型
 */

public class ProductPackMaterialTableModel extends  BaseTableModel<ProductMaterial> implements Materialable{


      static final String COLUMN_AMOUNT = "amount";

    public static String[] columnNames = new String[]{"序号","  材料类别    ","  材质     ","  位置    ","间隔","  物料编码   ", "材料名称", "数量","长","宽","高","毛长", "毛宽", "毛高","配额","单位","利用率","类型","单价","金额","分件备注"};
    public static int[] columnWidths = new int []{       30 ,   80,              60,             60,       40 ,    100,        120,        40,  40,  40, 40,  40,    40,  40,   80,    40,    60,     40,     60,   80, ConstantData.MAX_COLUMN_WIDTH};
     public static String[] fieldName = new String[]{ConstantData.COLUMN_INDEX,"packMaterialClass","packMaterialType","packMaterialPosition","gap","materialCode", "materialName", "quantity", "pLong", "pWidth", "pHeight","wLong","wWidth","wHeight","quota","unitName","available","type","price",COLUMN_AMOUNT,"memo"};
    public  static Class[] classes = new Class[]{Object.class,PackMaterialClass.class,PackMaterialType.class,PackMaterialPosition.class,String.class,Material.class, Material.class };

    public  static boolean[] editables = new boolean[]{false,true, true, true,true,true, true, true, true, true, true,false,false,false , false, false, true, false,false,true,true };

    private Product product;


    @Inject
    public ProductPackMaterialTableModel() {
        super(columnNames,fieldName,classes,ProductMaterial.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editables[columnIndex];
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value= super.getValueAt(rowIndex, columnIndex);


        if(fieldName[columnIndex].equals(COLUMN_AMOUNT)&&value instanceof Float&&product!=null )
        {

            if(product.packQuantity>0)
            {
                float floatValue=Float.valueOf(value.toString());
                value= FloatHelper.scale(floatValue/product.packQuantity);
            }
//            else
//            {
//                value="请输入装箱数";
//            }


        }

        return value;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {



        super.setValueAt(aValue, rowIndex, columnIndex);

        ProductMaterial material=getItem(rowIndex);

        switch (columnIndex)
        {


            case 1:
                //设置包装材料大类型
                material.setPackMaterialClass((PackMaterialClass) aValue);
                updateProduct();
                break;


            case 2:
                //设置材料类型
                material.setPackMaterialType((PackMaterialType) aValue);


                break;


            case 3:
                //设置使用位置
                material.setPackMaterialPosition((PackMaterialPosition) aValue);
                updateProduct();
                break;


            case 4:
                //设置间距
                material.setGap(Float.valueOf(aValue.toString()));
                updateProduct();

                break;
            case 7:
                //设置用量
                material.setQuantity(Float.valueOf(aValue.toString()));
                material.update();

                break;


            case 8:
                //设置长
                material.setpLong(Float.valueOf(aValue.toString()));
                material.update();

                break;


            case 9:
                //设置宽
                material.setpWidth(Float.valueOf(aValue.toString()));
                material.update();

                break;

            case 10:
                //设置高
                material.setpHeight(Float.valueOf(aValue.toString()));
                material.update();

                break;

            case 16:
                //设置高
                material.setAvailable(Float.valueOf(aValue.toString()));
                material.update();

                break;

            case 20:
                //设置备注
                material.setMemo( aValue.toString());
                break;



        }



        //检查包装
        //如果是内盒
        //找出胶带 更新胶带信息
        if(material.getPackMaterialClass()!=null) {
            switch (material.getPackMaterialClass().name) {


                case PackMaterialClass.CLASS_INSIDE_BOX:

                    for (ProductMaterial productMaterial : datas) {
                        PackMaterialClass packMaterialClass = productMaterial.getPackMaterialClass();
                        if (packMaterialClass != null) {
                            if (packMaterialClass.name.equals(PackMaterialClass.CLASS_JIAODAI)) {

                                productMaterial.updateJiaodaiQuota(product, material);
                                int relateIndex=datas.indexOf(productMaterial);
                                fireTableRowsUpdated(relateIndex,relateIndex);
                                break;
                            }

                        }


                    }


                    break;


                case PackMaterialClass.CLASS_JIAODAI:


                    //找出内盒  更新胶带信息
                    ProductMaterial foundNeihe=null;
                    for (ProductMaterial productMaterial : datas) {

                        PackMaterialClass packMaterialClass = productMaterial.getPackMaterialClass();
                        if (packMaterialClass != null) {
                            if (packMaterialClass.name.equals(PackMaterialClass.CLASS_INSIDE_BOX)) {

                                foundNeihe=productMaterial;

                                break;

                            }

                        }

                    }
                    //找出内盒  更新胶带信息
                    material.updateJiaodaiQuota(product, foundNeihe);


                    break;

            }

        }


        //保丽龙平板计算公式

        fireTableRowsUpdated(rowIndex,rowIndex);



    }


    public void setProduct(Product product) {
        this.product = product;


    }

    @Override
    public void  setMaterial(Material material,int rowIndex)
    {


        ProductMaterial productMaterial=getItem(rowIndex);
        if(productMaterial!=null)
        {
            productMaterial.updateMaterial(material);
        }


        fireTableRowsUpdated(rowIndex,rowIndex);

    }


    @Override
    public ProductMaterial addNewRow(int index) {
        ProductMaterial p=     super.addNewRow(index);



            p.id= -RandomUtils.nextInt();

        //包装的计算公式不一致  需要在本地标记类型id
        p.setFlowId(Flow.FLOW_PACK);
        return p;
    }


    @Override
    public int[] getColumnWidth() {
        return columnWidths;
    }


    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT*2/3;
    }


    /**
     * 产品信息更新  引发表格数据调整。
     */
    public void updateProduct() {


        //更新数据



        //胶带信息 与产品的包装类型相关

        //找出胶带
        int size = datas.size();

        //找出内盒数据
        ProductMaterial neihe=null;

        //找出外箱数据
        ProductMaterial waixiang=null;


        //找出胶带
        List<ProductMaterial> jiaodais=new ArrayList<>();

        //找出保丽隆
        List<ProductMaterial> baolilongs=new ArrayList<>();

        float gapFront=0;
        float gapBetween=0;

        for (int i = 0; i < size; i++) {
            ProductMaterial material=datas.get(i);
            PackMaterialClass packMaterialClass = material.getPackMaterialClass();
            String packMaterialClassName=packMaterialClass==null?"":packMaterialClass.name;
            if(!StringUtils.isEmpty(packMaterialClassName))
            switch (packMaterialClassName)
            {


                case PackMaterialClass.CLASS_BOX:
                    if(waixiang==null)
                        waixiang=material;
                    break;
                case PackMaterialClass.CLASS_INSIDE_BOX:
                    if(neihe==null)
                    neihe=material;
                break;
                case PackMaterialClass.CLASS_JIAODAI:

                    jiaodais.add(material);
                    break;

                case PackMaterialClass.CLASS_BAOLILONG:

                    baolilongs.add(material);
                    break;

            }



            //查找特定间隔值
            //前部
            //中间
            PackMaterialPosition position=material.getPackMaterialPosition();
            if(position!=null)
            {
                switch (position.name)
                {
                    case PackMaterialPosition.BETWEEN:
                        gapBetween=material.gap;

                        break;

                    case PackMaterialPosition.FRONT:
                        gapFront=material.gap;

                        break;
                }
            }




        }

            for(ProductMaterial jiaodai:jiaodais)
              jiaodai.updateJiaodaiQuota(product,neihe);


        for(ProductMaterial baolilong:baolilongs)
        {
            baolilong.updateBAOLILONGQuota(product, waixiang,gapFront,gapBetween);
        }






        //保丽龙的信息  跟产品的包装类型相关





        fireTableDataChanged();

    }
}
