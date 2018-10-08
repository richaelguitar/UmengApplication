package com.kad.kumeng.entity;

import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class KMessage extends UMessage implements Serializable {


    public KMessage(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }
}
