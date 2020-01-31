package com.teemo.ppjoke2.model;

public class Destination {

    /**
     * isFragment : true
     * asStarter : true
     * needLogin : false
     * pageUrl : main/tabs/home
     * className : com.teemo.ppjoke2.ui.home.HomeFragment
     * id : 1328735132
     */

    private boolean isFragment;
    private boolean asStarter;
    private boolean needLogin;
    private String pageUrl;
    private String className;
    private int id;

    public boolean isIsFragment() {
        return isFragment;
    }

    public void setIsFragment(boolean isFragment) {
        this.isFragment = isFragment;
    }

    public boolean isAsStarter() {
        return asStarter;
    }

    public void setAsStarter(boolean asStarter) {
        this.asStarter = asStarter;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Destination{" +
                "isFragment=" + isFragment +
                ", asStarter=" + asStarter +
                ", needLogin=" + needLogin +
                ", pageUrl='" + pageUrl + '\'' +
                ", className='" + className + '\'' +
                ", id=" + id +
                '}';
    }
}
