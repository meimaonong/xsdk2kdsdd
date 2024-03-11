package com.art.app.web.bean;

import lombok.Data;

import java.util.Objects;

@Data
public class ArticleResource extends BaseResource {

    private String imgUrl;
    private String videoUrl;
    private String resourceId;
    private Integer status;
    private int from;
    private String description;
    private int likeNum = 0;
    private boolean like = false;
    private boolean focus = false;
    private String detailUrl;
    private String failReason;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ArticleResource that = (ArticleResource) o;
        return Objects.equals(resourceId, that.resourceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), resourceId);
    }
}
