package com.viniciuscastro.models.googleslide;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleSlide {
    public String presentationId;
    public String title;
    public SlideSize size;
    public Page[] slides;
}
