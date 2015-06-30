package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.api.ApiManager;
import com.giants.hd.desktop.filters.ExcelFileFilter;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.model.MaterialClassTableModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.ObjectUtils;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.Material;
import com.giants3.hd.utils.entity.MaterialClass;
import com.giants3.hd.utils.entity.ProductProcess;
import com.giants3.hd.utils.pools.ObjectPool;
import com.giants3.hd.utils.pools.PoolCenter;
import com.google.inject.Inject;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * 材料分类对话框
 */
public class MaterialClassDialog extends BaseDialog<MaterialClass> {
    private JPanel contentPane;
    private JButton btn_save;
    private JButton btn_import;
    private JHdTable jtable;


    public List<MaterialClass> oldData=null;
    @Inject
    ApiManager apiManager;

     @Inject
    MaterialClassTableModel classTableModel;




    public MaterialClassDialog(Window window)
    {
        super(window, "材料分类列表");
        setContentPane(contentPane);
        setModal(true);
        setMinimumSize(new Dimension(800, 600));


       // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

                dispose();

            }
        });



        init();



        loadData();

    }


    private void init()
    {

        jtable.setModel(classTableModel);

        //保存数据
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                final List<MaterialClass> newData = classTableModel.getValuableDatas();

                if (newData.equals(oldData)) {

                    JOptionPane.showMessageDialog(MaterialClassDialog.this, "数据无改变！");
                    return;

                }

                saveDatas(newData);


            }
        });


        btn_import.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //选择excel 文件

                JFileChooser fileChooser = new JFileChooser(".");
                //下面这句是去掉显示所有文件这个过滤器。
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                //添加excel文件的过滤器
                fileChooser.addChoosableFileFilter(new ExcelFileFilter("xls"));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                /*
                    获得你选择的文件绝对路径
                */
                    String path = fileChooser.getSelectedFile().getAbsolutePath();


                    try {
                        List<MaterialClass> datas = readDataFromXLS(path);
                        saveDatas(datas);
                    } catch (Throwable e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(MaterialClassDialog.this, "文件读失败:" + e1.getMessage());
                    }

                }


            }
        });


    }


    /**
     * 保存数据
     * @param datas
     */

    private void saveDatas(final List<MaterialClass> datas)
    {


        new HdSwingWorker<Void, Object>(MaterialClassDialog.this) {
            @Override
            protected RemoteData<Void> doInBackground() throws Exception {


                return apiManager.saveMaterialClasses(datas);
            }

            @Override
            public void onResult(RemoteData<Void> data) {


                if (data.isSuccess()) {


                    JOptionPane.showMessageDialog(MaterialClassDialog.this, "保存成功");
                    loadData();


                } else {
                    JOptionPane.showMessageDialog(MaterialClassDialog.this, data.message);
                }


            }
        }.go();

    }

    /**
     * 从excel 表格中读取分类数据·
     * @param path
     * @return
     */
    private  List<MaterialClass> readDataFromXLS(String path) throws IOException, BiffException {



        InputStream is = new FileInputStream(path);


        Workbook rwb = Workbook.getWorkbook(is);

        //这里有两种方法获取sheet表:名字和下标（从0开始）

        Sheet st = rwb.getSheet("参数");

        int rsColumns = st.getColumns();
        int rsRows = st.getRows();


        List<MaterialClass> materials=new ArrayList<>();
//        int batchSize=10;
//        ObjectPool<Material> materialObjectPool= PoolCenter.getObjectPool(Material.class, batchSize);
        MaterialClass materialClass;

        for (int i = 1; i < rsRows; i++) {



            Cell cell = st.getCell(0, i);
            String value = cell.getContents();

            if(StringUtils.isEmpty(value))
            {
                //如果编码为空 直接跳过
                continue;

            }
            materialClass = new MaterialClass();

            //复制值。
            materialClass.code = value;

            try {
                materialClass.wLong = FloatHelper.scale(Float.valueOf(st.getCell(1, i).getContents()));
            } catch (Throwable t) {
                materialClass.wLong=0;
            }
            try {
                materialClass.wWidth = FloatHelper.scale(Float.valueOf(st.getCell(2, i).getContents()));
            } catch (Throwable t) {
                materialClass.wWidth=0;
            }
            try {
                materialClass.wHeight = FloatHelper.scale(Float.valueOf(st.getCell(3, i).getContents()));
            } catch (Throwable t) {
                materialClass.wHeight=0;
            }

            try {
                materialClass.available = FloatHelper.scale(Float.valueOf(st.getCell(4, i).getContents())) ;
            } catch (Throwable t) {
                materialClass.available=1;
            }

            try {
                materialClass.discount = FloatHelper.scale(Float.valueOf(st.getCell(5, i).getContents()));
            } catch (Throwable t) {
                materialClass.discount=0;
            }

            try {
                materialClass.type
                        = Integer.valueOf( st.getCell(6, i).getContents());
            } catch (Throwable t) {
                materialClass.type=-1;
            }




            materialClass.name = st.getCell(7, i).getContents();

            materials.add(materialClass);

            }


        rwb.close();
        is.close();

        return materials;







    }

    private  void loadData()
    {
        new HdSwingWorker<MaterialClass,Object>(this)
        {
            @Override
            protected RemoteData<MaterialClass> doInBackground() throws Exception {




                return apiManager.readMaterialClasses();
            }

            @Override
            public void onResult(RemoteData<MaterialClass> data) {


                if(data.isSuccess())
                {
                    oldData= (List<MaterialClass>) ObjectUtils.deepCopy(data.datas);
                    classTableModel.setDatas(data.datas);


                }else
                {
                    JOptionPane.showMessageDialog(MaterialClassDialog.this,data.message);
                }





            }
        }.go();
    }



}
