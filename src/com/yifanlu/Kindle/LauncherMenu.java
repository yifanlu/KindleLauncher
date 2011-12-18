package com.yifanlu.Kindle;

import com.amazon.agui.swing.MenuDialog;
import com.amazon.kindle.util.lipc.LipcException;
import com.amazon.kindle.util.lipc.LipcService;
import edu.emory.mathcs.backport.java.util.PriorityQueue;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/14/11
 * Time: 9:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class LauncherMenu extends LauncherAction {
    private MenuDialog mDialog;
    private PriorityQueue mItems;
    private LauncherMenu mParent;

    public LauncherMenu(String name, int priority) {
        this(name, priority, null);
    }

    public LauncherMenu(String name, int priority, LauncherMenu parent) {
        super(name, priority);
        mItems = new PriorityQueue();
        mParent = parent;
        setHasArrow(true);
    }

    public synchronized void doAction() {
        getMenu().postDialog(getCurrentAppId(), true);
        if (mParent != null) {
            mParent.putValue(Action.NAME, mParent.getValue());
        }
    }

    private String getCurrentAppId() {
        try {
            return LipcService.getInstance().getDefaultSource().getTarget("com.lab126.appmgrd").getStringProperty("activeApp");
        } catch (LipcException e) {
            KindleLauncher.LOG.error("Cannot get current running app from lipc.");
            e.printStackTrace();
        }
        return "com.lab126.booklet.home";
    }

    public void initMenu() {
        mDialog = new MenuDialog(getCurrentAppId(), false);
        mDialog.setTitle(this.getValue());
        PriorityQueue copy = new PriorityQueue();
        while (!mItems.isEmpty()) {
            LauncherAction item = (LauncherAction) mItems.remove();
            if (item.hasArrow())
                mDialog.addActionWithIndicator(item);
            else
                mDialog.addAction(item);
            copy.add(item);
        }
        if (mParent != null) {
            mParent.putValue(Action.NAME, "« Previous");
            mDialog.addAction(mParent);
        }
        mItems = copy;
    }

    public void addMenuItem(LauncherAction item) {
        mItems.add(item);
        mDialog = null;
    }

    public void removeMenuItem(LauncherAction item) {
        mItems.remove(item);
        mDialog = null;
    }

    public LauncherAction[] getMenuItems() {
        return (LauncherAction[]) mItems.toArray(new LauncherAction[0]);
    }

    public boolean isMenuEmpty() {
        return mItems.isEmpty();
    }

    public MenuDialog getMenu() {
        if (mDialog == null) {
            this.initMenu();
        }
        return mDialog;
    }

    public void setParent(LauncherMenu parent) {
        this.mParent = parent;
    }

    public LauncherMenu getParent() {
        return this.mParent;
    }
}
