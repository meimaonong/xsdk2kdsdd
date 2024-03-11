package com.art.app.web.bean.response;

import lombok.Data;

import java.util.List;

@Data
public class VideoResult {

    private boolean hasMore = false;
    private List<VideoInfo> contents;
}
