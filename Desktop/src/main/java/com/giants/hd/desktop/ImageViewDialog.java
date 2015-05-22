package com.giants.hd.desktop;

import com.giants.hd.desktop.api.HttpUrl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ImageViewDialog extends JDialog {
    private JPanel contentPane;
    private JLabel picture;

    public ImageViewDialog(Window frame) {

        super(frame);
        setContentPane(contentPane);

        setModal(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);


// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {

        ImageViewDialog dialog=new ImageViewDialog(null);
        dialog.loadImageAndShow(HttpUrl.loadProductPicture("10X1003"));

    }



    public  void loadImageAndShow(final String url)
    {





        picture.setText("正在加载图片....");
        pack();
        new SwingWorker<Image,String>()
        {

            @Override
            protected void done() {
                super.done();

                Image image= null;
                try {
                    image = get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(null!=image) {
                    picture.setText("");
                    picture.setIcon(new ImageIcon(image));
                }else
                {
                    picture.setText("图片加载失败");
                }
                ImageViewDialog.this.pack();


            }

            @Override
            protected Image doInBackground() throws Exception {
                return ImageIO.read(new URL(url));
            }
        }.execute();



        setMinimumSize(new Dimension(800, 600));

        pack();
        setVisible(true);

    }

    /**
     * 显示图片显示框体框
     * @param productName
     */
    public static void showDialog(Window frame,String productName) {



        String url=HttpUrl.loadProductPicture(productName);
        ImageViewDialog dialog=new ImageViewDialog(frame);
        dialog.loadImageAndShow(url);

    }
}
