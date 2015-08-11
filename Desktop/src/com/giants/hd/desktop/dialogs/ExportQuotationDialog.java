package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.reports.ReportFactory;
import com.giants.hd.desktop.reports.excels.ExcelReportor;
import com.giants.hd.desktop.utils.ExportHelper;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.QuotationDetail;
import com.giants3.hd.utils.entity.QuotationFile;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * 导出报价单面板
 */
public class ExportQuotationDialog extends BaseDialog<QuotationDetail> {
    private JPanel contentPane;
    private JComboBox cb_model;
    private JTextField jtf_path;
    private JButton btn_file;
    private JButton btn_confirm;

    private static String lastSelectedPath="";

    @Inject
    ReportFactory reportFactory;


    private QuotationDetail detail;
    public ExportQuotationDialog(Window window,QuotationDetail detail) {
        super(window,"报价模板选择");
        this.detail=detail;
        setContentPane(contentPane);


        for(QuotationFile s: QuotationFile.defaultFiles)
        {
            cb_model.addItem(s.name);
        }

        jtf_path.setText(lastSelectedPath);


        btn_file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser(".");
                //下面这句是去掉显示所有文件这个过滤器。
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                /*
                    获得你选择的文件绝对路径
                */
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    jtf_path.setText(path);
                    lastSelectedPath=path;
                }
            }
        });


        btn_confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if(cb_model.getSelectedItem()==null)
                {
                    JOptionPane.showMessageDialog(ExportQuotationDialog.this,"请选择导出模板");
                        cb_model.requestFocus();
                    return;
                }
                if(StringUtils.isEmpty(jtf_path.getText().trim()) )
                {
                    JOptionPane.showMessageDialog(ExportQuotationDialog.this,"请选择导出路径");
                    jtf_path.requestFocus();
                    return;
                }




                doExportWork(cb_model.getSelectedItem().toString(),jtf_path.getText().trim());
            }
        });

    }

    /**
     * 导出数据模型
     * @param modelName
     * @param exportPath
     */
    private void doExportWork( final String modelName, final String exportPath) {


        new HdSwingWorker<Void,Void>(ExportQuotationDialog.this)
        {
            @Override
            protected RemoteData<Void> doInBackground() throws Exception {

            ExcelReportor reporter= reportFactory.createExcelReportor(modelName);
                reporter.report(detail, exportPath);

            //    ExportHelper.exportQuotation(detail, modelName, exportPath);

                return null;
            }

            @Override
            public void onResult(RemoteData<Void> data) {


                JOptionPane.showMessageDialog(ExportQuotationDialog.this,"导出成功");

                dispose();


            }
        }.go();

    }


}
