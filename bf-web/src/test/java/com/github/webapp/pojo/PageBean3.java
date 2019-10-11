package com.github.webapp.pojo;

import java.util.Map;

/**
 * 测试Bean2 (不带有 Map page 属性)
 *
 * @author zxy
 */
public class PageBean3 {

    private int id;
    private String searchCODE;
    private boolean pageList;
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

    public boolean isPageList() {
        return pageList;
    }

    public void setPageList(boolean pageList) {
        this.pageList = pageList;
    }

    public Map<String, Object> getPage() {
        return page;
    }

    public void setPage(Map<String, Object> page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "PageBean3{" + "id=" + id + ", searchCODE='" + searchCODE + '\'' + ", pageList=" + pageList + ", page=" +
               page + '}';
    }
}
