package com.bath.tcpviz.dis.fileupload.controller;

import org.springframework.stereotype.Component;

@Component
public class ShareFileName {

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


}
