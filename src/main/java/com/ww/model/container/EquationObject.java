package com.ww.model.container;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquationObject {
    private Integer value;
    private String leftSide = "";
    private String rightSide = "";

    public void appendLeftSide(String v) {
        leftSide += v;
    }

    public void addLeftSide(Integer v) {
        if (v > 0) {
            leftSide += "+" + v;
        } else {
            leftSide += v;
        }
    }

    public void appendRightSide(String v) {
        rightSide += v;
    }

    public String getEquation() {
        return leftSide + " = " + rightSide;
    }

    public EquationObject(Integer value) {
        this.value = value;
    }
}
