package com.giants.hd.desktop.reports.jasper;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.Collection;

/**
 * Created by david on 2016/3/15.
 */
public class CustomBeanDataSource extends JRBeanCollectionDataSource {
    public CustomBeanDataSource(Collection beanCollection) {
        super(beanCollection);
    }

    public CustomBeanDataSource(Collection beanCollection, boolean isUseFieldDescription) {
        super(beanCollection, isUseFieldDescription);
    }
}
