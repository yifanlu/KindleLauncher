/**
 * Kindle Launcher
 * GUI menu launcher for Kindle Touch
 * Copyright (C) 2011  Yifan Lu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.yifanlu.Kindle;

import com.amazon.ebook.util.log.LogMessage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * This class holds the menu options for a JSON menu.
 *
 * @author Yifan Lu
 * @version 1.0
 */
public class JSONMenu implements Menuable {
    private static final LogMessage JSON_MENU_ITEM = new LogMessage("JsonMenuItem", new String[]{"name", "action", "params"});
    private File mJsonFile;
    private LauncherAction[] mMenuItems;
    private boolean mDynamic;

    /**
     * Create a new JSONMenu object with the JSON file. The format of the JSON menu is
     * <p/>
     * <pre>
     *     {
     *         "items": [
     *              {"name": "Main Menu, Item 1", "priority": 0, "action": "bin/action.sh", "params": "item1"},
     *              {
     *                  "name": "Main Menu, Submenu 1",
     *                  "priority": 1,
     *                  "items": [
     *                      {"name": "Submenu 1, Item 1", "priority": 0, "action": "/sbin/restart", "params": ""}
     *                  ]
     *              }
     *         ]
     *     }
     * </pre>
     * <p/>
     * For each menu item, the "name" is the text that shows up and the "priority" is the order that they show up in
     * (if two items have the same priority, they are sorted in alphabetical order). If the item is an action, the
     * "action" contains the script to run and "params" contains the parameters. If the script name starts with a slash,
     * it is an absolute path, otherwise it is relative to the path of the JSON file. If the item is a submenu, it
     * will contain "items" instead of "action" and "params" and "items" is an array of menu items.
     *
     * @param jsonFile A text file that contains the JSON data.
     */
    public JSONMenu(File jsonFile) {
        mJsonFile = jsonFile;
        mMenuItems = new LauncherAction[]{};
        mDynamic = false;
    }

    /**
     * If the menu is dynamic as specified in config.xml, it will first call {@link JSONMenu#parseJSONMenu()}. Then it
     * adds the menu options specified by the JSON file to the menu of the caller. The caller will most likely be
     * the main Launcher menu.
     *
     * @param menu The menu of the caller.
     */
    public void addItemsToMenu(LauncherMenu menu) {
        if (isDynamic()) {
            KindleLauncher.LOG.debug("Reloading the menu.");
            mJsonFile = new File(mJsonFile.getAbsolutePath());
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

    /**
     * Sets the dynamic status of the menu
     *
     * @param dynamic Should the menu be dynamic?
     * @see com.yifanlu.Kindle.JSONMenu#isDynamic()
     */
    public void setDynamic(boolean dynamic) {
        this.mDynamic = dynamic;
    }

    /**
     * If dynamic, the menu will be re-parsed from the JSON file each time it is rendered. This is useful if you have
     * a dynamic script that can alter the JSON menu when ran.
     *
     * @return The dynamic status of the menu
     */
    public boolean isDynamic() {
        return mDynamic;
    }

    /**
     * Takes the JSON file stored in the object and converts it to menu item objects.
     *
     * @throws IOException    if there is a problem reading the JSON text
     * @throws ParseException if there is a problem parsing the JSON text
     */
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

    /**
     * Recursively reads the JSON data and converts it to a {@link LauncherAction} which contains either a
     * {@link LauncherScript} which executes a shell command when called or a {@link LauncherMenu} which contains
     * other {@link LauncherAction} items.
     *
     * @param json   The JSON object to parse
     * @param parent The menu to add the items to
     * @return The {@link LauncherAction} that is defined by the JSON data
     * @throws IOException if there are problems reading the JSON file
     */
    protected LauncherAction jsonToAction(JSONObject json, LauncherMenu parent) throws IOException {
        String name = (String) json.get("name");
        Number priorityNum = (Number) json.get("priority");
        int priority;
        String action = (String) json.get("action");
        String params = (String) json.get("params");
        JSONArray items = (JSONArray) json.get("items");
        LauncherAction launcherAction;
        if (name == null)
            name = "No Text";
        if (priorityNum == null)
            priority = 0;
        else
            priority = priorityNum.intValue();
        if (items != null) {
            launcherAction = new LauncherMenu(name, priority, parent);
            KindleLauncher.LOG.debug(JSON_MENU_ITEM, new String[]{name, "submenu", ""}, "");
            Iterator it = items.iterator();
            while (it.hasNext()) {
                JSONObject itemObj = (JSONObject) it.next();
                ((LauncherMenu) launcherAction).addMenuItem(jsonToAction(itemObj, (LauncherMenu) launcherAction));
            }
        } else if (action != null) {
            if (params == null)
                params = "";
            File script;
            if (action.startsWith("/"))
                script = new File(action);
            else
                script = new File(mJsonFile.getParentFile(), action);
            launcherAction = new LauncherScript(name, priority, script, params);
            KindleLauncher.LOG.debug(JSON_MENU_ITEM, new String[]{name, action, params}, "");
        } else {
            throw new IOException("No valid action found for menu item: " + json.toJSONString());
        }
        return launcherAction;

    }
}
