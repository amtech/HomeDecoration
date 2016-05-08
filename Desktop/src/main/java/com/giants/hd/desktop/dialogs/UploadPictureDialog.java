package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.filters.PictureFileFilter;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public abstract class UploadPictureDialog extends BaseDialog {
    protected JPanel contentPane;
    protected JButton uploadPicture;
    protected JButton syncPicture;
    protected JCheckBox pictureOverride;

    public UploadPictureDialog(Window window) {
        super(window,"图片管理");
        setContentPane(contentPane);





        uploadPicture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                final File file = getSelectedFile();


                if (file == null) {

                } else {


                    final boolean doesOverride = pictureOverride.isSelected();


                    new HdSwingWorker<Void, String>(UploadPictureDialog.this) {

                        @Override
                        protected RemoteData<Void> doInBackground() throws Exception {

                            uploadFile(file, 1, doesOverride, this);

                            return new RemoteData<Void>();


                        }

                        @Override
                        protected void process(java.util.List<String> chunks) {

                            dialog.setMessage(chunks.get(0));
                        }

                        @Override
                        public void onResult(RemoteData<Void> data) {

                            JOptionPane.showMessageDialog(UploadPictureDialog.this, "图片上传完毕");

                            //清除本地缓存
                           // ImageLoader.getInstance().clearCache();

                        }
                    }.go();

                }

            }
        });


        syncPicture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new HdSwingWorker<Void,Object>(UploadPictureDialog.this)
                {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {


                      return  syncPicture();

                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {

                        JOptionPane.showMessageDialog(UploadPictureDialog.this,data.message);

                    }
                }.go();
            }
        });


    }



    private File getSelectedFile()
    {

        JFileChooser fileChooser = new JFileChooser(".");
        //下面这句是去掉显示所有文件这个过滤器。
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//添加excel文件的过滤器
        fileChooser.addChoosableFileFilter(new PictureFileFilter());
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
    private void uploadFile(File file,int type,boolean doesOverride,HdSwingWorker<Void,String> swingWorker) throws HdException {
        if(file.isDirectory())
        {

            File[] childFiles=file.listFiles();

            //每张图片独立上传
            for (int i = 0; i < childFiles.length; i++) {






                if(ImageUtils.isPictureFile(childFiles[i].getName()))
                    uploadFile(childFiles[i],type,doesOverride,swingWorker);

            }

        }else
        {
            RemoteData result=uploadPicture(file,doesOverride);


            swingWorker.publishMessage(result.message);


        }


    }



    protected abstract RemoteData<Void> uploadPicture(File file,boolean doesOverride) throws HdException;


    protected abstract  RemoteData<Void> syncPicture() throws HdException;

}
