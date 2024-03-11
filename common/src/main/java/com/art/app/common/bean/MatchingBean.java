package com.art.app.common.bean;

/**
 * bean匹配
 * @author xiehui
 *
 */
public interface MatchingBean<T> {

    boolean matching(T factor);
    
}
