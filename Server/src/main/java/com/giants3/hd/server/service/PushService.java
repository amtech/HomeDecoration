package com.giants3.hd.server.service;

import com.giants3.hd.server.noEntity.UmengPushData;
import com.giants3.hd.utils.DigestUtils;
import com.giants3.hd.utils.GsonUtils;

/**推送服务类
 * Created by davidleen29 on 2017/7/21.
 */
public class PushService {

    public static final String URL_PUSH="http://msg.umeng.com/api/send";
    public static final String URL_PUSH_HTTPS="https://msgapi.umeng.com/api/send";


    /**
     * 签名文件生成
     */
    public void  generateSign()
    {
      String   appkey = "";
        String  app_master_secret ="";
        String   timestamp = "";
        String  method = "POST";
        String url =URL_PUSH_HTTPS;

        UmengPushData umengPushData=new UmengPushData();
        String umengPushDataString= GsonUtils.toJson(umengPushData);

        String sign= DigestUtils.md5(method+url+umengPushDataString+app_master_secret);




    }
}
