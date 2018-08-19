package com.ww.model.container;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ww.helper.RandomHelper.randomInteger;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Interval {
    private int hours;
    private int minutes;
    private int seconds;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval interval = (Interval) o;
        return hours == interval.hours &&
                minutes == interval.minutes &&
                seconds == interval.seconds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes, seconds);
    }

    public static Interval randomInterval(int maxHours, int maxMinutes, int maxSeconds) {
        Interval interval = new Interval();
        if (maxHours > 0) {
            interval.setHours(randomInteger(1, maxHours));
        }
        if (maxMinutes > 0) {
            interval.setMinutes(randomInteger(1, maxMinutes));
        }
        if (maxSeconds > 0) {
            interval.setSeconds(randomInteger(1, maxSeconds));
        }
        return interval;
    }

    @Override
    public String toString() {
        List<String> parts = new ArrayList<>();
        if (hours > 0) {
            parts.add(hours + "h");
        }
        if (minutes > 0) {
            parts.add(minutes + "m");
        }
        if (seconds > 0) {
            parts.add(seconds + "s");
        }
        return StringUtils.join(parts, " ");
    }
}
