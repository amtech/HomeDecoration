package com.giants.hd.desktop;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class ImageViewDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    public ImageViewDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ImageViewDialog dialog = new ImageViewDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);

       new  SwingWorker<Void,Void>(){
           @Override
           protected Void doInBackground() throws Exception {

               publish();
               return null;
           }

           @Override
           protected void process(List<Void> chunks) {
               super.process(chunks);
           }

           @Override
           protected void done() {
               super.done();
           }
       }.execute();
    }
}
