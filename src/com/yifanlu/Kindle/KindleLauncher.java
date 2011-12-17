package com.yifanlu.Kindle;

import com.amazon.ebook.booklet.reader.sdk.ui.MenuInserter;
import com.amazon.ebook.booklet.reader.sdk.ui.ReaderAction;
import com.amazon.ebook.pl.SystemServices;
import com.amazon.ebook.util.log.Log;
import com.amazon.kindle.restricted.runtime.Framework;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/14/11
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class KindleLauncher extends LauncherMenu implements MenuInserter {
    public static final ResourceBundle RESOURCES = ResourceBundle.getBundle("com.yifanlu.Kindle.KindleLauncherResources");
    public static final SystemServices SERVICES = (SystemServices) Framework.getService(SystemServices.class);
    public static final Log LOG = Log.getInstance("KindleLauncher");
    private static KindleLauncher myInstance;
    private ArrayList mExtensions;

    private KindleLauncher() {
        super(RESOURCES.getString("menu.launcher.label"), 0);
        mExtensions = new ArrayList();
    }

    public static Action getInstance() {
        if (myInstance == null)
            myInstance = new KindleLauncher();
        return myInstance;
    }

    public void createMenu() {
        LOG.debug("Creating launcher menu");
        Iterator it = mExtensions.iterator();
        while (it.hasNext()) {
            Extension ext = (Extension) it.next();
            ext.getMenu().addItemsToMenu(this);
        }
    }

    public synchronized void doAction() {
        if (isMenuEmpty()) {
            createMenu();
        }
        super.doAction();
    }

    public ReaderAction[] getMenuActions() {
        return new ReaderAction[]{this};
    }
}
