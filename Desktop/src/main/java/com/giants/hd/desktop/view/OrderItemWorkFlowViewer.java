package com.giants.hd.desktop.view;

import com.giants3.hd.utils.entity.OutFactory;

import java.util.List;

/**
 * Created by davidleen29 on 2017/1/11.
 */
public interface OrderItemWorkFlowViewer  extends    AbstractViewer{


    List<OutFactory> getArrangedFactories() throws Exception;

    public void     setProductTypes(String[] productTypes, String[] productTypeNames, List<OutFactory> outFactories);
}
