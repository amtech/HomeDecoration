package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.api.HttpUrl;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.LocalFileHelper;
import com.giants.hd.desktop.api.ApiManager;
import com.giants3.hd.utils.RemoteData;
import com.giants3.hd.utils.entity.User;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends BaseDialog<User> {
    private JPanel contentPane;
    private JTextField tf_password;
    private JButton btn_login;
    private JButton btn_logout;
    private JComboBox cb_user;


    @Inject
    ApiManager apiManager;




    public LoginDialog( Window window) {
        super(window,"登录");
        setContentPane(contentPane);


        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                login();
            }
        });


        btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                dispose();
            }
        });


        tf_password.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                login();
            }
        });


        loadUsers();









    }




    private  void  loadUsers()
    {

        new HdSwingWorker<User,Object>(this)
        {
            @Override
            protected RemoteData<User> doInBackground() throws Exception {
                return apiManager.readUsers();
            }

            @Override
            public void onResult(RemoteData<User> data) {

                if(data.isSuccess())
                {



                    cb_user.removeAllItems();
                    for(User  user:data.datas)
                        cb_user.addItem(user);


                    User user= LocalFileHelper.get(User.class);
                    int selectIndex=0;
                    if(user!=null)
                    {
                        for (int i = 0,count= cb_user.getItemCount(); i <count; i++) {

                            User temp= (User) cb_user.getItemAt(i);
                            if(temp.id==user.id)
                            {
                                selectIndex=i;
                                break;
                            }


                        }


                    }


                     cb_user.setSelectedIndex(selectIndex);



                }else {

                    JOptionPane.showMessageDialog(LoginDialog.this,data.message);
                }






            }
        }.go();
    }










    public void login()
    {


        final  String userName=((User)(cb_user.getSelectedItem())).name;
        final String password=tf_password.getText().toString();

        new HdSwingWorker<User,Object>(this)
        {
            @Override
            protected RemoteData<User> doInBackground() throws Exception {
                return apiManager.login(userName, password);
            }

            @Override
            public void onResult(RemoteData<User> data) {

                if(data.isSuccess())
                {


                    //登录成功

                    HttpUrl.setToken(data.token);
                    User  user=data.datas.get(0);
                    setResult(user);


                    LocalFileHelper.set(user);


                    dispose();




                }else {

                    JOptionPane.showMessageDialog(LoginDialog.this,data.message);
                }






            }
        }.go();
    }

}
