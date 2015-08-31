package com.giants.hd.desktop;

import com.giants.hd.desktop.api.HttpUrl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ImageViewDialog extends JDialog {
    private JPanel contentPane;
    private JLabel picture;

    private ImageViewDialog(Window frame) {

        super(frame);
        setContentPane(contentPane);

        setModal(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        Dimension dimension=   java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setMinimumSize(new Dimension(600, 200));
        setLocation((dimension.width - 600) / 2, (dimension.height - 200) / 2);

    }






    public  void loadImageAndShow(final String url)
    {


        picture.setText("正在加载图片....");

        new SwingWorker<BufferedImage,String>()
        {

            @Override
            protected void done() {
                super.done();

                BufferedImage image= null;
                try {
                    image = get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(null!=image) {


                    picture.setText("");
                    Dimension dimension=   java.awt.Toolkit.getDefaultToolkit().getScreenSize();


                  int width=  image.getWidth();
                    int height=image.getHeight();


                    while (!(width<dimension.width&&height<dimension.height))
                    {
                        width= (int) (width/1.1f);height = (int) (height/1.1f);
                    }



                    try {
                      BufferedImage  newImage= resizeImage (image, width, height, image.getType());

                        image=newImage;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageIcon imageIcon=new ImageIcon(image);
                    picture.setIcon(imageIcon);



                   setSize(new Dimension(width, height));
                   setLocation((dimension.width - width) / 2, (dimension.height - height) / 2);








                }else
                {
                    picture.setText("图片加载失败");

                }
                //ImageViewDialog.this.pack();


            }

            @Override
            protected BufferedImage doInBackground() throws Exception {
                return ImageIO.read(new URL(url));
            }
        }.execute();






        setVisible(true);

    }

    /**
     * 显示图片显示框体框
     * @param materialCode
     */
    public static void showMaterialDialog(Window frame,String materialCode,String classId) {



        String url= HttpUrl.loadMaterialPicture(materialCode, classId);
         showDialog(frame, url);

    }

    /**
     * 显示图片显示框体框
     * @param productName
     */
    public static void showProductDialog(Window frame,String productName,String version) {



        String url=HttpUrl.loadProductPicture(productName, version);

        showDialog(frame,url,productName+"-"+version);

    }

    /**
     * 显示图片显示框体框
     * @param url
     */
    public static void showDialog(Window frame,String url) {
        showDialog(frame,url,url.substring(url.lastIndexOf("/")+1,url.lastIndexOf(".")));

    }

    /**
     * 显示图片显示框体框
     * @param url
     */
    public static void showDialog(Window frame,String url,String title) {
        ImageViewDialog dialog=new ImageViewDialog(frame);
        dialog.setTitle(title);
        dialog.loadImageAndShow(url);


    }


    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height, int type) throws IOException {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
}
