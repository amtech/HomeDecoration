package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.filters.ExcelFileFilter;
import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.MaterialClassTableModel;
import com.giants.hd.desktop.reports.EXCEL_TYPE;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.MaterialClass;
import com.giants3.hd.utils.exception.HdException;
import com.google.inject.Inject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * 材料分类对话框
 */
public class MaterialClassDialog extends BaseSimpleDialog<MaterialClass> {
    private JPanel contentPane;
    private JButton btn_save;
    private JButton btn_import;
    private JHdTable jtable;

    @Inject
    ApiManager apiManager;

     @Inject
     MaterialClassTableModel classTableModel;




    public MaterialClassDialog(Window window)
    {
        super(window);

    }


    @Override
    protected void init()
    {
        setContentPane(contentPane);
        setTitle( "材料分类列表");
        jtable.setModel(classTableModel);

        //保存数据
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSaveWork();


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
                        classTableModel.setDatas(datas);
                       // doSaveWork( );
                    } catch (Throwable e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(MaterialClassDialog.this, "文件读失败:" + e1.getMessage());
                    }

                }


            }
        });


    }




    /**
     * 从excel 表格中读取分类数据·
     * @param path
     * @return
     */
    private  List<MaterialClass> readDataFromXLS(String path) throws IOException {

        String fileType=path.substring(path.lastIndexOf(".")+1);


        InputStream is = new FileInputStream(path);
        Workbook rwb;
        if(EXCEL_TYPE.XLSX.equals(fileType))
        {
            rwb=new XSSFWorkbook(is);
        }else {
            rwb=new HSSFWorkbook(is);
        }


        //这里有两种方法获取sheet表:名字和下标（从0开始）

        Sheet st = rwb.getSheet("参数");


        int rsRows = st.getLastRowNum();


        List<MaterialClass> materials=new ArrayList<>();
//        int batchSize=10;
//        ObjectPool<Material> materialObjectPool= PoolCenter.getObjectPool(Material.class, batchSize);
        MaterialClass materialClass;

        for (int i = 1; i < rsRows; i++) {

            Row row=st.getRow(i);
            if(row==null)
                continue;

            Cell cell = row.getCell(0, Row.RETURN_BLANK_AS_NULL);
            String value = cell.getStringCellValue();
            if(StringUtils.isEmpty(value))
            {
                //如果编码为空 直接跳过
                continue;

            }
            materialClass = new MaterialClass();

            //复制值。
            materialClass.code = value;

            try {
                materialClass.wLong = (float) row.getCell(1, Row.RETURN_BLANK_AS_NULL).getNumericCellValue();
            } catch (Throwable t) {
                materialClass.wLong=0;
            }
            try {
                materialClass.wWidth =(float) row.getCell(2, Row.RETURN_BLANK_AS_NULL).getNumericCellValue()  ;
            } catch (Throwable t) {
                materialClass.wWidth=0;
            }
            try {
                materialClass.wHeight = (float) row.getCell(3, Row.RETURN_BLANK_AS_NULL).getNumericCellValue();
            } catch (Throwable t) {
                materialClass.wHeight=0;
            }

            try {
                materialClass.available =(float) row.getCell(4, Row.RETURN_BLANK_AS_NULL).getNumericCellValue();
            } catch (Throwable t) {
                materialClass.available=1;
            }

            try {
                materialClass.discount =(float) row.getCell(5, Row.RETURN_BLANK_AS_NULL).getNumericCellValue();
            } catch (Throwable t) {
                materialClass.discount=0;
            }

            try {
                materialClass.type
                        = (int) row.getCell(6, Row.RETURN_BLANK_AS_NULL).getNumericCellValue();
            } catch (Throwable t) {
                materialClass.type=-1;
            }




            materialClass.name =  row.getCell(7, Row.RETURN_BLANK_AS_NULL).getStringCellValue()  ;

            materials.add(materialClass);

            }


        rwb.close();
        is.close();

        return materials;







    }

    @Override
    protected RemoteData<MaterialClass> readData() throws HdException {
        return apiManager.readMaterialClasses();
    }

    @Override
    protected BaseTableModel<MaterialClass> getTableModel() {
        return classTableModel;
    }

    @Override
    protected   RemoteData<MaterialClass> saveData(List<MaterialClass> datas)throws HdException
    {

        return apiManager.saveMaterialClasses(datas);
    }

}
