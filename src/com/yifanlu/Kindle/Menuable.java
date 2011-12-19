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
 * Represents an object that stores menu items. All Java menu extensions must implement this.
 *
 * @author Yifan Lu
 * @version 1.0
 * @see ExtensionsLoader
 */
public interface Menuable {
    /**
     * Adds menu items to the parent menu. The parent is usually the main Launcher menu.
     *
     * @param menu The parent menu.
     */
    public void addItemsToMenu(LauncherMenu menu);
}
