package com.github.webapp.pojo;

import java.util.Map;

/**
 * 测试Bean1 (带有 Map page 属性)
 *
 * @author zxy
 */
public class PageBean1 {

    private int id;
    private String searchCODE;
    private Map<String, Object> page;

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

    public Map<String, Object> getPage() {
        return page;
    }

    public void setPage(Map<String, Object> page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "PageBean1{" + "id=" + id + ", searchCODE='" + searchCODE + '\'' + ", page=" + page + '}';
    }
}
