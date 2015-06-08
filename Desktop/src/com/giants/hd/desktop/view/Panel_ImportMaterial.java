package com.giants.hd.desktop.view;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.pools.ObjectPool;
import com.giants3.hd.utils.pools.PoolCenter;
import com.google.inject.Inject;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 从excel导入材料
 */
public class Panel_ImportMaterial extends BasePanel {
    private JTextField jtf_file;
    private JTextField jtf_sheet;
    private JFormattedTextField jtf_row;
    private JButton btn_file;
    private JButton btn_import;
    private JPanel panel1;




    @Inject
    ApiManager apiManager;

    @Override
    public JComponent getRoot() {
        return panel1;
    }


    public Panel_ImportMaterial() {

        btn_import.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

               final String path = jtf_file.getText().toString().trim();

                if (StringUtils.isEmpty(path)) {

                    JOptionPane.showMessageDialog((Component) e.getSource(), "请选择Excel文件!");
                }
                new HdSwingWorker<Void, Long>(getWindow((Component) e.getSource())) {
                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {
                        readExcel(path, jtf_sheet.getText().toString().trim(), Integer.valueOf(jtf_row.getText().toString().trim()));
                        return null;
                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {


                        JOptionPane.showMessageDialog((Component) e.getSource(), "导入材料成功!");




                    }
                }.go();



            }
        });


        btn_file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                JFileChooser fileChooser = new JFileChooser(".");
                //下面这句是去掉显示所有文件这个过滤器。
                fileChooser.setAcceptAllFileFilterUsed(false);
//添加excel文件的过滤器
                fileChooser.addChoosableFileFilter(new ExcelFileFilter("xls"));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                /*
                    获得你选择的文件绝对路径
                */
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    jtf_file.setText(path);

                }
            }
        });

    }

    private static class ExcelFileFilter extends FileFilter {

        String ext;

        //构造方法的参数是我们需要过滤的文件类型。比如excel文件就是 xls,exe文件是exe.

        ExcelFileFilter(String ext) {

            this.ext = ext;
        }

        public boolean accept(File file) {

            //首先判断该目录下的某个文件是否是目录，如果是目录则返回true，即可以显示在目录下。

            if (file.isDirectory()) {
                return true;
            }


            String fileName = file.getName();
            int index = fileName.lastIndexOf('.');

            if (index > 0 && index < fileName.length() - 1) {
                String extension = fileName.substring(index + 1).toLowerCase();
                if (extension.equals(ext))
                    return true;
            }
            return false;
        }


      /*
      *
      * 这个方法也是重写FileFilter的方法，作用是在过滤名那里显示出相关的信息。这个与我们过滤的文件类型想匹配，通过这些信息，可以让用户更清晰的明白需要过滤什么类型的文件。
      */

        public String getDescription() {

            if (ext.equals("xls")) {
                return "Microsoft Excel文件(*.xls)";
            }

            return "";
        }
    }






    /**
     * 读取excel 值 更新材料表
     */
    private void readExcel(String path, String sheetName, int firstRow) {


        try

        {

            InputStream is = new FileInputStream(path);

            Workbook rwb = Workbook.getWorkbook(is);

            //这里有两种方法获取sheet表:名字和下标（从0开始）

            Sheet st = rwb.getSheet(sheetName);

            int rsColumns = st.getColumns();
            int rsRows = st.getRows();


            List<Material> materials=new ArrayList<>();
            int batchSize=100;
            ObjectPool<Material> materialObjectPool= PoolCenter.getObjectPool(Material.class,batchSize);
            Material material;

            for (int i = firstRow; i < rsRows; i++) {



                Cell cell = st.getCell(0, i);
                String value = cell.getContents();

                if(StringUtils.isEmpty(value))
                {
                    //如果编码为空 直接跳过
                    continue;

                }
                material = materialObjectPool.newObject();

                //复制值。
                material.code = value;
                material.name = st.getCell(1, i).getContents();
                try {
                    material.wLong =FloatHelper.scale(Float.valueOf(st.getCell(2, i).getContents()));
                } catch (Throwable t) {
                    material.wLong=0;
                }
                try {
                    material.wWidth = FloatHelper.scale(Float.valueOf(st.getCell(3, i).getContents()));
                } catch (Throwable t) {
                    material.wWidth=0;
                }
                try {
                    material.wHeight = FloatHelper.scale(Float.valueOf(st.getCell(4, i).getContents()));
                } catch (Throwable t) {
                    material.wHeight=0;
                }

                try {
                    material.available = FloatHelper.scale(Float.valueOf(st.getCell(5, i).getContents())) ;
                } catch (Throwable t) {
                    material.available=0;
                }

                try {
                    material.discount = FloatHelper.scale(Float.valueOf(st.getCell(6, i).getContents()));
                } catch (Throwable t) {
                    material.discount=0;
                }

                try {
                    material.price = FloatHelper.scale(Float.valueOf(st.getCell(7, i).getContents()),5);
                } catch (Throwable t) {
                    material.price=0;
                }

                try {
                    material.typeId = Integer.valueOf(st.getCell(8, i).getContents());
                } catch (Throwable t) {
                    material.typeId=0;
                }

                material.typeName = String.valueOf(material.typeId);

                material.classId = st.getCell(9, i).getContents();

                material.spec = st.getCell(10, i).getContents();

                material.unitName = st.getCell(11, i).getContents();

                material.className = st.getCell(12, i).getContents();




                materials.add(material);
                int size=materials.size();

                //如果是大于100  或者已经最后一行了 提交
                if(size>=batchSize||i ==rsRows-1)
                {


                    apiManager.saveMaterials(materials);


                    for (int j = 0; j < size; j++) {

                        Material temp=materials.get(j);
                        materialObjectPool.release(temp);
                    }

                    materials.clear();


                }

            }





            //Sheet st = rwb.getSheet(0);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HdException e) {
            e.printStackTrace();
        }

    }
}
