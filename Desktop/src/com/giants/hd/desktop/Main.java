package com.giants.hd.desktop;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.api.HttpUrl;
import com.giants.hd.desktop.dialogs.*;
import com.giants.hd.desktop.local.BufferData;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.PropertyWorker;
import com.giants.hd.desktop.view.Panel_Material;
import com.giants.hd.desktop.view.Panel_ProductList;
import com.giants.hd.desktop.view.Panel_Quotation;
import com.giants.hd.desktop.widget.BackgroundPainter;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Guice;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by davidleen29 on 2015/5/6.
 */
public class Main extends JFrame {
    private JPanel panel1;
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
        super("报价系统");






        String baseUrl=PropertyWorker.readData("BaseUrl");

        if(null!=baseUrl)
        HttpUrl.iniBaseUrl(baseUrl );



        Guice.createInjector().injectMembers(this);
    }

    public static void main(String[] args) {



        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                initLookAndFeel();
                init();

            }
        });








    }


    /**
     * 配置界面的样式
     */
    private static void initLookAndFeel()
    {


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


        //设置分页标签选中时候颜色为红色

        //        UIManager.getLookAndFeelDefaults().put("TabbedPane:TabbedPaneTab[Enabled].backgroundPainter", new BackgroundPainter(Color.white));
//        UIManager.getLookAndFeelDefaults().put("TabbedPane:TabbedPaneTab[Enabled+MouseOver].backgroundPainter", new BackgroundPainter(Color.white));
//        UIManager.getLookAndFeelDefaults().put("TabbedPane:TabbedPaneTab[Enabled+Pressed].backgroundPainter", new BackgroundPainter(Color.white));
        UIManager.getLookAndFeelDefaults().put("TabbedPane:TabbedPaneTab[Focused+MouseOver+Selected].backgroundPainter", new BackgroundPainter(Color.RED));
        UIManager.getLookAndFeelDefaults().put("TabbedPane:TabbedPaneTab[Focused+Pressed+Selected].backgroundPainter", new BackgroundPainter(Color.RED));
        UIManager.getLookAndFeelDefaults().put("TabbedPane:TabbedPaneTab[Focused+Selected].backgroundPainter", new BackgroundPainter(Color.RED));
        UIManager.getLookAndFeelDefaults().put("TabbedPane:TabbedPaneTab[MouseOver+Selected].backgroundPainter", new BackgroundPainter(Color.RED));
        UIManager.getLookAndFeelDefaults().put("TabbedPane:TabbedPaneTab[Pressed+Selected].backgroundPainter", new BackgroundPainter(Color.RED));
        UIManager.getLookAndFeelDefaults().put("TabbedPane:TabbedPaneTab[Selected].backgroundPainter", new BackgroundPainter(Color.RED));
    }


    /**
     * 应用程序初始化
     */
    private static final void  init()
    {
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

        frame.pack();
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

        menuBar.add(createBaseData());

        menuBar.add(createAuthority());
        menuBar.add(createSysetm());


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

        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main.this.setContentPane(new Panel_Material("").getRoot());


            }
        });






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

                setContentPane(new Panel_Quotation().getRoot());

            }
        });



        return menu;


    }


    /**
     * 添加测试菜单
     * @return
     */
    private JMenu createBaseData()
    {
        //Build second menu in the menu bar.
        JMenu  menu = new JMenu("基础数据");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription(
                "This menu does nothing");



        //
        JMenuItem  menuItem = new JMenuItem("材质类型列表"
                );

        menu.add(menuItem);


        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MaterialClassDialog dialog = new MaterialClassDialog(Main.this);
                dialog.setLocationRelativeTo(getRootPane());
                dialog.setVisible(true);
            }
        });

        //
           menuItem = new JMenuItem("客户列表"        );

        menu.add(menuItem);

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerDialog dialog = new CustomerDialog(Main.this);
                dialog.setLocationRelativeTo(getRootPane());
                dialog.setVisible(true);
            }
        });



        menu.addSeparator();

        menuItem = new JMenuItem("工序列表" );
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

//                Main.this.setContentPane(new Panel_BaseData().getRoot());
                ProductProcessDialog dialog=new ProductProcessDialog(Main.this);
                dialog.setLocationRelativeTo(getRootPane());

                dialog.setVisible(true);

            }
        });


        menu.addSeparator();

        menuItem = new JMenuItem("业务员列表" );
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                SalesmanDialog dialog=new SalesmanDialog(Main.this);
                dialog.setLocationRelativeTo(getRootPane());
                dialog.setVisible(true);

            }
        });




        return menu;
    }


    /**
     * 权限菜单
     * @return
     */
    public  JMenu createAuthority()
    {
        //Build second menu in the menu bar.
        JMenu  menu = new JMenu("权限管理");




        //
        JMenuItem  menuItem = new JMenuItem("用户列表" );


        menu.add(menuItem);


        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserDialog dialog = new UserDialog(Main.this);
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        //
        menuItem = new JMenuItem("模块列表" );

        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModuleDialog dialog = new ModuleDialog(Main.this);
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        //
        menuItem = new JMenuItem("权限设置" );

        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AuthorityDialog dialog = new AuthorityDialog(Main.this);
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        return menu;

    }

    /**
     * 添加测试菜单
     * @return
     */
    private JMenu createSysetm()
    {
        //Build second menu in the menu bar.
        JMenu  menu = new JMenu("系统功能");
        menu.setMnemonic(KeyEvent.VK_S);
        menu.getAccessibleContext().setAccessibleDescription(
                "This menu does nothing");



        //
        JMenuItem  menuItem = new JMenuItem("数据同步" );

        menuItem.getAccessibleContext().setAccessibleDescription(
                "当图片不能正常显示时候执行");
        menu.add(menuItem);


        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SyncDialog dialog = new SyncDialog(Main.this);
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        //
        menuItem = new JMenuItem("图片批量上传" );

        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UploadPictureDialog dialog = new UploadPictureDialog(Main.this);
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


            RemoteData<Customer> customerRemoteData;


            RemoteData<Salesman> salesmanRemoteData;


            @Override
            protected RemoteData<PClass> doInBackground() throws Exception {

                packMaterialPositionRemoteData=apiManager.readPackMaterialPosition();

                packMaterialTypeRemoteData=apiManager.readPackMaterialType();

                packMaterialClassRemoteData  =apiManager.readPackMaterialClass();

                packRemoteData=apiManager.readPacks();


                materialTypeRemoteData=apiManager.readMaterialTypes();
                materialClassRemoteData=apiManager.readMaterialClasses();

                materialEquationRemoteData=   apiManager.readMaterialEquations();
                salesmanRemoteData=apiManager.readSalesmans();
                customerRemoteData=apiManager.readCustomers();

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


                BufferData.setMaterialEquations(materialEquationRemoteData.datas);

                BufferData.setCustomers(  customerRemoteData.datas);

                BufferData.setSalesmans(  salesmanRemoteData.datas);


            }




           @Override
            public void onHandleError(HdException exception)
            {



                JOptionPane.showMessageDialog(Main.this,"数据初始化失败，请检查网络，重新打开。");
                System.exit(0);



            }
        }.go();


    }
}
