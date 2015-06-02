package com.giants.hd.desktop.view;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.MaterialTableModel;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Material;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 材料列表面板
 */
public class Panel_Material  extends  BasePanel{
    private JPanel panel1;
    private JTable tb_material;
    private JTextField jtf_value;
    private JButton btn_search;
    private JButton btn_import;
    private Panel_Page pagePanel;


    @Override
    public JComponent getRoot() {
        return panel1;
    }


    @Inject
    ApiManager apiManager;

    @Inject
    MaterialTableModel materialTableModel;



    public Panel_Material( String value) {



        jtf_value.setText(value);

            search(value);


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String value= jtf_value.getText();
                search(value);
            }
        });

        tb_material.setModel(materialTableModel);


        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                search(jtf_value.getText().toString().trim(),pageIndex,pageSize);


            }
        });

        btn_import.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                showImportDialog();

            }
        });



        tb_material.setRowHeight(30);

    }

    private  void  showImportDialog()
    {

        JDialog dialog = new JDialog(getWindow(getRoot()));
        dialog.setModal(true);
        Panel_ImportMaterial panel = new Panel_ImportMaterial( );
        dialog.setContentPane(panel.getRoot());
        dialog.setMinimumSize(new Dimension(400,300));
        dialog.pack();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        search(jtf_value.getText().trim());
    }




    /**
     * 开启搜索功能
     *
     * @param value
     */

    public void search(final String value )
    {
        search(value,0,pagePanel.getPageSize());
    }
    public void search(final String value,final int pageIndex, final int pageSize)
    {



        new HdSwingWorker<Material,Object>( getWindow(getRoot()))
        {
            @Override
            protected RemoteData<Material> doInBackground() throws Exception {


                return   apiManager.loadMaterialByCodeOrName(value, pageIndex, pageSize);

            }

            @Override
            public void onResult(RemoteData<Material> data) {


                pagePanel.bindRemoteData(data);
                materialTableModel.setDatas(data.datas);

            }
        }.go();




    }
}
