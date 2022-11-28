package com.robot.tool;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * @author linwei
 * @title WebChatMessage
 * @date 2021年08月23日 下午3:59
 */
public class WebChatMessageUtils {
    /**
     * 发送微信公众号消息消息
     * @author linwei
     * @date 2021/8/23 下午4:04
     * @param personIds 发送的人的IDCARD
     * @param content 发送的内容
     * @param tokenValue 模块TOKEN
     * @return : void
     **/
    private static final  String path =null;
    public static void sendMessage(List<String> personIds, String content, String tokenValue) {

        JSONObject json =new JSONObject();
        json.set("content",content);
        json.set("personIds",personIds);
        HttpRequest.post(path)
                .header("token", tokenValue)
                .header("Content-Type","application/json").body(json.toJSONString(0)).execute().body();
    }
}
