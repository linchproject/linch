package com.linchproject.linch;

/**
 * @author Georg Schmidl
 */
public class App implements Comparable<App> {

    private String name;
    private String description;
    private String path;
    private String appPackage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    @Override
    public int compareTo(App app) {
        return getName().compareTo(app.getName());
    }
}
