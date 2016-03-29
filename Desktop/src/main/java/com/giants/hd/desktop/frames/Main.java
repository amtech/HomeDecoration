package com.giants.hd.desktop.frames;

import com.giants.hd.desktop.MyDesktopManager;
import com.giants.hd.desktop.local.ImageLoader;
import com.giants.hd.desktop.local.LocalFileHelper;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.domain.api.HttpUrl;


import com.giants.hd.desktop.dialogs.*;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.PropertyWorker;
import com.giants.hd.desktop.utils.AuthorityUtil;
import com.giants.hd.desktop.utils.ShortcutHelper;
import com.giants.hd.desktop.widget.BackgroundPainter;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.*;
import com.giants3.hd.utils.exception.HdException;

import com.giants3.hd.utils.noEntity.BufferData;
import com.google.inject.Guice;
import com.google.inject.Inject;
//import org.apache.commons.logging.Log;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by davidleen29 on 2015/5/6.
 */
public class Main extends BaseFrame {
    JDesktopPane desktop;


    public static final String TITLE = "报价系统";


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


    public Main() {
        super(TITLE);


        //Set up the GUI.
        desktop = new JDesktopPane(); //a specialized layered pane
        desktop.setDesktopManager(new MyDesktopManager(desktop));


        setContentPane(desktop);


        //Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);

        ShortcutHelper.createShortcut();


        String baseUrl = PropertyWorker.readData("BaseUrl");

        if (null != baseUrl)
            HttpUrl.iniBaseUrl(baseUrl);


        int version = PropertyWorker.getVersion().versionCode;
        HttpUrl.setVersionCode(version);


        Guice.createInjector().injectMembers(this);
    }

    public static void main(String[] args) {


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {

                LocalFileHelper.printThrowable(e);
                e.printStackTrace();
                System.exit(1);
            }
        });
        //throw new RuntimeException("aaaaaaaaaaaaaaaa");

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
    private static void initLookAndFeel() {


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
        UIManager.getLookAndFeelDefaults().put("TabbedPane:TabbedPaneTab[Selected].backgroundPainter", new BackgroundPainter(Color.RED));
        UIManager.getLookAndFeelDefaults().put("TableHeader.cellBorder", BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }


    /**
     * 应用程序初始化
     */
    private static final void init() {
        final Main frame = new Main();
        ;


        frame.setExtendedState(MAXIMIZED_BOTH);
        //  frame.generateMenu();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);


        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //点击了退出系统按钮
                int option = JOptionPane.showConfirmDialog(frame, "确定要退出吗？", " 提示", JOptionPane.OK_CANCEL_OPTION);
                if (JOptionPane.OK_OPTION == option) {
                    //点击了确定按钮
                    frame.dispose();
                    System.exit(0);
                }
            }
        });

        frame.pack();
        frame.setVisible(true);


        //ask for login

        LoginDialog dialog = new LoginDialog(frame);
        dialog.setModal(true);
        dialog.setVisible(true);

        if (dialog.getResult() == null) {
            System.exit(0);
        } else {
            frame.preLoadData(dialog.getResult());

        }


        sayHello();


    }


    private static void sayHello() {
//      Log logger=  MyLogger.Logger(Main.class);
//        logger.info("hello");
//        logger.warn("testtesttetstt");


    }

    /**
     * 生成菜单
     */
    public void generateMenu() {


        JMenuBar menuBar;


        //Create the menu bar.


        menuBar = new JMenuBar();


        if (AuthorityUtil.getInstance().viewProductModule())
            menuBar.add(createProduct());


        if (AuthorityUtil.getInstance().viewQuotationModule())
            menuBar.add(createReport());


        if (AuthorityUtil.getInstance().viewOrderMenu())
            menuBar.add(createOrder());


        if (AuthorityUtil.getInstance().viewBaseDataModule())
            menuBar.add(createBaseData());


        if (AuthorityUtil.getInstance().viewAuthorityModule())
            menuBar.add(createAuthority());

        if (AuthorityUtil.getInstance().viewSystemModule())
            menuBar.add(createSysetm());


        menuBar.add(createPersonal());

        menuBar.add(createHelp());

        //System.exit(0);
        setJMenuBar(menuBar);


    }


    public JMenu createProduct() {

        JMenu menu;


        menu = new JMenu("产品模块");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");


//a group of JMenuItems
        JMenuItem menuItem;

        if (AuthorityUtil.getInstance().viewProductList()) {
            menuItem = new JMenuItem("产品列表",
                    KeyEvent.VK_P);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_P, ActionEvent.ALT_MASK));

            menu.add(menuItem);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    ProductListInternalFrame productListInternalFrame = new ProductListInternalFrame();

                    productListInternalFrame.setVisible(true); //necessary as of 1.3
                    desktop.add(productListInternalFrame);


                    try {
                        productListInternalFrame.setSelected(true);
                    } catch (java.beans.PropertyVetoException exception) {
                    }
//                    desktop.add()
//                    Main.this.setContentPane(new Panel_ProductList().getRoot());


                }
            });


            //   menu.addSeparator();

        }


        if (AuthorityUtil.getInstance().viewProductPicture()) {
            menuItem = new JMenuItem("产品图片");
            menu.add(menuItem);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    // Main.this.setContentPane(new Panel_Material("").getRoot());
                    ProductPictureDialog dialog = new ProductPictureDialog(Main.this);
                    dialog.setLocationRelativeTo(getRootPane());
                    dialog.setVisible(true);

                }
            });

        }


        if (AuthorityUtil.getInstance().viewMaterialList()) {
            menuItem = new JMenuItem("材料列表",
                    KeyEvent.VK_M);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_M, ActionEvent.ALT_MASK));

            menu.add(menuItem);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    MaterialListInternalFrame materialListInternalFrame = new MaterialListInternalFrame();

                    materialListInternalFrame.setVisible(true); //necessary as of 1.3
                    desktop.add(materialListInternalFrame);
                    try {
                        materialListInternalFrame.setSelected(true);
                    } catch (java.beans.PropertyVetoException exception) {
                    }
                    // Main.this.setContentPane(new Panel_Material("").getRoot());


                }
            });

        }


        if (AuthorityUtil.getInstance().viewMaterialPicture()) {
            menuItem = new JMenuItem(Module.TITLE_MATERIAL_PICTURE);

            menu.add(menuItem);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    MaterialPictureDialog dialog = new MaterialPictureDialog(Main.this);
                    dialog.setLocationRelativeTo(getRootPane());
                    dialog.setVisible(true);

                }
            });

        }


        if (AuthorityUtil.getInstance().viewProductDelete()) {
            menuItem = new JMenuItem(Module.TITLE_PRODUCT_DELETE);

            menu.add(menuItem);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    ProductDeleteDialog dialog = new ProductDeleteDialog(Main.this);
                    dialog.setLocationRelativeTo(getRootPane());
                    dialog.setVisible(true);

                }
            });

        }


        if (AuthorityUtil.getInstance().viewProductReport()) {
            menuItem = new JMenuItem(Module.TITLE_PRODUCT_REPORT);

            menu.add(menuItem);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    ProductReportDialog dialog = new ProductReportDialog(Main.this);
//                    dialog.setLocationRelativeTo(getRootPane());
                    dialog.setVisible(true);


                }
            });

        }

        return menu;

    }


    /**
     * 报价模块
     *
     * @return
     */

    private JMenu createReport() {


        JMenu menu = new JMenu("报价管理");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription(
                "This menu does nothing");


        if (AuthorityUtil.getInstance().viewQuotationList()) {
            JMenuItem menuItem = new JMenuItem("报价列表"
            );

            menu.add(menuItem);


            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    QuotationInternalFrame quotationInternalFrame = new QuotationInternalFrame();

                    quotationInternalFrame.setVisible(true); //necessary as of 1.3
                    desktop.add(quotationInternalFrame);
                    try {
                        quotationInternalFrame.setSelected(true);
                    } catch (java.beans.PropertyVetoException exception) {
                    }

                    // setContentPane(new Panel_Quotation().getRoot());

                }
            });
        }


        if (AuthorityUtil.getInstance().viewQuotationDeleteList()) {
            JMenuItem menuItem = new JMenuItem(Module.TITLE_QUOTATION_DELETE);

            menu.add(menuItem);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    QuotationDeleteDialog dialog = new QuotationDeleteDialog(Main.this);
                    dialog.setLocationRelativeTo(getRootPane());
                    dialog.setVisible(true);

                }
            });
        }


        return menu;


    }


    /**
     * 添加测试菜单
     *
     * @return
     */
    private JMenu createBaseData() {

        JMenu menu = new JMenu("基础数据");

        JMenuItem menuItem;

        if (AuthorityUtil.getInstance().viewMaterialClassList()) {
            menuItem = new JMenuItem("材质类型列表"
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
        }

        if (AuthorityUtil.getInstance().viewCustomerList()) {
            menuItem = new JMenuItem("客户列表");

            menu.add(menuItem);

            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CustomerDialog dialog = new CustomerDialog(Main.this);
                    dialog.setLocationRelativeTo(getRootPane());
                    dialog.setVisible(true);
                }
            });

        }


        if (AuthorityUtil.getInstance().viewProcessList()) {

            menuItem = new JMenuItem("工序列表");
            menu.add(menuItem);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

//                Main.this.setContentPane(new Panel_BaseData().getRoot());
                    ProductProcessDialog dialog = new ProductProcessDialog(Main.this);
                    dialog.setLocationRelativeTo(getRootPane());

                    dialog.setVisible(true);

                }
            });

        }

        if (AuthorityUtil.getInstance().viewPackMaterialClassList()) {
            menuItem = new JMenuItem(Module.TITLE_PACK_MATERIAL_CLASS
            );

            menu.add(menuItem);


            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PackMaterialClassDialog dialog = new PackMaterialClassDialog(Main.this);
                    dialog.setLocationRelativeTo(getRootPane());
                    dialog.setVisible(true);
                }
            });
        }


//       // if(AuthorityUtil.getInstance().viewPackMaterialClassList()) {
//            menuItem = new JMenuItem(Module.TITLE_PACK_MATERIAL_TEMPLATE
//            );
//            menu.add(menuItem);
//
//
//            menuItem.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    PackMaterialTemplateDialog dialog = new PackMaterialTemplateDialog(Main.this);
//                    dialog.setLocationRelativeTo(getRootPane());
//                    dialog.setVisible(true);
//                }
//            });
//      //   }

        return menu;
    }


    /**
     * 权限菜单
     *
     * @return
     */
    public JMenu createAuthority() {
        //Build second menu in the menu bar.
        JMenu menu = new JMenu("权限管理");


        //
        JMenuItem menuItem = new JMenuItem("用户列表");


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
        menuItem = new JMenuItem("模块列表");

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
        menuItem = new JMenuItem("权限设置");

        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AuthorityDialog dialog = new AuthorityDialog(Main.this);
                dialog.pack();
                dialog.setVisible(true);
            }
        });


        //
        menuItem = new JMenuItem("报价细分权限");

        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuoteAuthDialog dialog = new QuoteAuthDialog(Main.this);
                dialog.pack();
                dialog.setVisible(true);
            }
        });


        return menu;

    }

    /**
     * 订单菜单
     *
     * @return
     */
    public JMenu createOrder() {

        JMenu menu = new JMenu(Module.TITLE_ORDER);
        //
        JMenuItem menuItem = new JMenuItem("订单列表");
        menu.add(menuItem);


        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {




                OrderListInternalFrame productListInternalFrame = new OrderListInternalFrame();

                productListInternalFrame.setVisible(true); //necessary as of 1.3
                desktop.add(productListInternalFrame);


                try {
                    productListInternalFrame.setSelected(true);
                } catch (java.beans.PropertyVetoException exception) {
                }
            }
        });


        return menu;

    }

    /**
     * 添加测试菜单
     *
     * @return
     */
    private JMenu createSysetm() {
        //Build second menu in the menu bar.
        JMenu menu = new JMenu("系统功能");


        JMenuItem menuItem;


        if (AuthorityUtil.getInstance().viewSyncData()) {
            menuItem = new JMenuItem(Module.TITLE_SYNC_DATA);

            menu.add(menuItem);


            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SyncDialog dialog = new SyncDialog(Main.this);
                    dialog.pack();
                    dialog.setVisible(true);
                }
            });
        }


        if (AuthorityUtil.getInstance().viewSysParam()) {
            menuItem = new JMenuItem(Module.TITLE_SYS_PARAM);
            menu.add(menuItem);


            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SysParamDialog dialog = new SysParamDialog(Main.this);
                    dialog.pack();
                    dialog.setVisible(true);
                }
            });


        }


        //任务定时面板

        if (AuthorityUtil.getInstance().viewTaskManage()) {
            menuItem = new JMenuItem(Module.TITLE_TASK_MANAGE);
            menu.add(menuItem);


            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    TaskListInternalFrame taskListInternalFrame = new TaskListInternalFrame();

                    taskListInternalFrame.setVisible(true); //necessary as of 1.3
                    desktop.add(taskListInternalFrame);
                    try {
                        taskListInternalFrame.setSelected(true);
                    } catch (java.beans.PropertyVetoException exception) {
                    }

                    // setContentPane(new Panel_Quotation().getRoot());

                }
            });
        }


//        menuItem = new JMenuItem("咸康数据调整");
//        menu.add(menuItem);
//        menuItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//
//
//           final     LoadingDialog     dialog=new LoadingDialog(Main.this, new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//
//                    }
//                });
//
//
//                UseCaseFactory.createUpdateXiankang().execute(new Subscriber() {
//                     @Override
//                     public void onCompleted() {
//                         dialog.setVisible(false);
//                         dialog.dispose();
//                         JOptionPane.showMessageDialog(Main.this,"调整成功");
//
//
//                     }
//
//                     @Override
//                     public void onError(Throwable e) {
//                         dialog.setVisible(false);
//                         dialog.dispose();
//                         JOptionPane.showMessageDialog(Main.this,e.getMessage());
//                     }
//
//                     @Override
//                     public void onNext(Object o) {
//
//                     }
//                 });
//
//                dialog.setVisible(true);
//            }
//        });

        return menu;
    }


    /**
     * 个人设置模块
     */


    private JMenu createPersonal() {


        JMenu menu = new JMenu("个人模块");

        //
        JMenuItem menuItem = new JMenuItem("修改密码"
        );

        menu.add(menuItem);


        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                UpdatePasswordDialog dialog = new UpdatePasswordDialog(Main.this);
                dialog.pack();
                dialog.setVisible(true);


            }
        });


        return menu;


    }


    /**
     * 帮助菜单
     */


    private JMenu createHelp() {


        JMenu menu = new JMenu("帮助");

        //
        JMenuItem menuItem = new JMenuItem("检查更新"
        );

        menu.add(menuItem);


        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                UpgradeDialog dialog = new UpgradeDialog(Main.this);
                dialog.pack();
                dialog.setVisible(true);


            }
        });


        return menu;


    }

    /**
     * 预先加载数据
     */
    private void preLoadData(final User user) {


        new HdSwingWorker<BufferData, Object>(this, "数据预加载处理 请稍后。。。") {


            @Override
            protected RemoteData<BufferData> doInBackground() throws Exception {


                return apiManager.loadInitData(user);

            }

            @Override
            public void onResult(RemoteData<BufferData> data) {


                CacheManager.getInstance().bufferData = data.datas.get(0);
                CacheManager.getInstance().bufferData.loginUser = user;
                generateMenu();
                AppVersion currentVersion = PropertyWorker.getVersion();
                setTitle(TITLE + "       (" + user.toString() + ")                     " + "当前版本" + currentVersion.versionName + "(" + currentVersion.versionCode + ")");


            }


            @Override
            public void onHandleError(HdException exception) {

                JOptionPane.showMessageDialog(Main.this, "数据初始化失败，请检查网络，重新打开。" + exception.getLocalizedMessage());
                System.exit(0);

            }
        }.go();


    }


    @Override
    public void dispose() {


        ImageLoader.getInstance().close();

        super.dispose();

    }


}
