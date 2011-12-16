package com.yifanlu.Kindle;

import java.util.ListResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/14/11
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class KindleLauncherResources extends ListResourceBundle {
    static final Object[][] RESOURCES = {
            {"menu.launcher.label", "Launcher"},
            {"menu.launcher.next_page.label", "Next Page"},
            {"menu.launcher.previous_page.label", "Previous Page"}
    };

    protected Object[][] getContents(){
        return RESOURCES;
    }
}
