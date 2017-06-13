package com.qianfan365.jcstore.common.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianfanyanfa on 2017/3/9.
 */
public class StaticsHistory {

    private String year;

    private List<StaticsBean> statics = new ArrayList<>();

    public StaticsHistory(){};
    public StaticsHistory(String year,List<StaticsBean> statics){
        this.year=year;
        this.statics=statics;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<StaticsBean> getStatics() {
        return statics;
    }

    public void setStatics(List<StaticsBean> statics) {
        this.statics = statics;
    }
}
