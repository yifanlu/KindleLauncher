package com.yifanlu.Kindle;

import com.amazon.ebook.booklet.reader.sdk.ui.MenuInserter;
import com.amazon.ebook.booklet.reader.sdk.ui.ReaderAction;
import com.amazon.ebook.util.thread.ThreadPool;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/14/11
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class LauncherAction extends ReaderAction implements Runnable, Actionable {
    private int mPriority;
    private String mName;
    private boolean mHasArrow;
    
    public LauncherAction(String name, int priority){
        this(name, priority, null);
    }

    public LauncherAction(String name, int priority, Icon icon){
        super(name, icon);
        this.mPriority = priority;
        this.mName = name;
        this.mHasArrow = false;
    }

    public final void actionPerformed(ActionEvent actionEvent) {
        ThreadPool.getInstance().runIt(this, "Launcher");
    }

    public void run() {
        doAction();
    }
    
    public String getValue(){
        return this.mName;
    }

    public abstract void doAction();
    
    public int compareTo(Object other){
        if(!(other instanceof LauncherAction))
            return -1;
        if(this.mPriority == ((LauncherAction)other).mPriority)
            return this.mName.compareTo(((LauncherAction)other).mName);
        return this.mPriority - ((LauncherAction)other).mPriority;
    }

    public void setHasArrow(boolean hasArrow){
        mHasArrow = hasArrow;
    }

    public boolean hasArrow(){
        return mHasArrow;
    }

    public int getPriority(){
        return mPriority;
    }

    public int getType() {
        return ReaderAction.TYPE_PLUGINS;
    }
}
