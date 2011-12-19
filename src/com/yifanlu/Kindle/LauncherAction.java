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

import com.amazon.ebook.booklet.reader.sdk.ui.ReaderAction;
import com.amazon.ebook.util.thread.ThreadPool;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * A menu item for Launcher. Contains the item's text, its priority, and the task it should perform when called.
 *
 * @author Yifan Lu
 * @version 1.0
 */
public abstract class LauncherAction extends ReaderAction implements Runnable {
    private int mPriority;
    private String mName;
    private boolean mHasArrow;

    /**
     * Creates a new menu item with the text to show.
     *
     * @param name The text of the menu item.
     */
    public LauncherAction(String name) {
        this(name, ReaderAction.PRIORITY_PLUGINS);
    }

    /**
     * Creates a new menu item with the text to show and the priority.
     *
     * @param name     The text of the menu item.
     * @param priority The order to show the item.
     */
    public LauncherAction(String name, int priority) {
        this(name, priority, null);
    }

    /**
     * Creates a new menu item with the text to show, the priority, and an icon.
     *
     * @param name     The text of the menu item.
     * @param priority The order to show the item.
     * @param icon     Unused.
     */
    public LauncherAction(String name, int priority, Icon icon) {
        super(name, icon);
        this.mPriority = priority;
        this.mName = name;
        this.mHasArrow = false;
        this.setEnabled(true);
    }

    /**
     * Called when the menu item is tapped by the user. It creates a new thread and runs it.
     *
     * @param actionEvent The action performed
     */
    public final void actionPerformed(ActionEvent actionEvent) {
        ThreadPool.getInstance().runIt(this, "Launcher");
    }

    /**
     * Called when the new thread is ran. It wraps {@link com.yifanlu.Kindle.LauncherAction#doAction()}
     */
    public final void run() {
        doAction();
    }

    /**
     * The value is the text label of the item
     *
     * @return the text label of the item
     */
    public String getValue() {
        return this.mName;
    }

    /**
     * This is the task the item performs when called. Overwritten by subclasses.
     */
    public abstract void doAction();

    /**
     * Compares this item to another one. They are compared first by priority and next by their text labels.
     *
     * @param other The other object.
     * @return The difference in priority between the two or {@link String#compareTo(Object)} if equal.
     */
    public int compareTo(Object other) {
        if (!(other instanceof LauncherAction))
            return -1;
        if (this.mPriority == ((LauncherAction) other).mPriority)
            return this.mName.compareTo(((LauncherAction) other).mName);
        return this.mPriority - ((LauncherAction) other).mPriority;
    }

    /**
     * Sets the visibility of the arrow
     *
     * @param hasArrow Does this item have an arrow?
     * @see com.yifanlu.Kindle.LauncherMenu#hasArrow()
     */
    public void setHasArrow(boolean hasArrow) {
        mHasArrow = hasArrow;
    }

    /**
     * If true, the menu item will have a small arrow to the far right of the text.
     *
     * @return Does this item have an arrow?
     */
    public boolean hasArrow() {
        return mHasArrow;
    }

    /**
     * The display order of this item as specified on creation.
     *
     * @return The priority of the item
     */
    public int getPriority() {
        return mPriority;
    }

    /**
     * Tells the Kindle Reader SDK that this item should be enabled.
     *
     * @return The SDK type of the item
     */
    public int getType() {
        return ReaderAction.TYPE_ALL;
    }
}
