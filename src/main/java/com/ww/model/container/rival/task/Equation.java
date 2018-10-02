package com.ww.model.container.rival.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Equation {
    private Integer value;
    private String leftSide = "";
    private String rightSide = "";

    public void appendLeftSide(String v) {
        leftSide += v;
    }

    private String add(Integer v){
        if (v > 0) {
            return "+" + v;
        } else {
            return v+"";
        }
    }

    public void addLeftSide(Integer v) {
        leftSide += add(v);
    }

    public void addRightSide(Integer v) {
        rightSide += add(v);
    }

    public void appendRightSide(String v) {
        rightSide += v;
    }

    public String getEquation() {
        return leftSide + " = " + rightSide;
    }

    public Equation(Integer value) {
        this.value = value;
    }
}
