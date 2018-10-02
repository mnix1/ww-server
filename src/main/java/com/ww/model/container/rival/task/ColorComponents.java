package com.ww.model.container.rival.task;

import lombok.Getter;
import lombok.Setter;

import java.awt.Color;

import static com.ww.helper.ColorHelper.colorToSumInt;
import static com.ww.helper.ColorHelper.percentComponent;

@Getter
@Setter
public class ColorComponents {
    private Color color;
    private boolean calculatedComponents = false;
    private double redPercentComponent;
    private double greenPercentComponent;
    private double bluePercentComponent;
    private int sumComponent;

    public ColorComponents(Color color) {
        this.color = color;
    }

    public ColorComponents(int r, int g, int b) {
        this.color = new Color(r, g, b);
    }

    public int getRed() {
        return color.getRed();
    }

    public int getGreen() {
        return color.getGreen();
    }

    public int getBlue() {
        return color.getBlue();
    }

    public void calculatePercentComponents() {
        if (isCalculatedComponents()) {
            return;
        }
        sumComponent = colorToSumInt(color);
        redPercentComponent = percentComponent(color.getRed(), sumComponent);
        greenPercentComponent = percentComponent(color.getGreen(), sumComponent);
        bluePercentComponent = percentComponent(color.getBlue(), sumComponent);
        calculatedComponents = true;
    }
}
