package com.giants.hd.desktop.model;

import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.entity.app.QuotationItem;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public class AppQuotationItemTableModel extends  BaseListTableModel<QuotationItem> {




    public AppQuotationItemTableModel( ) {
        super(QuotationItem.class, TableStructureUtils.getAppQuotationItem());

    }






}
