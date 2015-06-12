package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 包装 输入表格 模型
 */

public class ProductPackMaterialTableModel extends  BaseTableModel<ProductMaterial> implements Materialable{

    public static String[] columnNames = new String[]{"  材料类别    ","  材质     ","  位置    ","  物料编码   ", "材料名称", "数量","长","宽","高","长", "宽", "高","配额","单位","利用率","类型","单价","金额","分件备注"};
    public static int[] columnWidths = new int []{      80,              60,             60,            100,        120,        40,  40,  40, 40,  40,    40,  40,   80,    40,    60,     40,     60,   80, ConstantData.MAX_COLUMN_WIDTH};


    public static String[] fieldName = new String[]{"packMaterialClass","packMaterialType","packMaterialPosition","materialCode", "materialName", "quantity", "pLong", "pWidth", "pHeight","wLong","wWidth","wHeight","quota","unitName","available","type","price","amount","memo"};
    public  static Class[] classes = new Class[]{PackMaterialClass.class,PackMaterialType.class,PackMaterialPosition.class,Material.class, Material.class };

    public  static boolean[] editables = new boolean[]{true, true, true,true, true, true, true, true, true,false,false,false , false, false, true, false,false,true,true };

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

        if(fieldName[columnIndex].equals("amount")&&value instanceof Float&&product!=null )
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


            case 0:
                //设置包装材料大类型
                material.setPackMaterialClass((PackMaterialClass) aValue);
                break;


            case 1:
                //设置材料类型
                material.setPackMaterialType((PackMaterialType) aValue);


                break;


            case 2:
                //设置使用位置
                material.setPackMaterialPosition((PackMaterialPosition) aValue);

                break;
            case 5:
                //设置用量
                material.setQuantity(Float.valueOf(aValue.toString()));
                material.update();

                break;


            case 6:
                //设置长
                material.setpLong(Float.valueOf(aValue.toString()));
                material.update();

                break;


            case 7:
                //设置宽
                material.setpWidth(Float.valueOf(aValue.toString()));
                material.update();

                break;

            case 8:
                //设置高
                material.setpHeight(Float.valueOf(aValue.toString()));
                material.update();

                break;

            case 14:
                //设置高
                material.setAvailable(Float.valueOf(aValue.toString()));
                material.update();

                break;

            case 18:
                //设置
                material.setMemo( aValue.toString());
                break;



        }



        //检查波安装
        //如果是内盒
        //找出胶带 更新胶带信息
        if(material.getPackMaterialClass()!=null) {
            switch (material.getPackMaterialClass().name) {


                case PackMaterialClass.CLASS_INSIDE_BOX:

                    for (ProductMaterial productMaterial : getDatas()) {
                        PackMaterialClass packMaterialClass = productMaterial.getPackMaterialClass();
                        if (packMaterialClass != null) {
                            if (packMaterialClass.name.equals(PackMaterialClass.CLASS_JIAODAI)) {

                                productMaterial.updateRelatedMaterial(material);
                                int relateIndex=getDatas().indexOf(productMaterial);
                                fireTableRowsUpdated(relateIndex,relateIndex);
                                break;
                            }

                        }


                    }


                    break;


                case PackMaterialClass.CLASS_JIAODAI:

                    for (ProductMaterial productMaterial : getDatas()) {

                        PackMaterialClass packMaterialClass = productMaterial.getPackMaterialClass();
                        if (packMaterialClass != null) {
                            if (packMaterialClass.name.equals(PackMaterialClass.CLASS_INSIDE_BOX)) {

                                material.updateRelatedMaterial(productMaterial);
                                break;

                            }

                        }

                    }


                    break;

            }

        }

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
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT;
    }
}
