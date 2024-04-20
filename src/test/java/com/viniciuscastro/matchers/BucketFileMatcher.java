package com.viniciuscastro.matchers;

import org.mockito.ArgumentMatcher;

import com.viniciuscastro.presentation.models.BucketFile;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BucketFileMatcher implements ArgumentMatcher<BucketFile> {
    private BucketFile file;

    @Override
    public boolean matches(BucketFile fileCandidate) {
        return fileCandidate.getFilename().equals(file.getFilename());
    }
}
