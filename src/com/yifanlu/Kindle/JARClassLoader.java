package com.yifanlu.Kindle;

import com.amazon.ebook.util.log.LogMessage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/15/11
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class JARClassLoader extends URLClassLoader {
    private static JARClassLoader myInstance;
    private static final LogMessage JAR_LOAD_ERROR = new LogMessage("JarLoad", new String[]{"jar file", "java error"});
    private static final LogMessage CLASS_LOAD_ERROR = new LogMessage("ClassLoad", new String[]{"class", "java error"});

    public JARClassLoader() {
        super(new URL[]{});
    }

    public void addURL(URL url) {
        super.addURL(url);
    }

    public static JARClassLoader getInstance() {
        if (myInstance == null)
            myInstance = new JARClassLoader();
        return myInstance;
    }

    public void addJar(File jarFile) {
        try {
            URL jarURL = jarFile.toURI().toURL();
            addURL(jarURL);
        } catch (MalformedURLException e) {
            KindleLauncher.LOG.error(JAR_LOAD_ERROR, new String[]{jarFile.getAbsolutePath(), e.toString()}, "");
            e.printStackTrace();
        }
    }
}
