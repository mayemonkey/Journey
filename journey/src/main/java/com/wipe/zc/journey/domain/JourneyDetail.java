package com.wipe.zc.journey.domain;

import java.util.List;

/**
 * 行程详细对象
 */
public class JourneyDetail {

    private String contnent;

    private List<String> list_url;

    public String getContnent() {
        return contnent;
    }

    public void setContnent(String contnent) {
        this.contnent = contnent;
    }

    public List<String> getList_url() {
        return list_url;
    }

    public void setList_url(List<String> list_url) {
        this.list_url = list_url;
    }
}
