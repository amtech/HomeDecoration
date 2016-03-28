package com.giants.hd.desktop.reports.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.util.Log;

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

    @Override
    protected String getPropertyName(JRField field) {

        return super.getPropertyName(field);
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
        System.out.print("field:"+field.getName());
        Object result= super.getFieldValue(field);


        return result;
    }
}
