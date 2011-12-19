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

import com.amazon.ebook.booklet.reader.sdk.BookException;
import com.amazon.ebook.booklet.reader.sdk.ReaderPlugin;
import com.amazon.ebook.booklet.reader.sdk.ReaderSDK;
import com.amazon.ebook.booklet.reader.sdk.ui.MenuInserter;

/**
 * A Kindle Reader SDK plugin that adds the Launcher menu item to the main Kindle menu.
 *
 * @author Yifan Lu
 * @version 1.0
 * @see KindleLauncher
 */
public class ReaderPluginLauncher extends ReaderPlugin {
    private ReaderSDK mSdk;

    /**
     * Called by the plugin loader. Adds the Launcher menu item to the main menu.
     *
     * @param readerSDK The SDK object
     * @throws BookException if there is a problem creating the plugin
     */
    public void pluginLoad(ReaderSDK readerSDK) throws BookException {
        mSdk = readerSDK;
        mSdk.getRegistry().registerMenuInserter((MenuInserter) KindleLauncher.getInstance());
    }

    /**
     * Called by the plugin unloader. Removes the Launcher menu item to the main menu.
     */
    public void pluginUnload() {
        mSdk.getRegistry().deregisterMenuInserter((MenuInserter) KindleLauncher.getInstance());
    }

    /**
     * The name of the plugin.
     *
     * @return The name of the plugin.
     */
    public String getName() {
        return "Launcher";
    }
}
