package com.giants.hd.desktop.model;

import com.giants.hd.desktop.local.ConstantData;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.ProductProcess;
import com.giants3.hd.utils.entity.ProductWage;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 产品工资列表模型
 */

public class ProductWageTableModel extends  BaseTableModel<ProductWage> implements Processable {

    public static String[] columnNames = new String[]{"序号","工序编码", "工序名称", " 工价 ", " 金额 ","备注                    "};
    public static int[] columnWidths=new int[]{    40,      150,        200,        80,      100,   ConstantData.MAX_COLUMN_WIDTH};

    public static String[] fieldName = new String[]{ConstantData.COLUMN_INDEX,"processCode", "processName", "price", "amount", "memo" };
    public  static Class[] classes = new Class[]{Object.class,ProductProcess.class, ProductProcess.class  };

    public  static boolean[] editables = new boolean[]{false,true, true, true, false, true};
    private Product product;

    @Inject
    public ProductWageTableModel() {
        super(columnNames,fieldName,classes,ProductWage.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editables[columnIndex];
    }



    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value= super.getValueAt(rowIndex, columnIndex);

        if(fieldName[columnIndex].equals("amount")&&value instanceof Float&&product!=null&&product.packQuantity!=0)
        {

            float floatValue=Float.valueOf(value.toString());
            value=floatValue/product.packQuantity;

        }

        return value;
    }
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {



        super.setValueAt(aValue, rowIndex, columnIndex);

        ProductWage data=getItem(rowIndex);
        switch (columnIndex)
        {

            case  1:
                 data.setProcessCode(aValue.toString());

                break;
            case 2:

                data.setProcessName(aValue.toString());
                break;


            case 3:

                data.setPrice(Float.valueOf(aValue.toString()));
                data.setAmount(data.price);
                break;


            case 5:

               data.setMemo(aValue.toString());
                break;


        }


        fireTableRowsUpdated(rowIndex,rowIndex);


    }


    public void setProduct(Product product) {
        this.product = product;
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
     * 更新指定行的工序
     * @param process
     * @param rowIndex
     */
    @Override
    public void setProcess(ProductProcess process, int rowIndex) {
        if(rowIndex>=0&&rowIndex<datas.size())
        {
            ProductWage wage=getItem(rowIndex);
            wage.setProductProcess(process);



        }

        fireTableRowsUpdated(rowIndex,rowIndex);
    }
}
