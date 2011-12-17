package com.yifanlu.Kindle;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/17/11
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Extension {
    private String mName;
    private String mVersion;
    private String mAuthor;
    private String mId;
    private Menuable mMenu;

    public Extension() {
        this.mName = "Plugin";
        this.mVersion = "0.0";
        this.mAuthor = "Author";
        this.mId = super.toString();
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getName() {
        return this.mName;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public String getAuthor() {
        return this.mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getId() {
        return this.mId;
    }

    public Menuable getMenu() {
        return this.mMenu;
    }

    public void setMenu(Menuable menu) {
        this.mMenu = menu;
    }
}
