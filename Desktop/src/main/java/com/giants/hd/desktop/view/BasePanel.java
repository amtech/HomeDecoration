package com.giants.hd.desktop.view;

import com.google.inject.Guice;

/**
 * 基础模本类。  提供guice注射等等功能  提供一些公共方法
 */
public  abstract  class BasePanel {



    public BasePanel()
    {

        Guice.createInjector().injectMembers(this);
    }
}
