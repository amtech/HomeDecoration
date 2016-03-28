package com.giants.hd.desktop.reports.jasper;

import com.giants3.hd.utils.noEntity.ProductDetail;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by david on 2016/3/13.
 */
public class ProductPaintReportTest {

    @Test
    public void testExport() throws Exception {

        new ProductPaintReport().export(new ProductDetail(),"test",100);
    }
}