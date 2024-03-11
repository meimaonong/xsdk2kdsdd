package com.art.app.mw.push;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public interface PushModel {

    static Gson gson = new Gson();

    JsonElement toJSON();

}
