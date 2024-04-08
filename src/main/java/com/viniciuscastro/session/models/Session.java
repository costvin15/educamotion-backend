package com.viniciuscastro.session.models;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.cloud.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Session {
    private String id;
    private String code;
    private String presentationId;
    private Boolean active = true;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Session(String presentationId) {
        this.code = Session.generateCode();
        this.presentationId = presentationId;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }

    private static String generateCode() {
        return RandomStringUtils.random(6, true, true);
    }
}
