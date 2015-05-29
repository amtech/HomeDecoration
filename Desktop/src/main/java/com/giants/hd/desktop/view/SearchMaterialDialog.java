package com.giants.hd.desktop.view;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.interf.PageListener;
import com.giants.hd.desktop.model.MaterialTableModel;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;

public class SearchMaterialDialog extends BaseDialog<Material>{
    private JPanel contentPane;
    private JTextField tf_value;
    private JButton bn_search;
    private JTable table;
    private Panel_Page pagePanel;


    @Inject
    ApiManager apiManager;

    @Inject
    MaterialTableModel materialTableModel;
    public SearchMaterialDialog(Window window,String value) {
        super(window);
        setContentPane(contentPane);
        setModal(true);

        tf_value.setText(value);
        if(!StringUtils.isEmpty( value))
        {
            search(value);
        }

        bn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String value= tf_value.getText();
                search(value);
            }
        });

         table.setModel(materialTableModel);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if(e.getClickCount()==2)
                {

                   int modelRowId=table.convertRowIndexToModel( table.getSelectedRow());
                  Material material=  materialTableModel.getItem(modelRowId);
                    setResult(material);
                    setVisible(false);
                    dispose();


                }
            }
        });


        pagePanel.setListener(new PageListener() {
            @Override
            public void onPageChanged(int pageIndex, int pageSize) {


                search(tf_value.getText().toString().trim(),pageIndex,pageSize);


            }
        });


    }

    public static void main(String[] args) {
        SearchMaterialDialog dialog = new SearchMaterialDialog(null,null);
        dialog.setMinimumSize(new Dimension(800,600));
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
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



        new  SwingWorker<RemoteData<Material>,Long >(){


            @Override
            protected RemoteData<Material> doInBackground() throws HdException {



                return   apiManager.loadMaterialByCodeOrName(value,pageIndex,pageSize);


            }

            @Override
            protected void done() {
                super.done();

                try {
                    RemoteData<Material> remoteData=get();

                    pagePanel.bindRemoteData(remoteData);
                    materialTableModel.setDatas(remoteData.datas);













                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();


    }



}
