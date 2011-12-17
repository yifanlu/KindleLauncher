package com.yifanlu.Kindle;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/17/11
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class JSONMenu implements Menuable {
    private File mJsonFile;
    private LauncherMenu[] mMenuItems;
    private boolean mDynamic;

    public JSONMenu(File jsonFile) {
        mJsonFile = jsonFile;
        mMenuItems = new LauncherMenu[]{};
        mDynamic = false;
    }

    public void addMenuItem(LauncherMenu menu) {
        mMenuItems = (LauncherMenu[]) Arrays.copyOf(mMenuItems, mMenuItems.length + 1);
        mMenuItems[mMenuItems.length - 1] = menu;
    }

    public void addItemsToMenu(LauncherMenu menu) {
        if (isDynamic()) {
            try {
                parseJSONMenu();
            } catch (IOException e) {
                KindleLauncher.LOG.error("Cannot create menu because file " + mJsonFile.getAbsolutePath() + " cannot be read.");
                e.printStackTrace();
            }
        }
        for (int i = 0; i < mMenuItems.length; i++) {
            menu.addMenuItem(mMenuItems[i]);
        }
    }

    public void setDynamic(boolean dynamic) {
        this.mDynamic = dynamic;
    }

    public boolean isDynamic() {
        return mDynamic;
    }

    public void parseJSONMenu() throws IOException {
        mMenuItems = new LauncherMenu[]{};

    }
}
