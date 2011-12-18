package com.yifanlu.Kindle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/17/11
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class JSONMenu implements Menuable {
    private File mJsonFile;
    private LauncherAction[] mMenuItems;
    private boolean mDynamic;

    public JSONMenu(File jsonFile) {
        mJsonFile = jsonFile;
        mMenuItems = new LauncherAction[]{};
        mDynamic = false;
    }

    public void addItemsToMenu(LauncherMenu menu) {
        if (isDynamic()) {
            try {
                parseJSONMenu();
            } catch (IOException e) {
                KindleLauncher.LOG.error("Cannot create menu because file " + mJsonFile.getAbsolutePath() + " cannot be read.");
                e.printStackTrace();
            } catch (ParseException e) {
                KindleLauncher.LOG.error("Cannot create menu because file " + mJsonFile.getAbsolutePath() + " cannot be parsed.");
                e.printStackTrace();
            }
        }
        for (int i = 0; i < mMenuItems.length; i++) {
            if (mMenuItems[i] instanceof LauncherMenu)
                ((LauncherMenu) mMenuItems[i]).setParent(menu);
            menu.addMenuItem(mMenuItems[i]);
        }
    }

    public void setDynamic(boolean dynamic) {
        this.mDynamic = dynamic;
    }

    public boolean isDynamic() {
        return mDynamic;
    }

    public void parseJSONMenu() throws IOException, ParseException {
        mMenuItems = new LauncherMenu[]{};
        FileReader read = new FileReader(mJsonFile);
        JSONParser parse = new JSONParser();
        JSONObject obj = (JSONObject) parse.parse(read);
        LauncherAction action = jsonToAction(obj, null);
        if (action instanceof LauncherMenu && action.getValue().equals("No Text")) {
            mMenuItems = ((LauncherMenu) action).getMenuItems();
        } else {
            mMenuItems = new LauncherAction[]{action};
        }
    }

    private LauncherAction jsonToAction(JSONObject json, LauncherMenu parent) throws IOException {
        String name = (String) json.get("name");
        Number priorityNum = (Number) json.get("priority");
        int priority;
        String action = (String) json.get("action");
        String params = (String) json.get("params");
        JSONArray items = (JSONArray) json.get("items");
        LauncherAction launcherAction = null;
        if (name == null)
            name = "No Text";
        if (priorityNum == null)
            priority = 0;
        else
            priority = priorityNum.intValue();
        if (items != null) {
            launcherAction = new LauncherMenu(name, priority, parent);
            Iterator it = items.iterator();
            while (it.hasNext()) {
                JSONObject itemObj = (JSONObject) it.next();
                ((LauncherMenu) launcherAction).addMenuItem(jsonToAction(itemObj, (LauncherMenu) launcherAction));
            }
        } else if (action != null) {
            if (params == null)
                params = "";
            File script = new File(mJsonFile.getParentFile(), action);
            launcherAction = new LauncherScript(name, priority, script, params);
        } else {
            throw new IOException("No valid action found for menu item: " + json.toJSONString());
        }
        return launcherAction;

    }
}
