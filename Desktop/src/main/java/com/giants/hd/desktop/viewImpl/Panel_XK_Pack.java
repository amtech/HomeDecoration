package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.interf.DataChangeListener;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Product;
import com.giants3.hd.utils.entity.ProductMaterial;
import com.giants3.hd.utils.entity.Xiankang;
import com.giants3.hd.utils.noEntity.ProductDetail;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * 咸康面板 包装额外信息信息
 * Created by davidleen29 on 2015/7/14.
 */
public class Panel_XK_Pack  extends BasePanel {
    private JTextField tf_pack_middle;
    private JTextField tf_pack_front_back;
    private JTextField tf_pack_cube;
    private JTextArea ta_pack_memo;
    private JTextField tf_pack_perimeter;
    private JPanel panel_xk_pack;
    private JTextField tf_pack_front;

    private Xiankang data;
    private DocumentListener documentListener;
    private DocumentListener memoDocumentListener;

    public DataChangeListener<Xiankang> dataChangeListener;

  public static final    String keyString = "护角";


    ProductDetail productDetiail;

    public Panel_XK_Pack(   ) {

        documentListener=new DocumentListener() {

            public StringBuilder stringBuilder=new StringBuilder();
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update()
            {



                if(dataChangeListener!=null)
                {



                    Xiankang xiankang=getData();





                    stringBuilder.setLength(0);
                    if(productDetiail.product!=null)
                    {
                        Product product=productDetiail.product;



                        if(product.packQuantity>0)
                        {

                            String head="";
                            if (hasHujiao(productDetiail)) {

                                head= "加" + keyString + ",";
                            }

                            stringBuilder.append(product.packQuantity).append("(");


                            if(xiankang.pack_perimeter>0&&xiankang.pack_front_back>0&&xiankang.pack_middle>0) {

                                    stringBuilder.append(head);




                                    stringBuilder.append("四周各").append(xiankang.pack_perimeter).append("”   ");
                                if (xiankang.pack_front_back > 0)
                                    stringBuilder.append("前后各").append(xiankang.pack_front_back).append("”   ");
                                if (xiankang.pack_middle > 0)
                                    stringBuilder.append("中间各").append(xiankang.pack_middle).append("”   ");
                            }else
                            if (xiankang.pack_perimeter > 0  &&xiankang.pack_front_back > 0)
                            {
                                stringBuilder.append(head);
                                stringBuilder.append("四周各").append(xiankang.pack_perimeter).append("”   ");
                                stringBuilder.append("前后各").append(xiankang.pack_front_back).append("”   ");
                            }
                            else
                            if(xiankang.pack_middle>0&&xiankang.pack_cube>0)
                            {

                                stringBuilder.append(head);
                                stringBuilder.append("六面各").append(xiankang.pack_cube).append("”   ");

                                stringBuilder.append("中间各").append(xiankang.pack_middle).append("”   ");
                            }else
                                if(xiankang.pack_cube>0) {
                                    stringBuilder.append(head);
                                    stringBuilder.append("六面各").append(xiankang.pack_middle).append("”   ");
                                }


                        }

                        stringBuilder.append(" )");
                    }


                    ta_pack_memo.setText(stringBuilder.toString());


                }

            }
        };


        memoDocumentListener=new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update()
            {



                if(dataChangeListener!=null)
                {


                    Xiankang xiankang=getData();



                    dataChangeListener.onDataChanged(xiankang);
                }

            }
        };









    }

    /**
     * 检查包装材料中是否有护角材料
     * @param productDetail
     * @return
     */
    private boolean hasHujiao(ProductDetail productDetail)
    {


        if(null!=productDetail&&null!=productDetail.packMaterials)
        {
            for(ProductMaterial material:productDetail.packMaterials)
            {
                if(material.packMaterialClass!=null&&material.packMaterialClass.name.indexOf(keyString)>-1)
                {
                    return true;
                }
            }
        }



        return false;
    }

    private void attachDocumentListener() {
        tf_pack_front.getDocument().addDocumentListener(documentListener);
        tf_pack_cube.getDocument().addDocumentListener(documentListener);
        tf_pack_front_back.getDocument().addDocumentListener(documentListener);
        tf_pack_middle.getDocument().addDocumentListener(documentListener);
        tf_pack_perimeter.getDocument().addDocumentListener(documentListener);
        tf_pack_front.getDocument().addDocumentListener(documentListener);

        ta_pack_memo.getDocument().addDocumentListener(memoDocumentListener);

    }

    private void detachDocumentListener() {
        tf_pack_front.getDocument().removeDocumentListener(documentListener);
        tf_pack_cube.getDocument().removeDocumentListener(documentListener);
        tf_pack_front_back.getDocument().removeDocumentListener(documentListener);
        tf_pack_middle.getDocument().removeDocumentListener(documentListener);

        tf_pack_perimeter.getDocument().removeDocumentListener(documentListener);
        tf_pack_front.getDocument().removeDocumentListener(documentListener);

        ta_pack_memo.getDocument().removeDocumentListener(memoDocumentListener);
    }

    /**
     * 设置面板是否可见
     * @param visible
     */
    public void setVisible(boolean visible) {


        panel_xk_pack.setVisible(visible);

    }

    @Override
    public JComponent getRoot() {
        return panel_xk_pack;
    }

    public void setData(Xiankang data) {
        this.data=data;
        if(data==null ) return ;
        detachDocumentListener();
        tf_pack_cube.setText(String.valueOf(data.pack_cube));
        tf_pack_middle.setText(String.valueOf(data.pack_middle));
        tf_pack_front_back.setText(String.valueOf(data.pack_front_back));
        tf_pack_front.setText(String.valueOf(data.pack_front));
        tf_pack_perimeter.setText(String.valueOf(data.pack_perimeter));
        ta_pack_memo.setText(String.valueOf(data.pack_memo));
        attachDocumentListener();

    }

    public Xiankang getData( ) {

        if(data==null)
            data=new Xiankang();

        try {
            data.pack_cube = Float.valueOf(tf_pack_cube.getText().trim());
        }catch (Throwable t)
        {
            t.printStackTrace();
            data.pack_cube=0;
        }


        try {
            data.pack_front = Float.valueOf(tf_pack_front.getText().trim());
        }catch (Throwable t)
        {
            t.printStackTrace();
            data.pack_front=0;
        }

        try {
            data.pack_front_back = Float.valueOf(tf_pack_front_back.getText().trim());
        }catch (Throwable t)
        {
            t.printStackTrace();
            data.pack_front_back=0;
        }


        try {
            data.pack_middle = Float.valueOf(tf_pack_middle.getText().trim());
        }catch (Throwable t)
        {
            t.printStackTrace();
            data.pack_middle=0;
        }


        try {
            data.pack_perimeter = Float.valueOf(tf_pack_perimeter.getText().trim());
        }catch (Throwable t)
        {
            t.printStackTrace();
            data.pack_perimeter=0;
        }

            data.pack_memo = ta_pack_memo.getText().trim();

        return data;







    }

    public boolean isModified(Xiankang data) {
        return false;
    }


    public void setDataChangeListener(DataChangeListener<Xiankang> dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }
}
