package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.filters.PictureFileFilter;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.view.BasePanel;
import com.giants.hd.desktop.view.Panel_PhotoSync;
import com.giants.hd.desktop.view.Panel_UploadPicture;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * 图片批量上传对话框
 */
public class UploadPictureDialog extends BaseDialog<Void>  implements BasePanel.PanelListener {


    @Inject
    ApiManager apiManager;


    @Inject
    Panel_UploadPicture panel_uploadPicture;

    public UploadPictureDialog(Window window)
    {
        super(window,"图片同步");
        setMinimumSize(new Dimension(400,400));
        setLocationRelativeTo(window);


        setContentPane(   panel_uploadPicture.getRoot());
        init();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }




    public void init()
    {


        panel_uploadPicture.btn_uploadMaterial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                final    File file=  getSelectedFile();
                if(file==null)
                {

                }else
                {




                    final  boolean doesOverride=panel_uploadPicture.cb_MaterialReplace.isSelected();



                    new HdSwingWorker<Void,Object>(UploadPictureDialog.this)
                    {

                        @Override
                        protected RemoteData<Void> doInBackground() throws Exception {

                             uploadFile(file,0,doesOverride);

                            return new RemoteData<Void>();
                        }

                        @Override
                        public void onResult(RemoteData<Void> data) {

                            JOptionPane.showMessageDialog(UploadPictureDialog.this,"材料上传完毕");

                        }
                    }.go();

                }

            }
        });

        panel_uploadPicture.btn_uploadProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



            final    File file=  getSelectedFile();


                if(file==null)
                {

                }else
                {




                    final  boolean doesOverride=panel_uploadPicture.cb_productReplace.isSelected();


                new HdSwingWorker<Void,Object>(UploadPictureDialog.this)
                {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {

                             uploadFile(file,1,doesOverride);
                        return new RemoteData<Void>();


                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {

                        JOptionPane.showMessageDialog(UploadPictureDialog.this,"产品图片上传完毕");

                    }
                }.go();

                }
            }
        });
    }




    @Override
    public void save() {









    }

    @Override
    public void delete() {

    }

    @Override
    public void close() {
        dispose();
    }


    private File getSelectedFile()
    {

        JFileChooser fileChooser = new JFileChooser(".");
        //下面这句是去掉显示所有文件这个过滤器。
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//添加excel文件的过滤器
         fileChooser.addChoosableFileFilter(new PictureFileFilter( ));
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

           return fileChooser.getSelectedFile();

        }
        return null;

    }


    /**
     *递归上传文件方法
     * @param file
     * @throws HdException
     */
    private void uploadFile(File file,int type,boolean doesOverride) throws HdException {
        if(file.isDirectory())
        {

            File[] childFiles=file.listFiles();

            for (int i = 0; i < childFiles.length; i++) {



                    uploadFile(childFiles[i],type,doesOverride);

            }

        }else
        {
            if( type==1)  apiManager.uploadProductPicture(file,doesOverride);
                else
                apiManager.uploadMaterialPicture(file, doesOverride);
        }


    }

}
