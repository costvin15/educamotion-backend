package com.viniciuscastro.slides.resources;

public enum MimeType {
    PRESENTATION("mimeType='application/vnd.google-apps.presentation'");

    public String mimeType;

    MimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
