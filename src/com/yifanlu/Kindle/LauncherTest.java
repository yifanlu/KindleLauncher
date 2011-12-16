package com.yifanlu.Kindle;

import com.amazon.ebook.util.log.Log;
import com.amazon.ebook.util.log.LogMessage;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/15/11
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class LauncherTest extends LauncherAction {
    public LauncherTest(String name) {
        this(name, 10);
    }
    
    public LauncherTest(String name, int priority){
        super(name, priority);
    }

    public synchronized void doAction() {
        KindleLauncher.LOG.info("Clicked " + getValue() + "!");
    }
}
