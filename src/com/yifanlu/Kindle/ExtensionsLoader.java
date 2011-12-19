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
import org.json.simple.parser.ParseException;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;
import java.util.ArrayList;

/**
 * This class holds the functions for loading and parsing the XML configs from extensions.
 *
 * @author Yifan Lu
 * @version 1.0
 */
public class ExtensionsLoader {
    private static final LogMessage LOADED_EXTENSION = new LogMessage("ExtensionLoad", new String[]{"name", "version", "author", "id"});
    private static final LogMessage MENU_ATTRIBUTES = new LogMessage("MenuLoad", new String[]{"name", "value"});
    private File mDirectory;

    /**
     * Create a new extension loader using "directory" as the base
     *
     * @param directory Where the extensions are located. Each extension must be in its own folder and contain config.xml.
     */
    public ExtensionsLoader(File directory) {
        mDirectory = directory;
    }

    /**
     * This converts all the extensions into objects using information from their config.xml and returns an array.
     *
     * @return A list of extensions that are loaded or an empty list of no extension is loaded successfully.
     */
    public ArrayList loadExtensions() {
        KXmlParser parser = new KXmlParser();
        ArrayList extList = new ArrayList();
        File[] extensions = mDirectory.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        int i;
        try {
            for (i = 0; i < extensions.length; i++) {
                File ext = extensions[i];
                KindleLauncher.LOG.info("Loading: " + ext.getAbsolutePath());
                File config = new File(ext, "config.xml");
                Extension extObj = new Extension();
                parser.setInput(new InputStreamReader(new FileInputStream(config)));
                parser.nextTag();
                parser.require(XmlPullParser.START_TAG, null, "extension");
                while (parser.nextTag() != XmlPullParser.END_TAG) {
                    parser.require(XmlPullParser.START_TAG, null, null);
                    String name = parser.getName();
                    if (name.equals("information"))
                        parseInformation(parser, extObj);
                    else if (name.equals("menus"))
                        parseMenus(parser, extObj, ext);
                    else //skip
                        while (parser.next() != XmlPullParser.END_TAG || !parser.getName().equals(name))
                            ; // this one line does magic to skip all unrelated events
                    parser.require(XmlPullParser.END_TAG, null, name);
                }
                parser.require(XmlPullParser.END_TAG, null, "extension");
                parser.next();
                parser.require(XmlPullParser.END_DOCUMENT, null, null);
                extList.add(extObj);
                KindleLauncher.LOG.info(LOADED_EXTENSION, new String[]{extObj.getName(), extObj.getVersion(), extObj.getAuthor(), extObj.getId()}, "");
            }
        } catch (IOException e) {
            KindleLauncher.LOG.error("Error reading extension config. " + e.getMessage());
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            KindleLauncher.LOG.error("Error parsing extension config. " + e.getMessage());
            e.printStackTrace();
        } catch (NullPointerException e) {
            KindleLauncher.LOG.error("Error parsing extension menu. Required attribute not found. " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            KindleLauncher.LOG.error("Error parsing extension menu. Cannot find menu Java class. " + e.getMessage());
            e.printStackTrace();
        } catch (ClassCastException e) {
            KindleLauncher.LOG.error("Error parsing extension menu. " + e.getMessage());
            e.printStackTrace();
        } catch (InstantiationException e) {
            KindleLauncher.LOG.error("Error parsing extension menu. Cannot create object from Java class. " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            KindleLauncher.LOG.error("Error parsing extension menu. Cannot create object from Java class. " + e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            KindleLauncher.LOG.error("Error parsing extension menu. Cannot parse JSON menu. " + e.getMessage());
            e.printStackTrace();
        }
        return extList;
    }

    /**
     * This parses the "information" section of the config.xml file. This section contains information about the
     * extension. This information is currently unused, but may be useful in the future so it is recommended that
     * all extensions contain it. The format is
     * <p/>
     * <pre>
     *      &lt;information&gt;
     *          &lt;name&gt;Name of the plugin&lt;/name&gt;
     *          &lt;version&gt;Version of the plugin&lt;/version&gt;
     *          &lt;author&gt;Author of the plugin&lt;/author&gt;
     *          &lt;id&gt;Unique id of the plugin&lt;/id&gt;
     *      &lt;/information&gt;
     * </pre>
     *
     * @param parser {@link org.kxml2.io.KXmlParser}
     * @param extObj The {@link Extension} object to fill.
     * @throws IOException            if the config cannot be read cannot be accessed
     * @throws XmlPullParserException if the config is malformed
     */
    protected void parseInformation(KXmlParser parser, Extension extObj) throws IOException, XmlPullParserException {
        while (parser.nextTag() != XmlPullParser.END_TAG) {
            parser.require(XmlPullParser.START_TAG, null, null);
            String name = parser.getName();
            String text = parser.nextText();

            if (name.equals("name"))
                extObj.setName(text);
            else if (name.equals("version"))
                extObj.setVersion(text);
            else if (name.equals("author"))
                extObj.setAuthor(text);
            else if (name.equals("id"))
                extObj.setId(text);

            parser.require(XmlPullParser.END_TAG, null, name);
        }
    }

    /**
     * This parses the "menus" section of the config.xml file. The "menus" section can contain one or more list of
     * menu options that are loaded by either JSON or Java. All menu options from each list are added to the main menu.
     * The format of the entries in config.xml for the menus section is
     * <p/>
     * <pre>
     *      &lt;menus&gt;
     *          &lt;menu type=&quot;VALUE&quot; jar=&quot;VALUE&quot; dynamic=&quot;VALUE&quot;&gt;TEXT&lt;/menu&gt;
     *      &lt;/menus&gt;
     * </pre>
     * <p/>
     * Where type is either "java" or "json", jar is required only for "java" types and is the path to the Jar file
     * containing the Menuable class relative to config.xml, dynamic is required only for JSON types and tells the
     * loader to re-read the JSON file each time the menu is opened, and the text of the element is either the path
     * to the JSON menu file or the Java class to load.
     *
     * @param parser {@link org.kxml2.io.KXmlParser}
     * @param extObj The {@link Extension} object to fill.
     * @param extDir The path to the extension's directory
     * @throws IOException            if the config cannot be read or the extension directory cannot be accessed
     * @throws XmlPullParserException if the config is malformed
     * @throws ClassNotFoundException if the menu type is Java and the class specified cannot be loaded
     * @throws IllegalAccessException if the menu type is Java and the class specified cannot be accessed
     * @throws InstantiationException if the menu type is Java and the class specified cannot be instantiated
     * @throws ParseException         if the config file cannot be parsed correctly
     */
    protected void parseMenus(KXmlParser parser, Extension extObj, File extDir) throws IOException, XmlPullParserException, ClassNotFoundException, IllegalAccessException, InstantiationException, ParseException {
        while (parser.nextTag() != XmlPullParser.END_TAG) {
            parser.require(XmlPullParser.START_TAG, null, "menu");

            Menuable menu;
            boolean isJava = false;
            boolean isDynamic = false;
            String jarFile = null;
            int i = parser.getAttributeCount();
            KindleLauncher.LOG.debug("Attributes: " + parser.getAttributeCount());
            while (i-- > 0) {
                String name = parser.getAttributeName(i);
                String value = parser.getAttributeValue(i);
                if (name.equals("type"))
                    isJava = value.equals("java");
                else if (name.equals("dynamic"))
                    isDynamic = value.equals("true");
                else if (name.equals("jar"))
                    jarFile = value;
                KindleLauncher.LOG.debug(MENU_ATTRIBUTES, new String[]{name, value}, "");
            }

            String text = parser.nextText();

            if (isJava) {
                JARClassLoader classLoader = JARClassLoader.getInstance();
                classLoader.addJar(new File(extDir, jarFile));
                Class cls = classLoader.loadClass(text);
                menu = (Menuable) cls.newInstance();
                KindleLauncher.LOG.debug("Loaded class: " + text);
            } else {
                File jsonFile = new File(extDir, text);
                JSONMenu jsonMenu = new JSONMenu(jsonFile);
                jsonMenu.setDynamic(isDynamic);
                jsonMenu.parseJSONMenu();
                menu = jsonMenu;
                KindleLauncher.LOG.debug("Loaded menu: " + jsonFile.getAbsolutePath());
            }

            extObj.setMenu(menu);

            parser.require(XmlPullParser.END_TAG, null, "menu");
        }
    }
}
