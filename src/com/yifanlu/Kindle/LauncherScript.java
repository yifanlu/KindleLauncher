package com.yifanlu.Kindle;

import com.amazon.ebook.pl.SystemServices;
import org.json.simple.JSONObject;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/14/11
 * Time: 9:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class LauncherScript extends LauncherAction {
    private File mScript;
    private String mArgs;

    public LauncherScript(String name, int priority, File script, String args) {
        super(name, priority);
        this.mScript = script;
        this.mArgs = args;
    }

    public synchronized void doAction() {
        StringBuffer out = new StringBuffer();
        StringBuffer err = new StringBuffer();
        KindleLauncher.SERVICES.getDeviceService().exec(mScript.getAbsolutePath() + " " + mArgs, out, err);
    }
}
