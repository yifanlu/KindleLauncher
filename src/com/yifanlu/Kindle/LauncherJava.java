package com.yifanlu.Kindle;

import com.amazon.ebook.booklet.reader.sdk.ui.ReaderAction;
import com.amazon.ebook.util.log.LogMessage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/14/11
 * Time: 9:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class LauncherJava extends LauncherAction {
    private Class mClassToLoad;
    private static final JARClassLoader classLoader = new JARClassLoader();
    private static final LogMessage JAR_LOAD_ERROR = new LogMessage("JarLoad", new String[]{"jar file", "java error"});
    private static final LogMessage CLASS_LOAD_ERROR = new LogMessage("ClassLoad", new String[]{"class", "java error"});
    
    public LauncherJava(String name, int priority, File jarFile, String classToUse) {
        super(name, priority);
        mClassToLoad = loadClass(jarFile, classToUse);
    }
    
    private Class loadClass(File jarFile, String classToUse){
        try {
            URL jarURL = jarFile.toURI().toURL();
            classLoader.addURL(jarURL);
            return classLoader.loadClass(classToUse);
        } catch (MalformedURLException e) {
            KindleLauncher.LOG.error(JAR_LOAD_ERROR, new String[]{ jarFile.getAbsolutePath(), e.toString() }, "");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            KindleLauncher.LOG.error(JAR_LOAD_ERROR, new String[]{ jarFile.getAbsolutePath(), e.toString() }, "");
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void doAction() {
        try {
            Object obj = mClassToLoad.newInstance();
            if(!(obj instanceof Actionable)){
                throw new ClassCastException("Class has to implement Actionable");
            }
            ((Actionable)obj).doAction();
        } catch (InstantiationException e) {
            KindleLauncher.LOG.error(CLASS_LOAD_ERROR, new String[]{ mClassToLoad.toString(), e.toString() }, "");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            KindleLauncher.LOG.error(CLASS_LOAD_ERROR, new String[]{ mClassToLoad.toString(), e.toString() }, "");
            e.printStackTrace();
        }
    }
}
