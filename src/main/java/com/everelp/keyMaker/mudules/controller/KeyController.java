package com.everelp.keyMaker.mudules.controller;

import cn.hutool.core.util.RuntimeUtil;
import com.everelp.keyMaker.common.db.MakerCache;
import com.everelp.keyMaker.common.db.User;
import com.everelp.keyMaker.common.db.UserCache;
import com.everelp.keyMaker.common.util.UserUtil;
import com.everelp.keyMaker.common.vo.ResCodeMsg;
import com.everelp.keyMaker.common.vo.ServerRes;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
        makeKeys(user.getUserName(), json);
        MakerCache.nowUserName = user.getUserName();
        MakerCache.makeTime = new Date().getTime();
        return serverRes;
    }

    private boolean makeKeys(String userName, String json) {
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
        p = RuntimeUtil.exec("mkdir /home/ubuntu/nrf52/download/" + userName);
        while (true) {
            if (!p.isAlive()) {
                break;
            }
        }
        p = RuntimeUtil.exec("cp /home/ubuntu/nrf52/SDK15_app_s132.zip /home/ubuntu/nrf52/download/" + userName);
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
    @RequestMapping(value = "/download")
    public void getUrlFile(String url, HttpServletRequest request, HttpServletResponse response) {
        // 这里的url，我为了测试，直接就写静态的。
        User user = UserUtil.getCurrentUser(request);
        if (user == null) {
            response.setStatus(401);
            return;
        }
        url = "/home/ubuntu/nrf52/download/" + user.getUserName() + "/SDK15_app_s132.zip";
        File file = new File(url);

        //判断文件是否存在如果不存在就返回默认图片或者进行异常处理
        if (!(file.exists() && file.canRead())) {
            response.setStatus(404);
            return;
        }
        response.addHeader("Content-Disposition","attachment;filename=SDK15_app_s132.zip");
        response.setContentType("application/x-zip-compressed;charset=utf-8");
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            inputStream.read(data);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isMakeRunning() {
        long lastMakeTime = MakerCache.makeTime;
        // 五分钟
        if (new Date().getTime() - 300000 > lastMakeTime) {
            return false;
        }
        File file = new File("/home/ubuntu/nrf52/download/" + MakerCache.nowUserName + "/SDK15_app_s132.zip");
        if (file.exists() && file.canRead()) {
            return false;
        }
        return true;
    }
}
