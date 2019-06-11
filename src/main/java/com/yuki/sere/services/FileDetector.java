package com.yuki.sere.services;

import java.io.*;

/**
 * Created by fan on 2019/6/11.
 */
public class FileDetector {

    private long lastModified;

    private final File htmlEmailTemp;

    private String path;

    public String getPath() {
        return path;
    }

    public FileDetector setPath(String path) {
        this.path = path;
        return this;
    }

    public FileDetector(String path) {
        this.path = path;
        htmlEmailTemp = new File(path);
    }

    public boolean isChange() {
        if (lastModified == 0 || htmlEmailTemp.lastModified() != lastModified) {
            lastModified = htmlEmailTemp.lastModified();
            return true;
        }
        return false;
    }

    public String getHtmlEmailTemp() throws IOException {
        InputStream inputStream = new BufferedInputStream(new FileInputStream(htmlEmailTemp));
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        return new String(buffer);
    }
}
