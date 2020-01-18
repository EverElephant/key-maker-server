package com.everelp.keyMaker.mudules.controller;

import cn.hutool.core.util.RuntimeUtil;
import com.everelp.keyMaker.common.db.MakerCache;
import com.everelp.keyMaker.common.db.User;
import com.everelp.keyMaker.common.db.UserCache;
import com.everelp.keyMaker.common.util.UserUtil;
import com.everelp.keyMaker.common.vo.ResCodeMsg;
import com.everelp.keyMaker.common.vo.ServerRes;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

@RestController
@RequestMapping("key")
public class KeyController {

    @PostMapping("genKeyZip")
    public ServerRes genKeyZip(String json, HttpServletRequest request) {
        ServerRes serverRes = new ServerRes(ResCodeMsg.SUCCESS);
        if (isMakeRunning()) {
            serverRes.setCodeMsg(ResCodeMsg.MAKER_UNAVAL);
            return serverRes;
        }
        User user = UserCache.cache.get(request.getSession().getId());
        if (user == null || StringUtils.isEmpty(user.getUserName())) {
            serverRes.setCodeMsg(ResCodeMsg.NO_USER);
            return serverRes;
        }
        Date now = new Date();
        MakerCache.nowUserName = user.getUserName();
        MakerCache.makeTime = now.getTime();
        makeKeys(user.getUserName(), json, now);
        return serverRes;
    }

    private boolean makeKeys(String userName, String json, Date now) {
        Process p = RuntimeUtil.exec("make -C /home/ubuntu/nRF5_SDK_15.3.0/examples/ble_peripheral/ble_app_hids_keyboard/pca10040/s132/armgcc clean");
        while (true) {
            if (!p.isAlive()) {
                break;
            }
        }
        p = RuntimeUtil.exec("make -C /home/ubuntu/nRF5_SDK_15.3.0/examples/ble_peripheral/ble_app_hids_keyboard/pca10040/s132/armgcc");
        while (true) {
            if (!p.isAlive()) {
                break;
            }
        }
        p = RuntimeUtil.exec("cp /home/ubuntu/nRF5_SDK_15.3.0/examples/ble_peripheral/ble_app_hids_keyboard/pca10040/s132/armgcc/_build/nrf52832_xxaa.hex /home/ubuntu/nrf52/app_new.hex");
        while (true) {
            if (!p.isAlive()) {
                break;
            }
        }
        p = RuntimeUtil.exec("nrfutil pkg generate --application /home/ubuntu/nrf52/app_new.hex --application-version 2 --hw-version 52 --sd-req 0x9D --key-file /home/ubuntu/nrf52/priv.pem /home/ubuntu/nrf52/SDK15_app_s132.zip");
        while (true) {
            if (!p.isAlive()) {
                break;
            }
        }
        p = RuntimeUtil.exec("mkdir /home/ubuntu/nrf52/download/" + userName + now.getTime());
        while (true) {
            if (!p.isAlive()) {
                break;
            }
        }
        p = RuntimeUtil.exec("cp /home/ubuntu/nrf52/SDK15_app_s132.zip /home/ubuntu/nrf52/download/" + userName + now.getTime());
        while (true) {
            if (!p.isAlive()) {
                break;
            }
        }
        p = RuntimeUtil.exec("mkdir /var/www/html/download/" + userName);
        while (true) {
            if (!p.isAlive()) {
                break;
            }
        }
        p = RuntimeUtil.exec("cp /home/ubuntu/nrf52/download/" + userName + now.getTime()+"/SDK15_app_s132.zip /var/www/html/download/" + userName);
        while (true) {
            if (!p.isAlive()) {
                break;
            }
        }
        return true;
    }


    /**
     * 下载zip文件
     *
     * @param url      图片url
     * @param response 请求响应
     */
    @PostMapping(value = "/download")
    public ServerRes getUrlFile(String url, HttpServletRequest request, HttpServletResponse response) {
        ServerRes serverRes = new ServerRes(ResCodeMsg.SUCCESS);
        User user = UserUtil.getCurrentUser(request);
        if(user == null){
            response.setStatus(401);
            return serverRes;
        }
        if(isMakeRunning()){
            if(MakerCache.nowUserName.equals(user.getUserName())){
                serverRes.setCodeMsg(ResCodeMsg.MAKING_NOW);
                return serverRes;
            }
        }
        File file = new File("/var/www/html/download/"+user.getUserName()+"/SDK15_app_s132.zip");
        if(!file.exists() || !file.canRead()){
            serverRes.setCodeMsg(ResCodeMsg.NO_FILE_MADE);
            return serverRes;
        }
        serverRes.setData("http://"+request.getServerName()+"/download/"+user.getUserName()+"/SDK15_app_s132.zip");
        return serverRes;
    }

    private boolean isMakeRunning() {
        long lastMakeTime = MakerCache.makeTime;
        // 超过五分钟一定可以再生成了
        if (new Date().getTime() - 300000 > lastMakeTime) {
            return false;
        }
        // 五分钟以内，看下对应的生成了没
        File file = new File("/home/ubuntu/nrf52/download/" + MakerCache.nowUserName + MakerCache.makeTime + "/SDK15_app_s132.zip");
        if (file.exists() && file.canRead()) {
            return false;
        }
        return true;
    }
}
