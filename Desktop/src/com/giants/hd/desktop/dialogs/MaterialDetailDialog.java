package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.view.*;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Material;
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
}
