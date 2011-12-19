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

import com.amazon.ebook.util.log.LogMessage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * This classloader allows loading JARs from any URL and does not require the URL to be passed on initiation.
 *
 * @author Yifan Lu
 * @version 1.0
 */
public class JARClassLoader extends URLClassLoader {
    private static JARClassLoader myInstance;
    private static final LogMessage JAR_LOAD_ERROR = new LogMessage("JarLoad", new String[]{"jar file", "java error"});

    private JARClassLoader() {
        super(new URL[]{});
    }

    /**
     * Adds an URL to the class path.
     *
     * @param url The URL of the class path.
     */
    public void addURL(URL url) {
        super.addURL(url);
    }

    /**
     * We are a singleton, this gets the single instance of this class.
     *
     * @return The single instance of this class loader.
     */
    public static JARClassLoader getInstance() {
        if (myInstance == null)
            myInstance = new JARClassLoader();
        return myInstance;
    }

    /**
     * Adds a JAR file to the class path.
     *
     * @param jarFile The {@link java.io.File} that contains the JAR to load.
     */
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
