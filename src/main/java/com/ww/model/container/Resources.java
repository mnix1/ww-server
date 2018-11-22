package com.ww.model.container;

import com.ww.model.constant.social.ResourceType;
import lombok.Getter;

@Getter
public class Resources {
    private Long gold;
    private Long crystal;
    private Long wisdom;
    private Long elixir;

    public Resources(Long gold) {
        write(ResourceType.GOLD, gold);
    }

    public Resources(Long gold, Long crystal) {
        this(gold);
        write(ResourceType.CRYSTAL, crystal);
    }

    public Resources(Long gold, Long crystal, Long wisdom) {
        this(gold, crystal);
        write(ResourceType.WISDOM, wisdom);
    }

    public Resources(Long gold, Long crystal, Long wisdom, Long elixir) {
        this(gold, crystal, wisdom);
        write(ResourceType.ELIXIR, elixir);
    }

    public Resources(ResourceType type, Long value) {
        write(type, value);
    }

    public Resources(Resources resources) {
        for (ResourceType type : ResourceType.values()) {
            write(type, resources.read(type));
        }
    }

    public Resources write(ResourceType type, Long value) {
        if (type == ResourceType.GOLD) {
            this.gold = value;
        } else if (type == ResourceType.CRYSTAL) {
            this.crystal = value;
        } else if (type == ResourceType.WISDOM) {
            this.wisdom = value;
        } else if (type == ResourceType.ELIXIR) {
            this.elixir = value;
        }
        return this;
    }

    public Long read(ResourceType type) {
        if (type == ResourceType.GOLD) {
            return gold;
        } else if (type == ResourceType.CRYSTAL) {
            return crystal;
        } else if (type == ResourceType.WISDOM) {
            return wisdom;
        } else if (type == ResourceType.ELIXIR) {
            return elixir;
        }
        return null;
    }

    public boolean check(ResourceType type) {
        return read(type) != null;
    }


    public boolean hasNotLessThan(Resources resources) {
        for (ResourceType type : ResourceType.values()) {
            if (resources.check(type) && read(type) < resources.read(type)) {
                return false;
            }
        }
        return true;
    }

    public Resources subtract(Resources resources) {
        for (ResourceType type : ResourceType.values()) {
            if (resources.check(type)) {
                if (!check(type)) {
                    write(type, 0L);
                }
                write(type, read(type) - resources.read(type));
            }
        }
        return this;
    }

    public Resources add(Resources resources) {
        for (ResourceType type : ResourceType.values()) {
            if (resources.check(type)) {
                if (!check(type)) {
                    write(type, 0L);
                }
                write(type, read(type) + resources.read(type));
            }
        }
        return this;
    }

    public Resources multiply(int multiplier) {
        for (ResourceType type : ResourceType.values()) {
            if (!check(type)) {
                write(type, read(type) * multiplier);
            }
        }
        return this;
    }

    public Long lowest() {
        Long lowest = null;
        for (ResourceType type : ResourceType.values()) {
            if (check(type) && (lowest == null || read(type) < lowest)) {
                lowest = read(type);
            }
        }
        return lowest;
    }

    public Long highest() {
        Long highest = null;
        for (ResourceType type : ResourceType.values()) {
            if (check(type) && (highest == null || read(type) > highest)) {
                highest = read(type);
            }
        }
        return highest;
    }

    public boolean getEmpty() {
        return !((gold != null && gold > 0)
                || (crystal != null && crystal > 0)
                || (wisdom != null && wisdom > 0)
                || (elixir != null && elixir > 0));
    }

}