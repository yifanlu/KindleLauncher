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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

/**
 * This class holds the menu options for a stdout JSON menu.
 *
 * @author Vasilij Litvinov
 * @version 1.0
 */
public class StdoutJSONMenu extends JSONMenu {
    private File mScript;
    private String mArgs;
    private int mTimeout;
    private File mExtDir;

    /**
     * Create a new StdoutJSONMenu object with the command and arguments to invoke that prints JSON-formatted menu to stdout
     * for JSONMenu format see JSONMenu class
     *
     * @param script   The shell script or command to run
     * @param extDir   Directory where extention that is being loaded lives, ignored if script starts with "/"
     * @param args     The arguments of the script
     * @param timeout  timeout to wait before terminating; don't terminate if 0
     */
    public StdoutJSONMenu(String script, File extDir, String args, int timeout) {
        super(new File(extDir, "dummy.json"));
        // passing dummy.json to JSONMenu constructor as jsonFile - we won't be reading a file anyway
        // but it would be needed when JSONMenu.jsonToAction kicks into action
        if (script.startsWith("/"))
            mScript = new File(script);
        else
            mScript = new File(extDir, script);
        mArgs = args;
        mTimeout = timeout;
        mExtDir = extDir;
        setDynamic(false); // tell our ancestor not to reload anything
    }
    
    public String getScriptStr() {
        String result = mScript.getAbsolutePath();
        if (mArgs != "")
            result = result + " " + mArgs;
        return result;
    }

    /**
     * Here we call the command and parse its output
     *
     * @param menu The menu of the caller.
     */
    public void addItemsToMenu(LauncherMenu menu) {
        KindleLauncher.LOG.debug("Refreshing the menu");
        try {
            parseJSONMenu();
        } catch (IOException e) {
            KindleLauncher.LOG.error("Cannot create menu because script <" + getScriptStr() + "> cannot be executed?");
            e.printStackTrace();
        } catch (ParseException e) {
            KindleLauncher.LOG.error("Cannot create menu because script <" + getScriptStr() + "> output cannot be parsed.");
            e.printStackTrace();
        }
        super.addItemsToMenu(menu);
    }


    /**
     * Executes the command stored in the object and parses its stdout as JSON
     * stops executing if command returned non-zero exit code or non-empty stderr
     *
     * @throws IOException    if there is a problem reading the JSON text
     * @throws ParseException if there is a problem parsing the JSON text
     */
    public void parseJSONMenu() throws IOException, ParseException {
        mMenuItems = new LauncherMenu[]{};
        JSONParser parse = new JSONParser();
        
        StringBuffer out = new StringBuffer();
        StringBuffer err = new StringBuffer();
        String cmd = "/mnt/us/extensions/wdexec.sh " + mExtDir.getAbsolutePath() + " ";
        if (mTimeout > 0) {
            cmd += "../timeout.sh -t " + mTimeout + " ";
        }
        cmd += getScriptStr();
        int retcode = KindleLauncher.SERVICES.getDeviceService().exec(cmd, out, err);
        if (retcode != 0) {
            throw new IOException("Script <" + cmd + "> returned non-zero exit code: " + retcode);
        }
        if (err.length() != 0) {
            throw new IOException("Script <" + cmd + "> returned non-emtpy stderr: " + err.toString());
        }
        
        KindleLauncher.LOG.debug("Loading JSON from script <" + cmd + "> stdout: " + out.toString());
        JSONObject obj = (JSONObject) parse.parse(out.toString());
        
        updateActions(obj);
    }

}
