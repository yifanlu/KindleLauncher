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

import com.amazon.ebook.booklet.reader.sdk.ui.MenuInserter;
import com.amazon.ebook.booklet.reader.sdk.ui.ReaderAction;
import com.amazon.ebook.pl.SystemServices;
import com.amazon.ebook.util.log.Log;
import com.amazon.kindle.restricted.runtime.Framework;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * The main class that is called when the Launcher menu is opened.
 *
 * @author Yifan Lu
 * @version 1.0
 */
public class KindleLauncher extends LauncherMenu implements MenuInserter {
    /**
     * Contains localized text for the menu items.
     */
    public static final ResourceBundle RESOURCES = ResourceBundle.getBundle("com.yifanlu.Kindle.KindleLauncherResources");

    /**
     * Contains Kindle services that can be used
     */
    public static final SystemServices SERVICES = (SystemServices) Framework.getService(SystemServices.class);

    /**
     * The default {@link ExtensionsLoader} that reads from /mnt/us/extensions
     */
    private static final ExtensionsLoader EXTENSIONS_LOADER = new ExtensionsLoader(new File("/mnt/us/extensions"));

    /**
     * The default logger that tags all logs with KindleLauncher
     */
    public static final Log LOG = Log.getInstance("KindleLauncher");


    private static KindleLauncher myInstance;
    private ArrayList mExtensions;

    private KindleLauncher() {
        super(RESOURCES.getString("menu.launcher.label"), 0);
        mExtensions = new ArrayList();
    }

    /**
     * Gets the instance of the singleton.
     *
     * @return the single instance of this class as an {@link Action}
     */
    public static Action getInstance() {
        LOG.debug("We are being called!");
        if (myInstance == null)
            myInstance = new KindleLauncher();
        return myInstance;
    }

    /**
     * Loads the extensions using {@link ExtensionsLoader}
     */
    public void reloadExtensions() {
        LOG.debug("Loading extensions");
        mExtensions = EXTENSIONS_LOADER.loadExtensions();
    }

    /**
     * Creates the main menu for the Launcher using menu items from extensions.
     * We call {@link com.yifanlu.Kindle.KindleLauncher#reloadExtensions()} if the extensions have not been loaded.
     * Then, we tell each extensions' {@link Menuable} to {@link Menuable#addItemsToMenu(LauncherMenu)} with the main menu.
     */
    public void createMenu() {
        if (mExtensions.isEmpty() || isMenuEmpty())
            reloadExtensions();
        clearMenu();
        Iterator it = mExtensions.iterator();
        while (it.hasNext()) {
            Extension ext = (Extension) it.next();
            ext.getMenu().addItemsToMenu(this);
        }
    }

    /**
     * Called when user chooses the Launcher item from the Kindle menu.
     * This calls the menu creation method.
     *
     * @see com.yifanlu.Kindle.KindleLauncher#createMenu()
     */
    public synchronized void doAction() {
        createMenu();
        super.doAction();
    }

    /**
     * The Kindle Reader SDK tells plugins to give a list of menu items to add to the Kindle menu. We will add ourselves
     * to it.
     *
     * @return an array containing this {@link KindleLauncher} object
     */
    public ReaderAction[] getMenuActions() {
        return new ReaderAction[]{this};
    }
}
