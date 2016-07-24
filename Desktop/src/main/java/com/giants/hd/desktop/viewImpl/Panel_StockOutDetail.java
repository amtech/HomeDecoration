package com.giants.hd.desktop.viewImpl;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.filters.PictureFileFilter;
import com.giants.hd.desktop.frames.StockOutDetailFrame;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.StockOutGuihaoModel;
import com.giants.hd.desktop.model.StockOutItemTableModel;
import com.giants.hd.desktop.presenter.StockOutDetailPresenter;
import com.giants.hd.desktop.utils.JTableUtils;
import com.giants.hd.desktop.view.StockOutDetailViewer;
import com.giants.hd.desktop.widget.AttachPanel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants.hd.desktop.widget.QuotationItemPopMenu;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.PackMaterialType;
import com.giants3.hd.utils.entity_erp.ErpStockOutItem;
import com.giants3.hd.utils.noEntity.ErpStockOutDetail;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 出库界面详情界面
 * Created by davidleen29 on 2016/7/14.
 */
public class Panel_StockOutDetail extends BasePanel implements StockOutDetailViewer {
    private JButton btn_addPicture;
    private JPanel root;
    private JHdTable tb;
    private JTextField tf_ck_no;
    private JTextField tf_cus;
    private JTextField tf_dd;
    private JTextField tf_mdg;
    private JTextField tf_tdh;
    private JTextField tf_gsgx;
    private JScrollPane scroll;
    private AttachPanel panel_attach;
    private JTextArea ta_memo;
    private JTextArea ta_neihemai;
    private JTextArea ta_cemai;
    private JTextArea ta_zhengmai;
    private JButton save;
    private JHdTable tb_guihao;
    private JTextField tf_guihao;
    private JTextField tf_fengqian;
    private JButton btn_addgui;
    private StockOutDetailPresenter presenter;

    private StockOutItemTableModel tableModel;

    private StockOutGuihaoModel guihaoModel;

    public Panel_StockOutDetail(StockOutDetailPresenter presenter) {
        this.presenter = presenter;

        init();
    }

    private void init() {

        tableModel = new StockOutItemTableModel();

        tb.setModel(tableModel);


        tb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {

                    int column = tb.convertColumnIndexToModel(tb.getSelectedColumn());
                    int selectRow = tb.convertRowIndexToModel(tb.getSelectedRow());
                    //单击第一列 显示原图
                    if (column == 0) {


                        ErpStockOutItem item = tableModel.getItem(selectRow);

                        if (!StringUtils.isEmpty(item.prd_no)) {
                            ImageViewDialog.showProductDialog(getWindow(getRoot()), item.prd_no, item.pVersion, item.url);
                        }
                    }


                }
            }

            ;

        });


        guihaoModel = new StockOutGuihaoModel();
        tb_guihao.setModel(guihaoModel);
        //柜号删除处理
        MouseAdapter adapter = new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                showMenu(e);

            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseReleased(e);
                showMenu(e);

            }

            private void showMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JTable source = (JTable) e.getSource();

                    JPopupMenu menu = new JPopupMenu();

                    final JMenuItem delete = new JMenuItem("删除");
                    menu.add(delete);
                    delete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int tableRow[] = JTableUtils.getSelectedRowSOnModel(tb_guihao);
                            if (tableRow.length == 0) {
                                JOptionPane.showMessageDialog(tb_guihao, "请选择行进行删除。");
                                return;
                            }
                            StockOutDetailFrame.GuiInfo guiInfo = guihaoModel.getItem(tableRow[0]);

                            presenter.removeGuiInfo(guiInfo);

                        }
                    });


                    menu.show(e.getComponent(), e.getX(), e.getY());

                }
            }
        };

        tb_guihao.addMouseListener(adapter);


        btn_addPicture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                JFileChooser fileChooser = new JFileChooser(".");
                //下面这句是去掉显示所有文件这个过滤器。
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.addChoosableFileFilter(new PictureFileFilter());
                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    presenter.addPictureClick(fileChooser.getSelectedFiles());
                }


            }
        });


        //保存
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presenter.save();


            }
        });


        //图片的摆放布局
        panel_attach.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel_attach.setMaxSize(80);

        panel_attach.setListener(new AttachPanel.OnAttachFileDeleteListener() {
            @Override
            public void onDelete(int index, String url) {

                presenter.onDeleteAttach(url);
            }
        });

        //添加柜号数据
        btn_addgui.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String guihao = tf_guihao.getText();
                String fengqian = tf_fengqian.getText().trim();

                if (StringUtils.isEmpty(guihao) || StringUtils.isEmpty(fengqian)) {
                    JOptionPane.showMessageDialog(getRoot(), "请输入柜号封签号");
                    return;
                }


                presenter.addGuiInfo(guihao, fengqian);

            }
        });
    }

    /**
     * 侧唛数据修改监听
     */
    private DocumentListener cemaiDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            presenter.onCemaiChange(ta_cemai.getText().trim());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            presenter.onCemaiChange(ta_cemai.getText().trim());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            presenter.onCemaiChange(ta_cemai.getText().trim());

        }
    };

    /**
     * 内盒唛数据修改监听
     */
    private DocumentListener neihemaiDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            presenter.onNeihemaiChange(ta_neihemai.getText().trim());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            presenter.onNeihemaiChange(ta_neihemai.getText().trim());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            presenter.onNeihemaiChange(ta_neihemai.getText().trim());

        }
    };


    /**
     * 正唛数据修改监听
     */
    private DocumentListener zhengmaiDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            presenter.onZhengmaiChange(ta_zhengmai.getText().trim());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            presenter.onZhengmaiChange(ta_zhengmai.getText().trim());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            presenter.onZhengmaiChange(ta_zhengmai.getText().trim());

        }
    };


    /**
     * 备注数据修改监听
     */
    private DocumentListener memoDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            presenter.onMemoChange(ta_memo.getText().trim());

        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            presenter.onMemoChange(ta_memo.getText().trim());

        }

        @Override
        public void changedUpdate(DocumentEvent e) {

            presenter.onMemoChange(ta_memo.getText().trim());

        }
    };


    @Override
    public JComponent getRoot() {
        return root;
    }


    @Override
    public void setStockOutDetail(ErpStockOutDetail erpStockOutDetail) {


        tf_ck_no.setText(erpStockOutDetail.erpStockOut.ck_no);

        tf_dd.setText(erpStockOutDetail.erpStockOut.ck_dd);
        tf_cus.setText(erpStockOutDetail.erpStockOut.cus_no);
        tf_gsgx.setText(erpStockOutDetail.erpStockOut.gsgx);
        tf_mdg.setText(erpStockOutDetail.erpStockOut.mdg);
        tf_tdh.setText(erpStockOutDetail.erpStockOut.tdh);
        tableModel.setDatas(erpStockOutDetail.items);


        ta_cemai.getDocument().removeDocumentListener(cemaiDocumentListener);
        ta_neihemai.getDocument().removeDocumentListener(neihemaiDocumentListener);
        ta_zhengmai.getDocument().removeDocumentListener(zhengmaiDocumentListener);
        ta_memo.getDocument().removeDocumentListener(memoDocumentListener);

        ta_cemai.setText(erpStockOutDetail.erpStockOut.cemai);
        ta_zhengmai.setText(erpStockOutDetail.erpStockOut.zhengmai);
        ta_neihemai.setText(erpStockOutDetail.erpStockOut.neheimai);
        ta_memo.setText(erpStockOutDetail.erpStockOut.memo);


        ta_cemai.getDocument().addDocumentListener(cemaiDocumentListener);
        ta_neihemai.getDocument().addDocumentListener(neihemaiDocumentListener);
        ta_zhengmai.getDocument().addDocumentListener(zhengmaiDocumentListener);
        ta_memo.getDocument().addDocumentListener(memoDocumentListener);


    }

    @Override
    public void showAttachFiles(List<String> attachStrings) {

        String[] urls = new String[attachStrings.size()];
        attachStrings.toArray(urls);
        panel_attach.setAttachFiles(urls);
        // btn_addPicture.setVisible(attachStrings.size()< 8);


    }


    @Override
    public void showGuihaoData(Set<StockOutDetailFrame.GuiInfo> guiInfos) {

        tf_guihao.setText("");
        tf_fengqian.setText("");
        List<StockOutDetailFrame.GuiInfo> guiInfoList = new ArrayList<>();
        guiInfoList.addAll(guiInfos);
        guihaoModel.setDatas(guiInfoList);


        //设置出库表格柜号处理数据
        JComboBox<StockOutDetailFrame.GuiInfo> guiInfoJComboBox = new JComboBox<>();
        guiInfoJComboBox.addItem(new StockOutDetailFrame.GuiInfo("",""));
        for (StockOutDetailFrame.GuiInfo guiInfo : guiInfoList)
            guiInfoJComboBox.addItem(guiInfo);
        DefaultCellEditor comboboxEditor = new DefaultCellEditor(guiInfoJComboBox);

        tb.setDefaultEditor(StockOutDetailFrame.GuiInfo.class, comboboxEditor);

    }
}
