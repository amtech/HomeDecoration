package com.giants.hd.desktop;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.api.HttpUrl;
import com.giants.hd.desktop.local.BufferData;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.PropertyWorker;
import com.giants.hd.desktop.view.Panel_Material;
import com.giants.hd.desktop.view.Panel_ProductList;
import com.giants.hd.desktop.view.SearchMaterialDialog;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import com.google.inject.Guice;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by davidleen29 on 2015/5/6.
 */
public class Main extends JFrame {
    private JPanel panel1;
    private JTextPane textPane1;
    private JTextArea textArea1;
    private JTextField tf_product;
    private JLabel lable2;
    private JLabel photo;



    @Inject
    ApiManager apiManager;

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }


    public Main()
    {
        super();






        String baseUrl=PropertyWorker.readData("BaseUrl");

        if(null!=baseUrl)
        HttpUrl.iniBaseUrl(baseUrl );



        Guice.createInjector().injectMembers(this);
    }

    public static void main(String[] args) {

        try {
//            // Set cross-platform Java L&F (also called "Metal")
//            UIManager.setLookAndFeel(
//                    UIManager.getCrossPlatformLookAndFeelClassName());
//

//            // Set System L&F
//            UIManager.setLookAndFeel(
//                    UIManager.getSystemLookAndFeelClassName());


            UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getName());


         // setUIFont(new javax.swing.plaf.FontUIResource("宋体", Font.PLAIN, 18));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }


        final Main frame = new Main();




        frame.setContentPane(frame.panel1);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.generateMenu();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //点击了退出系统按钮
                int option = JOptionPane.showConfirmDialog(frame, "确定要退出吗？", " 提示", JOptionPane.OK_CANCEL_OPTION);
                if (JOptionPane.OK_OPTION == option) {
                    //点击了确定按钮
                    System.exit(0);
                }
            }
        });
        frame.setVisible(true);




        frame.preLoadData();


    }


    /**
     * 生成菜单
     */
    public void generateMenu() {



        JMenuBar menuBar;


        //Create the menu bar.
        menuBar = new JMenuBar();


        menuBar.add(createProduct());

        menuBar.add(createReport());

        menuBar.add(createTest());


        //System.exit(0);
        setJMenuBar(menuBar);





    }



    public JMenu createProduct()
    {

//Build the first menu.
        JMenu     menu = new JMenu("产品模块");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");


//a group of JMenuItems
        JMenuItem   menuItem = new JMenuItem("产品列表",
            KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_P, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "产品信息的模块");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main.this.setContentPane(new Panel_ProductList().getRoot());


            }
        });
        menu.addSeparator();

            menuItem = new JMenuItem("材料列表",
                KeyEvent.VK_M);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_M, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "产品信息的模块");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main.this.setContentPane(new Panel_Material("").getRoot());


            }
        });


//        menuItem = new JMenuItem("Both text and icon",
//                new ImageIcon("images/middle.gif"));
//        menuItem.setMnemonic(KeyEvent.VK_B);
//        menu.add(menuItem);
//
//        menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
//        menuItem.setMnemonic(KeyEvent.VK_D);
//        menu.add(menuItem);

//a group of radio button menu items
//        menu.addSeparator();
//        ButtonGroup group = new ButtonGroup();
//        JRadioButtonMenuItem   rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
//        rbMenuItem.setSelected(true);
//        rbMenuItem.setMnemonic(KeyEvent.VK_R);
//        group.add(rbMenuItem);
//        menu.add(rbMenuItem);

//        rbMenuItem = new JRadioButtonMenuItem("Another one");
//        rbMenuItem.setMnemonic(KeyEvent.VK_O);
//        group.add(rbMenuItem);
//        menu.add(rbMenuItem);

//a group of check box menu items

//        JCheckBoxMenuItem      cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
//        cbMenuItem.setMnemonic(KeyEvent.VK_C);
//        menu.add(cbMenuItem);
//
//        cbMenuItem = new JCheckBoxMenuItem("Another one");
//        cbMenuItem.setMnemonic(KeyEvent.VK_H);
//        menu.add(cbMenuItem);

//a submenu
//        menu.addSeparator();
//        JMenu  submenu = new JMenu("A submenu");
//        submenu.setMnemonic(KeyEvent.VK_S);

//        menuItem = new JMenuItem("An item in the submenu");
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_2, ActionEvent.ALT_MASK));
//        submenu.add(menuItem);
//
//        menuItem = new JMenuItem("打开材料搜索对话框");
//        submenu.add(menuItem);
//        menu.add(submenu);


//
//        menuItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });

        return menu;

    }




    private JMenu createReport()
    {


        //Build second menu in the menu bar.
        JMenu    menu = new JMenu("报价管理");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription(
                "This menu does nothing");



        //
        JMenuItem  menuItem = new JMenuItem("报价列表",
                KeyEvent.VK_B);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_B, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        menu.add(menuItem);


        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });



        return menu;


    }

    private JMenu createTest()
    {
        //Build second menu in the menu bar.
        JMenu  menu = new JMenu("功能测试");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription(
                "This menu does nothing");



        //
        JMenuItem  menuItem = new JMenuItem("上传图片",
                KeyEvent.VK_U);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_U, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        menu.add(menuItem);


        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageViewDialog dialog = new ImageViewDialog(Main.this);
                dialog.pack();
                dialog.setVisible(true);
            }
        });



        //
        menuItem = new JMenuItem("打开搜索材料对话框",
                KeyEvent.VK_U);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        menu.add(menuItem);


        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {









                SearchMaterialDialog dialog = new SearchMaterialDialog(Main.this,"",null);
                dialog.setTitle("搜索材料");

                dialog.pack();
                dialog.setVisible(true);
            }
        });


        return menu;
    }




    /**
     * 预先加载数据
     */
    private void preLoadData() {


        new HdSwingWorker<PClass, Object>(this,"数据预加载处理 请稍后。。。") {





            RemoteData<PackMaterialType> packMaterialTypeRemoteData;
            RemoteData<PackMaterialPosition> packMaterialPositionRemoteData;
            RemoteData<PackMaterialClass> packMaterialClassRemoteData;

            RemoteData<Pack> packRemoteData;


            RemoteData<MaterialType> materialTypeRemoteData;
            RemoteData<MaterialClass> materialClassRemoteData;
            RemoteData<MaterialEquation> materialEquationRemoteData;

            @Override
            protected RemoteData<PClass> doInBackground() throws Exception {

                packMaterialPositionRemoteData=apiManager.readPackMaterialPosition();

                packMaterialTypeRemoteData=apiManager.readPackMaterialType();

                packMaterialClassRemoteData  =apiManager.readPackMaterialClass();

                packRemoteData=apiManager.readPacks();


                materialTypeRemoteData=apiManager.readMaterialTypes();
                materialClassRemoteData=apiManager.readMaterialClasses();

                materialEquationRemoteData=   apiManager.readMaterialEquations();
                return apiManager.readProductClass();

            }

            @Override
            public void onResult(RemoteData<PClass> data) {


                BufferData.setPackMaterialPositions(packMaterialPositionRemoteData.datas
                );

                BufferData.setPackMaterialTypes(packMaterialTypeRemoteData.datas
                );
                BufferData.setPackMaterialClasses(packMaterialClassRemoteData.datas
                );

                BufferData.setPacks(packRemoteData.datas);
                BufferData.setPClasses(data.datas);



                BufferData.setMaterialClasses(materialClassRemoteData.datas);
                BufferData.setMaterialTypes(materialTypeRemoteData.datas);


                BufferData.setMaterialEquations(  materialEquationRemoteData.datas);

            }
        }.go();


    }
}