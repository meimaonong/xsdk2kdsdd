package com.art.app.web.bean.response;

import lombok.Data;

import java.util.List;

@Data
public class XslList<T> extends HomePageResult {

    private List<T> contents;
}
