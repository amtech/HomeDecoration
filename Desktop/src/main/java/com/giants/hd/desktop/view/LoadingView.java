package com.giants.hd.desktop.view;

import java.awt.*;

/**
 * Interface representing a View that will use to load data.
 */
public interface LoadingView {


        /**
         * Show a view with a progress bar indicating a loading process.
         */
        void showLoading();

        /**
         * Hide a loading view.
         */
        void hideLoading();

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

}