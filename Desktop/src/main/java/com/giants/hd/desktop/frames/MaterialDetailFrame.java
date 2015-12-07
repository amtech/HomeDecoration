package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.dialogs.BaseDialog;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.viewImpl.BasePanel;
import com.giants.hd.desktop.viewImpl.Panel_Material_Detail;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.Material;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 材料详细面板
 */
public class MaterialDetailFrame extends BaseFrame implements BasePanel.PanelListener {


    @Inject
    ApiManager apiManager;
    @Inject
    Panel_Material_Detail material_detail;


    Material material;
    Material oldMaterial;

    public  boolean changeAndSaved=false;
    public MaterialDetailFrame(Window window, Material material)
    {
        super("材料详情");
        setContentPane(   material_detail.getRoot());
        init(  material);
    }




    public void init(Material newMaterial)
    {


        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                close();

            }});

        material_detail.setListener(this);

        bindMaterial(newMaterial);


    }



    public void bindMaterial(Material newMaterial)
    {

        oldMaterial= (Material) ObjectUtils.deepCopy(newMaterial);
        this.material=newMaterial;
        material_detail.setData(material);
    }

    @Override
    public void save() {


         material_detail.getData(material);

        if(oldMaterial==null||!oldMaterial.equals(material))
        {


        new HdSwingWorker<Material,Object>(SwingUtilities.windowForComponent(material_detail.getRoot()))
        {
            @Override
            protected RemoteData<Material> doInBackground() throws Exception {
                return apiManager.saveMaterial(material);
            }

            @Override
            public void onResult(RemoteData<Material> data) {

                if(data.isSuccess())
                {
                    JOptionPane.showMessageDialog(MaterialDetailFrame.this,"保存成功");
                    changeAndSaved=true;
                    bindMaterial(data.datas.get(0));


                }else {

                    JOptionPane.showMessageDialog(MaterialDetailFrame.this,data.message);
                }







            }
        }.go();

        }else
        {


            JOptionPane.showMessageDialog(getRootPane(),"数据无改变");
        }

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

                        JOptionPane.showMessageDialog(MaterialDetailFrame.this,"删除成功！");

                        MaterialDetailFrame.this.dispose();



                    }else
                    {
                        JOptionPane.showMessageDialog(MaterialDetailFrame.this,data.message);
                    }

                }
            }.go();



        }

    }

    @Override
    public void close() {


        material_detail.getData(material);

        if(oldMaterial==null||!oldMaterial.equals(material)) {


            int option=   JOptionPane.showConfirmDialog(MaterialDetailFrame.this,"数据有改动，确定关闭窗口？", " 提示", JOptionPane.OK_CANCEL_OPTION);

            if (JOptionPane.OK_OPTION == option) {
                //点击了确定按钮

                MaterialDetailFrame.this.dispose();
            }

        }else
        {
            dispose();
        }





    }

    @Override
    public void verify() {

    }

    @Override
    public void unVerify() {

    }
}
