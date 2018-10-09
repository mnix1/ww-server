package com.ww.model;

import com.ww.model.constant.Language;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.language.bm.Lang;

import java.io.Serializable;

@Getter
@Setter
public class Session implements Serializable {
    Long profileId;
    String profileTag;
    Language language;
}
