package com.ww.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Session implements Serializable {
    Long profileId;
    String profileTag;
}
