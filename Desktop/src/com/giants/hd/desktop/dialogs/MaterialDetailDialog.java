package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.view.*;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.ProductDetail;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;

/**
 * 材料详细面板
 */
public class MaterialDetailDialog extends BaseDialog<Material>implements BasePanel.PanelListener {


    @Inject
    ApiManager apiManager;
    @Inject
    Panel_Material_Detail material_detail;


    Material material;
    public MaterialDetailDialog(Window window,Material material)
    {
        super(window,"材料详情");
        this.material=material;
        setContentPane(   material_detail.getRoot());
        init();
    }




    public void init()
    {
        //material_detail.init();
        material_detail.setListener(this);
        material_detail.setData(material);


    }

    @Override
    public void save() {


         material_detail.getData(material);


        new HdSwingWorker<Material,Object>(SwingUtilities.windowForComponent(material_detail.getRoot()))
        {
            @Override
            protected RemoteData<Material> doInBackground() throws Exception {
                return apiManager.saveMaterial(material);
            }

            @Override
            public void onResult(RemoteData<Material> data) {



                dispose();



            }
        }.go();

    }

    @Override
    public void delete() {







        if(material.id<=0)
        {

            JOptionPane.showMessageDialog(this, "材料数据未建立，请先保存");
            return;

        }



        int res=   JOptionPane.showConfirmDialog(this, "是否删除该材料？（导致数据无法恢复）", "删除材料", JOptionPane.OK_CANCEL_OPTION);
        if(res==JOptionPane.YES_OPTION)
        {
            new HdSwingWorker<Void,Void>(this)
            {

                @Override
                protected RemoteData<Void> doInBackground() throws Exception {

                    return     apiManager.deleteMaterialLogic(material.id);

                }

                @Override
                public void onResult(RemoteData<Void> data) {

                    if(data.isSuccess())
                    {

                        JOptionPane.showMessageDialog(MaterialDetailDialog.this,"删除成功！");

                        MaterialDetailDialog.this.dispose();



                    }else
                    {
                        JOptionPane.showMessageDialog(MaterialDetailDialog.this,data.message);
                    }

                }
            }.go();



        }

    }
}
