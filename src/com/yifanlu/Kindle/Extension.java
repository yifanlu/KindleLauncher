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

/**
 * This class represents the extension and is parsed by the config.xml file.
 * Currently, the only thing of use is the {@link Menuable}, which
 * holds the menu items.
 *
 * @author Yifan Lu
 * @version 1.0
 */
public class Extension {
    private String mName;
    private String mVersion;
    private String mAuthor;
    private String mId;
    private Menuable mMenu;

    /**
     * We want all the fields to be set by the parser, but we place default values in just in case.
     */
    public Extension() {
        this.mName = "Plugin";
        this.mVersion = "0.0";
        this.mAuthor = "Author";
        this.mId = super.toString();
    }

    /**
     * Sets the name of the extension.
     *
     * @param name The name of the extension.
     */
    public void setName(String name) {
        this.mName = name;
    }

    /**
     * Gets the name of the extension.
     * This is currently unused.
     *
     * @return The name of the extension.
     */
    public String getName() {
        return this.mName;
    }

    /**
     * Sets the version of the extension.
     *
     * @param version The version of the extension.
     */
    public void setVersion(String version) {
        this.mVersion = version;
    }

    /**
     * Gets the version of the extension.
     * This is currently unused.
     *
     * @return The version of the extension.
     */
    public String getVersion() {
        return this.mVersion;
    }

    /**
     * Sets the author of the extension.
     *
     * @param author The author of the extension.
     */
    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    /**
     * Gets the author of the extension.
     * This is currently unused.
     *
     * @return The author of the extension.
     */
    public String getAuthor() {
        return this.mAuthor;
    }

    /**
     * Sets the id of the extension. Must be unique.
     *
     * @param id The id of the extension.
     */
    public void setId(String id) {
        this.mId = id;
    }

    /**
     * Gets the id of the extension.
     * This is currently unused.
     *
     * @return The id of the extension.
     */
    public String getId() {
        return this.mId;
    }

    /**
     * Gets the menu options for this extension.
     *
     * @return The menu options for this extension.
     * @see com.yifanlu.Kindle.Menuable
     */
    public Menuable getMenu() {
        return this.mMenu;
    }

    /**
     * Sets the menu options for this extension.
     *
     * @param menu The menu option for this extension.
     * @see com.yifanlu.Kindle.Menuable
     */
    public void setMenu(Menuable menu) {
        this.mMenu = menu;
    }
}
