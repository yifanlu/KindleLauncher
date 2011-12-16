package com.yifanlu.Kindle;

import com.amazon.ebook.booklet.reader.sdk.BookException;
import com.amazon.ebook.booklet.reader.sdk.ReaderPlugin;
import com.amazon.ebook.booklet.reader.sdk.ReaderSDK;
import com.amazon.ebook.booklet.reader.sdk.ui.MenuInserter;

/**
 * Created by IntelliJ IDEA.
 * User: yifanlu
 * Date: 12/15/11
 * Time: 9:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReaderPluginLauncher extends ReaderPlugin {
    private ReaderSDK mSdk;

    public void pluginLoad(ReaderSDK readerSDK) throws BookException {
        mSdk = readerSDK;
        mSdk.getRegistry().registerMenuInserter((MenuInserter) KindleLauncher.getInstance());
    }

    public void pluginUnload() {
        mSdk.getRegistry().deregisterMenuInserter((MenuInserter) KindleLauncher.getInstance());
    }

    public String getName() {
        return "Launcher";
    }
}
