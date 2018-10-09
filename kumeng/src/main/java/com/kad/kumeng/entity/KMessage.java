package com.kad.kumeng.entity;

import android.text.TextUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KMessage  implements Serializable {

    public static final String DISPLAY_TYPE_CUSTOM = "custom";
    public static final String DISPLAY_TYPE_NOTIFICATION = "notification";
    public static final String DISPLAY_TYPE_AUTOUPDATE = "autoupdate";
    public static final String DISPLAY_TYPE_PULLAPP = "pullapp";
    public static final String DISPLAY_TYPE_NOTIFICATIONPULLAPP = "notificationpullapp";
    public static final String NOTIFICATION_GO_ACTIVITY = "go_activity";
    public static final String NOTIFICATION_GO_APP = "go_app";
    public static final String NOTIFICATION_GO_URL = "go_url";
    public static final String NOTIFICATION_GO_CUSTOM = "go_custom";
    public static final String NOTIFICATION_GO_APPURL = "go_appurl";
    public String msg_id;
    public String message_id;
    public String task_id;
    public String display_type;
    public String alias;
    public String ticker;
    public String title;
    public String text;
    public boolean play_vibrate;
    public boolean play_lights;
    public boolean play_sound;
    public boolean screen_on;
    public String after_open;
    public String custom;
    public String url;
    public String sound;
    public String img;
    public String icon;
    public String activity;
    public String recall;
    public String bar_image;
    public String expand_image;
    public boolean isAction;
    public String pulled_service;
    public String pulled_package;
    public String pulledWho;
    public int builder_id;
    public Map<String, String> extra;
    private JSONObject a;
    public String largeIcon;
    public long random_min;
    public boolean clickOrDismiss;


    public KMessage(JSONObject var1) throws JSONException {
        this.a = var1;
        this.msg_id = var1.getString("msg_id");
        this.display_type = var1.getString("display_type");
        this.alias = var1.optString("alias");
        this.random_min = var1.optLong("random_min");
        JSONObject var2 = var1.getJSONObject("body");
        this.ticker = var2.optString("ticker");
        this.title = var2.optString("title");
        this.text = var2.optString("text");
        this.play_vibrate = var2.optBoolean("play_vibrate", true);
        this.play_lights = var2.optBoolean("play_lights", true);
        this.play_sound = var2.optBoolean("play_sound", true);
        this.screen_on = var2.optBoolean("screen_on", false);
        this.url = var2.optString("url");
        this.img = var2.optString("img");
        this.sound = var2.optString("sound");
        this.icon = var2.optString("icon");
        this.after_open = var2.optString("after_open");
        this.largeIcon = var2.optString("largeIcon");
        this.activity = var2.optString("activity");
        this.custom = var2.optString("custom");
        this.recall = var2.optString("recall");
        this.bar_image = var2.optString("bar_image");
        this.expand_image = var2.optString("expand_image");
        this.builder_id = var2.optInt("builder_id", 0);
        this.isAction = var2.optBoolean("isAction", false);
        this.pulled_service = var2.optString("pulled_service");
        this.pulled_package = var2.optString("pulled_package");
        this.pulledWho = var2.optString("pa");
        JSONObject var3 = var1.optJSONObject("extra");
        if (var3 != null && var3.keys() != null) {
            this.extra = new HashMap();
            Iterator var4 = var3.keys();

            while(var4.hasNext()) {
                String var5 = (String)var4.next();
                this.extra.put(var5, var3.getString(var5));
            }
        }

    }

    public JSONObject getRaw() {
        return this.a;
    }

    public boolean hasResourceFromInternet() {
        return this.isLargeIconFromInternet() || this.isSoundFromInternet() || !TextUtils.isEmpty(this.bar_image) || !TextUtils.isEmpty(this.expand_image);
    }

    public boolean isLargeIconFromInternet() {
        return !TextUtils.isEmpty(this.img);
    }

    public boolean isSoundFromInternet() {
        return !TextUtils.isEmpty(this.sound) && (this.sound.startsWith("http://") || this.sound.startsWith("https://"));
    }

    @Override
    public String toString() {
        return "KMessage{" +
                "msg_id='" + msg_id + '\'' +
                ", message_id='" + message_id + '\'' +
                ", task_id='" + task_id + '\'' +
                ", display_type='" + display_type + '\'' +
                ", alias='" + alias + '\'' +
                ", ticker='" + ticker + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", play_vibrate=" + play_vibrate +
                ", play_lights=" + play_lights +
                ", play_sound=" + play_sound +
                ", screen_on=" + screen_on +
                ", after_open='" + after_open + '\'' +
                ", custom='" + custom + '\'' +
                ", url='" + url + '\'' +
                ", sound='" + sound + '\'' +
                ", img='" + img + '\'' +
                ", icon='" + icon + '\'' +
                ", activity='" + activity + '\'' +
                ", recall='" + recall + '\'' +
                ", bar_image='" + bar_image + '\'' +
                ", expand_image='" + expand_image + '\'' +
                ", isAction=" + isAction +
                ", pulled_service='" + pulled_service + '\'' +
                ", pulled_package='" + pulled_package + '\'' +
                ", pulledWho='" + pulledWho + '\'' +
                ", builder_id=" + builder_id +
                ", extra=" + extra +
                ", a=" + a +
                ", largeIcon='" + largeIcon + '\'' +
                ", random_min=" + random_min +
                ", clickOrDismiss=" + clickOrDismiss +
                '}';
    }
}
