package com.giants.hd.desktop.widget;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.local.ImageLoader;
import com.giants3.hd.domain.api.HttpUrl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *附件列表图片
 * Created by david on 2015/9/22.
 */
public class AttachPanel  extends JPanel {

    List<JLabel> labelList;

    List<String> pictureUrls;


    private MouseAdapter mouseAdapter;



    public AttachPanel()
    {


        mouseAdapter=new PanelItemMouseAdapter();
        labelList=new ArrayList<>();
        pictureUrls=new ArrayList<>();
    }


    public List<String> getAttachFiles()
    {
        return pictureUrls;
    }


    public void addUrl(String  fileName )
    {




        final JLabel jLabel=new JLabel();
        showPicture(jLabel,fileName);


    }


    public void setAttachFiles(String[] urls)
    {


        labelList.clear();
        pictureUrls.clear();

        this.removeAll();

        for(String s:urls)
        {
            final JLabel jLabel=new JLabel();
            showPicture(jLabel,s);
        }





    }



    private void showPicture(final JLabel jLable, final String fileName)
    {





        jLable.addMouseListener(mouseAdapter);
        labelList.add(jLable);
        pictureUrls.add(fileName);



        String url=getUrl(fileName);

        ImageLoader.getInstance().displayImage(new Iconable() {
            @Override
            public void setIcon(ImageIcon icon) {
                jLable.setIcon(icon);
                jLable.setText("");
            }

            @Override
            public void onError(String message) {
                jLable.setText(message);
            }
        },url,120,120);


//        new HdPictureWorker(new Iconable(){
//            @Override
//            public void setIcon(ImageIcon icon) {
//
//                if(icon==null)
//                {
//                    jLable.setText("图片读取失败");
//                }else {
//                    jLable.setIcon(icon);
//                }
//
//            }
//        },120,120,url).execute();

        add(jLable);

        this.updateUI();

    }


    private class PanelItemMouseAdapter extends MouseAdapter
    {


        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            showMenu(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            showMenu(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            JLabel label= (JLabel) e.getSource();
            final int index=labelList.indexOf(label);



                if(e.getClickCount()==2)
                {



                    String url=getUrl(pictureUrls.get(index));

                    ImageViewDialog.showDialog(SwingUtilities.getWindowAncestor(AttachPanel.this), url);
                   // ImageViewDialog.showDialog(SwingUtilities.getWindowAncestor(this),);
                }






        }
    }

    /**
     * 弹出删除菜单
     * @param e
     */
    private void showMenu(MouseEvent e)
    {
        final JLabel label= (JLabel) e.getSource();
        final int index=labelList.indexOf(label);
        //弹出删除菜单。
        if (e.isPopupTrigger()) {


            JPopupMenu menu=new JPopupMenu();
            JMenuItem delete = new JMenuItem("删除");
            menu.add(delete);
            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    labelList.remove(index);
                    pictureUrls.remove(index);
                    AttachPanel.this.remove(label);

                   AttachPanel.this.updateUI();

                }
            });

            menu.show(e.getComponent(), e.getX(), e.getY());


        }
    }



    private String  getUrl(String fileName)
    {

        String url="";
        if(fileName.startsWith("TEMP_"))
        {
            url= HttpUrl.loadTempPicture(fileName);

        }else
        {
            url=HttpUrl.loadAttachPicture(fileName);
        }

        return url;
    }

}
