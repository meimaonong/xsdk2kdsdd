package com.art.app.web.bean.response;

import com.art.app.web.bean.ArtistResource;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class ArtistResult extends HomePageResult {

    private List<ArtistResource> hotArtists = Lists.newArrayList();

    private List<ArtistResource> artists = Lists.newArrayList();
}
