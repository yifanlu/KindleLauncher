package com.yifanlu.Kindle;

import com.amazon.ebook.booklet.reader.sdk.ui.MenuInserter;
import com.amazon.ebook.booklet.reader.sdk.ui.ReaderAction;
import com.amazon.ebook.pl.SystemServices;
import com.amazon.ebook.util.log.Log;
import com.amazon.ebook.util.thread.ThreadPool;
import com.amazon.kindle.restricted.runtime.Framework;
import com.amazon.kindle.util.lipc.LipcException;
import com.amazon.kindle.util.lipc.LipcService;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/14/11
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class KindleLauncher extends LauncherMenu implements MenuInserter {
    public static final ResourceBundle RESOURCES = ResourceBundle.getBundle("com.yifanlu.Kindle.KindleLauncherResources");
    public static final SystemServices SERVICES = (SystemServices)Framework.getService(SystemServices.class);
    public static final Log LOG = Log.getInstance("KindleLauncher");
    private static KindleLauncher myInstance;

    private KindleLauncher(){
        super(RESOURCES.getString("menu.launcher.label"), 0);
    }

    public static Action getInstance() {
        if(myInstance == null)
            myInstance = new KindleLauncher();
        return myInstance;
    }

    public void createMenu(){
        LOG.debug("Creating launcher menu");
        LauncherMenu menu1 = new LauncherMenu("Submenu 1", 0, this);
        LauncherTest menu1Option1 = new LauncherTest("Option 1", 0);
        LauncherTest menu1Option2 = new LauncherTest("Option 2", 1);
        LauncherMenu menu2 = new LauncherMenu("Submenu 2", 2, menu1);
        menu1.addMenuItem(menu1Option1);
        menu1.addMenuItem(menu1Option2);
        menu1.addMenuItem(menu2);
        LauncherTest option1 = new LauncherTest("Option 1", 1);
        addMenuItem(menu1);
        addMenuItem(option1);

        LauncherScript script = new LauncherScript("Set portrait mode using Shell", 3, new File("/usr/bin/lipc-set-prop"), "com.lab126.winmgr orientationLock U");
        LauncherJava java = new LauncherJava("Set landscape mode using Java", 4, new File("/opt/amazon/ebook/lib/KindleLauncher.jar"), "com.yifanlu.Kindle.Test.TestScript");
        addMenuItem(script);
        addMenuItem(java);
    }

    public synchronized void doAction(){
        if(isMenuEmpty()){
            createMenu();
        }
        super.doAction();
    }

    public ReaderAction[] getMenuActions() {
        return new ReaderAction[]{ this };
    }
}
