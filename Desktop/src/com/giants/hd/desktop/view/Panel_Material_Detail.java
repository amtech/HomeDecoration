package com.giants.hd.desktop.view;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.local.BufferData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.MaterialClass;
import com.giants3.hd.utils.entity.MaterialEquation;
import com.giants3.hd.utils.entity.MaterialType;
import com.google.inject.Inject;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * 材料详细
 */
public class Panel_Material_Detail extends  BasePanel {
    private JTextField tf_code;
    private JTextField tf_name;
    private JComboBox<MaterialClass> cb_materialClass;
    private JTextField tf_unit;
    private JFormattedTextField ftf_price;
    private JTextField tf_spec;
    private JComboBox<MaterialType> cb_materialType;
    private JFormattedTextField ftf_wLong;
    private JFormattedTextField ftf_wWdith;
    private JFormattedTextField ftf_wHeight;
    private JButton btn_save;
    private JTextField tf_memo;
    private JFormattedTextField ftf_unitRatio;
    //private JComboBox<MaterialEquation> cb_equation;
    private JPanel rootPanel;
    private JFormattedTextField ftf_available;
    private JButton btn_delete;
    private JLabel lb_photo;


    @Inject
    public Panel_Material_Detail()
    {

        init();


        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if(listener!=null)
                {
                    listener.save();
                }
            }
        });

        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(listener!=null)
                {
                    listener.delete();
                }
            }
        });
    }


    @Override

    public JComponent getRoot() {
        return rootPanel;
    }



    public void init()
    {



       // cb_materialClass.addItem();


        for(MaterialClass materialClass: BufferData.materialClasses)
        {
            cb_materialClass.addItem(materialClass);
        }

        for(MaterialType materialType: BufferData.materialTypes)
        {
            cb_materialType.addItem(materialType);
        }

//        for(MaterialEquation equation: BufferData.materialEquations)
//        {
//            cb_equation.addItem(equation);
//        }

         lb_photo  .addMouseListener(new MouseInputAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 super.mouseClicked(e);
                 if (e.getClickCount() >= 2) {


                     if (!StringUtils.isEmpty(tf_code.getText().trim())) {
                         ImageViewDialog.showMaterialDialog(getWindow(getRoot()), tf_code.getText().trim());
                     } else {


                         JOptionPane.showMessageDialog(lb_photo, "请输入编码...");
                     }

                 }
             }
    });

    }



    public void setData(Material data) {
        tf_code.setText(data.getCode());
        tf_name.setText(data.getName());
        tf_unit.setText(data.getUnitName());
        tf_spec.setText(data.getSpec());
        tf_memo.setText(data.getMemo());

        ftf_price.setValue(new Float(data.getPrice()));
        ftf_wLong.setValue(new Float(data.getwLong()));
        ftf_wWdith.setValue(new Float(data.getwWidth()));
        ftf_wHeight.setValue(new Float(data.getwHeight()));
        ftf_available.setValue(new Float(data.getAvailable()));
        ftf_unitRatio.setValue(new Float(data.getUnitRatio()));

        int selectedItem=0;
        for(int i=0,count=cb_materialClass.getItemCount();i<count;i++)
        {

            if(cb_materialClass.getItemAt(i).code.equals(data.classId))
            {
                selectedItem=i;
                break;
            }
        }
        cb_materialClass.setSelectedIndex(selectedItem);



        selectedItem=0;
        for(int i=0,count=cb_materialType.getItemCount();i<count;i++)
        {

            if(data.typeId==cb_materialType.getItemAt(i).typeId)
            {
                selectedItem=i;
                break;
            }
        }
        cb_materialType.setSelectedIndex(selectedItem);




        lb_photo.setIcon(data.photo == null ? null : new ImageIcon(data.photo));



//        selectedItem=-1;
//        for(int i=0,count=cb_equation.getItemCount();i<count;i++)
//        {
//
//            if(data.equationId==cb_equation.getItemAt(i).equationId)
//            {
//                selectedItem=i;
//                break;
//            }
//        }
//        cb_equation.setSelectedIndex(selectedItem);

    }

    public void getData(Material data) {
        data.setCode(tf_code.getText());
        data.setName(tf_name.getText());
        data.setUnitName(tf_unit.getText());
        data.setSpec(tf_spec.getText());
        data.setMemo(tf_memo.getText());

        MaterialClass materialClass= (MaterialClass) cb_materialClass.getSelectedItem();
        data.setClassId(materialClass.code);
        data.setClassName(materialClass.name);


        MaterialType materialType= (MaterialType) cb_materialType.getSelectedItem();
        data.setTypeId(materialType.typeId);
        data.setTypeName(materialType.typeName);


//        MaterialEquation equation= (MaterialEquation) cb_equation.getSelectedItem();
//        data.equationId=equation.equationId;


        data.setPrice(Float.valueOf(ftf_price.getValue().toString()));

        data.setwLong(Float.valueOf(ftf_wLong.getValue().toString()));

        data.setwWidth(Float.valueOf(ftf_wWdith.getValue().toString()));

        data.setwHeight(Float.valueOf(ftf_wHeight.getValue().toString()));

        data.setAvailable(Float.valueOf(ftf_available.getValue().toString()));

        data.setUnitRatio(Float.valueOf(ftf_unitRatio.getValue().toString()));



    }

    public boolean isModified(Material data) {
        if (tf_code.getText() != null ? !tf_code.getText().equals(data.getCode()) : data.getCode() != null) return true;
        if (tf_name.getText() != null ? !tf_name.getText().equals(data.getName()) : data.getName() != null) return true;
        if (tf_unit.getText() != null ? !tf_unit.getText().equals(data.getUnitName()) : data.getUnitName() != null)
            return true;
        if (tf_spec.getText() != null ? !tf_spec.getText().equals(data.getSpec()) : data.getSpec() != null) return true;
        if (tf_memo.getText() != null ? !tf_memo.getText().equals(data.getMemo()) : data.getMemo() != null) return true;
        return false;
    }



}
