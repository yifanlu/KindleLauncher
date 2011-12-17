package com.yifanlu.Kindle;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/16/11
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtensionsLoader {
    public static final String EXTENSIONS_DIRECTORY = "/mnt/us/extensions";
    private File mDirectory;

    public ExtensionsLoader(File directory) {
        mDirectory = directory;
    }

    public void loadExtensions() {
        KXmlParser parser = new KXmlParser();
        File[] extensions = mDirectory.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        int i;
        try {
            for (i = 0; i < extensions.length; i++) {
                File ext = extensions[i];
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
            KindleLauncher.LOG.error("Error parsing extension menu. Java class not implementing Menuable. " + e.getMessage());
            e.printStackTrace();
        } catch (InstantiationException e) {
            KindleLauncher.LOG.error("Error parsing extension menu. Cannot create object from Java class. " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            KindleLauncher.LOG.error("Error parsing extension menu. Cannot create object from Java class. " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void parseMenus(KXmlParser parser, Extension extObj, File extDir) throws IOException, XmlPullParserException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        while (parser.nextTag() != XmlPullParser.END_TAG) {
            parser.require(XmlPullParser.START_TAG, null, "menu");

            String text = parser.nextText();
            Menuable menu = null;
            boolean isJava = false;
            boolean isDynamic = false;
            String jarFile = null;

            int i = parser.getAttributeCount();
            while (i-- > 0) {
                String name = parser.getAttributeName(i);
                String value = parser.getAttributeValue(i);
                if (name.equals("type"))
                    isJava = value.equals("java");
                else if (name.equals("dynamic"))
                    isDynamic = value.equals("true");
                else if (name.equals("jar"))
                    jarFile = value;
            }

            if (isJava) {
                JARClassLoader classLoader = JARClassLoader.getInstance();
                classLoader.addJar(new File(extDir, jarFile));
                Class cls = classLoader.loadClass(text);
                menu = (Menuable) cls.newInstance();
            } else {
                JSONMenu jsonMenu = new JSONMenu(new File(extDir, text));
                jsonMenu.setDynamic(isDynamic);
                jsonMenu.parseJSONMenu();
                menu = jsonMenu;
            }

            extObj.setMenu(menu);

            parser.require(XmlPullParser.END_TAG, null, "menu");
        }
    }

    private void parseInformation(KXmlParser parser, Extension extObj) throws IOException, XmlPullParserException {
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
}
