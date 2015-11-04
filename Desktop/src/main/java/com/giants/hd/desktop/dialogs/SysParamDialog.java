package com.giants.hd.desktop.dialogs;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.HdUIException;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.GlobalData;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SysParamDialog extends BaseDialog {
    private JPanel contentPane;
    private JTextField tf_cost_price_ratio;
    private JTextField tf_exportRate;
    private JTextField tf_extra_ratio_diluent;
    private JTextField tf_price_transport;
    private JTextField tf_fob_addtion;
    private JTextField tf_price_diluent;
    private JButton save;
    private JTextField tf_repairePrice;
    private JTextField tf_manageRatioXK;
    private JTextField tf_manageRatioNormal;
    private JTextField tf_manageRatioForeign;
    GlobalData oldData;
    GlobalData newData;
    @Inject
    ApiManager apiManager;


    public SysParamDialog(Window window) {
        super(window);
        setTitle("全局参数设定");

        setContentPane(contentPane);
        setSize(new Dimension(700, 500));
        final GlobalData globalData= (GlobalData) ObjectUtils.deepCopy(CacheManager.getInstance().bufferData.globalData);
        init(globalData);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                try {
                    getData(newData);
                }catch (HdException ex)
                {
                    JOptionPane.showMessageDialog(getParent(),ex.getMessage());
                    return ;
                }

               if(newData.equals(oldData))
               {

                   JOptionPane.showMessageDialog(getParent(),"数据无改变");
                   return;
               }

                new HdSwingWorker<GlobalData,Object>((Window)getParent())
                {
                    @Override
                    protected RemoteData<GlobalData> doInBackground() throws Exception {


                        return   apiManager.setGlobalData(newData);

                    }

                    @Override
                    public void onResult(RemoteData<GlobalData> data) {

                        if(data.isSuccess())
                        {


                            JOptionPane.showMessageDialog(getParent(),"修改成功");

                            CacheManager.getInstance().bufferData.globalData=data.datas.get(0);
                            init((GlobalData) ObjectUtils.deepCopy(CacheManager.getInstance().bufferData.globalData));
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(getParent(),data.message);
                        }




                    }
                }.go();




            }
        });
    }




    public void getData(GlobalData globalData) throws HdException {

        try {
            globalData.price_of_diluent =
                    Float.valueOf(tf_price_diluent.getText().trim());

            globalData.exportRate =
                    Float.valueOf(tf_exportRate.getText().trim());
            globalData.extra_ratio_of_diluent = Float.valueOf(tf_extra_ratio_diluent.getText().trim());
            globalData.price_of_export = Float.valueOf(tf_price_transport.getText().trim());
            globalData.addition = Float.valueOf(tf_fob_addtion.getText().trim());
            globalData.cost_price_ratio = Float.valueOf(tf_cost_price_ratio.getText().trim());
            globalData.manageRatioNormal= Float.valueOf(tf_manageRatioNormal.getText().trim());
            globalData.manageRatioXK= Float.valueOf(tf_manageRatioXK.getText().trim());
            globalData.repairPrice= Float.valueOf(tf_repairePrice.getText().trim());
            globalData.manageRatioForeign= Float.valueOf(tf_manageRatioForeign.getText().trim());
        }catch (Throwable t)
        {
            throw   HdUIException.create("输入的数据有错， 请输入数字,不能为空");
        }

    }


    private void init(GlobalData globalData)
    {

        newData=globalData;
        this.oldData=(GlobalData) ObjectUtils.deepCopy(globalData);
        tf_price_diluent.setText(String.valueOf(globalData.price_of_diluent));
        tf_exportRate.setText(String.valueOf(globalData.exportRate));
        tf_extra_ratio_diluent.setText(String.valueOf(globalData.extra_ratio_of_diluent));
        tf_price_transport.setText(String.valueOf(globalData.price_of_export));
        tf_fob_addtion.setText(String.valueOf(globalData.addition));
        tf_cost_price_ratio.setText(String.valueOf(globalData.cost_price_ratio));

        tf_manageRatioNormal.setText(String.valueOf(globalData.manageRatioNormal))  ;
        tf_manageRatioXK.setText(String.valueOf(globalData.manageRatioXK))  ;
        tf_repairePrice.setText(String.valueOf(globalData.repairPrice))  ;
        tf_manageRatioForeign.setText(String.valueOf(globalData.manageRatioForeign))  ;


    }
}
