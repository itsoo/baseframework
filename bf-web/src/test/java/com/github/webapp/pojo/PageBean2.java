package com.github.webapp.pojo;

/**
 * 测试Bean2 (不带有 Map page 属性)
 *
 * @author zxy
 */
public class PageBean2 {

    private int id;
    private String searchCODE;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearchCODE() {
        return searchCODE;
    }

    public void setSearchCODE(String searchCODE) {
        this.searchCODE = searchCODE;
    }

    @Override
    public String toString() {
        return "PageBean2{" + "id=" + id + ", searchCODE='" + searchCODE + '\'' + '}';
    }
}
