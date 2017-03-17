package com.giants.hd.desktop.view;

import java.awt.*;

/**
 * Interface representing a View that will use to load data.
 */
public interface AbstractViewer {


        /**
         * Show a view with a progress bar indicating a loading process.
         */
        void showLoadingDialog();

        void showLoadingDialog(String hint);



        /**
         * Show a retry view in case of an error when retrieving data.
         */
        void showRetry();

        /**
         * Hide a retry view shown if there was an error when retrieving data.
         */
        void hideRetry();

        /**
         * Show an error message
         *
         * @param message A string representing an error.
         */
        void showError(String message);

        /**
         * Get a {@link java.awt.Window}.
         */
        Window getWindow();

        Container getRoot();

        void hideLoadingDialog();
        void showMesssage(String message);

    /**
     * 显示等待框处理 ，只有在一些需要长时间加载的界面上使用
     * @deprecated
     *
     */
    void showLoadingDialogCarfully();
}
