package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.model.BaseTableModel;
import com.giants.hd.desktop.model.QuoteAuthModel;
import com.giants.hd.desktop.widget.JHdTable;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.CacheManager;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.GlobalData;
import com.giants3.hd.utils.entity.QuoteAuth;
import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.exception.HdException;
import com.giants3.hd.utils.noEntity.BufferData;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class QuoteAuthDialog extends BaseSimpleDialog<QuoteAuth> {
    private JPanel contentPane;
    private JButton save;
    private JHdTable jt;
    private JPanel panel_relateSales;


    QuoteAuthModel quoteAuthModel;

    @Inject
    ApiManager apiManager;

    List<User> sales;

    public QuoteAuthDialog(Window window) {
        super(window);
        setTitle("报价细分权限");
        setContentPane(contentPane);


        sales = CacheManager.getInstance().bufferData.salesmans;


        for (User user : sales) {

            JRadioButton radioButton = new JRadioButton(user.code+"-"+user.name+"-"+user.chineseName);
            panel_relateSales.add(radioButton);
        }
        panel_relateSales.setVisible(false);
        jt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {

                    int modelRow = jt.convertRowIndexToModel(jt.getSelectedRow());
                    QuoteAuth quoteAuth = quoteAuthModel.getItem(modelRow);
                    bindRelateSalesData(  quoteAuth);
                }
            }
        });



    }


    private void bindRelateSalesData(QuoteAuth quoteAuth)
    {

        String relate=quoteAuth.relatedSales;

        String[] relateCodes= StringUtils.isEmpty(relate)?null:relate.split(",|,");
        int size=panel_relateSales.getComponentCount();
        for(int i=0;i<size;i++  )
        {
            Component component=panel_relateSales.getComponent(i);
            if(component instanceof  JRadioButton )
            {

                JRadioButton jRadioButton= (JRadioButton) component;

                jRadioButton.removeItemListener(itemListener);



                boolean find=false;
                if(relateCodes!=null)

                for(String id:relateCodes)
                {

                    if(String.valueOf(sales.get(i).id).equals(id))
                    {
                        find=true;
                        break;
                    }
                }
                jRadioButton.setSelected(find);

                jRadioButton.addItemListener(itemListener);
            }

        }

        panel_relateSales.setVisible(true);


    }

    private ItemListener itemListener=new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {



            StringBuilder  newRelates=new StringBuilder();
            //读取所有item  保存
            int size=panel_relateSales.getComponentCount();
            for(int i=0;i<size;i++  )
            {
               JRadioButton jRadioButton= (JRadioButton) panel_relateSales.getComponent(i);
                if(jRadioButton.isSelected())
                {
                    newRelates.append(sales.get(i).id).append(",");
                }


            }


            quoteAuthModel.getItem(jt.convertRowIndexToModel(jt.getSelectedRow())).relatedSales=newRelates.toString();



        }
    };


    @Override
    protected RemoteData<QuoteAuth> readData() throws HdException {
        return apiManager.readQuoteAuth() ;
    }

    @Override
    protected BaseTableModel<QuoteAuth> getTableModel() {
        return quoteAuthModel;
    }

    @Override
    protected void init() {



        quoteAuthModel=new QuoteAuthModel();
        jt.setModel(quoteAuthModel);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                doSaveWork();
            }
        });

    }



    @Override
    protected   RemoteData<QuoteAuth> saveData(List<QuoteAuth> datas)throws HdException
    {

        return apiManager.saveQuoteAuthList(datas);
    }

    @Override
    protected void setNewData(RemoteData<QuoteAuth> newData) {
        super.setNewData(newData);

        panel_relateSales.setVisible(false);
    }
}
