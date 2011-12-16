package com.yifanlu.Kindle;

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
    public JARClassLoader() {
        super(new URL[]{});
    }

    public void addURL(URL url){
        super.addURL(url);
    }
}
