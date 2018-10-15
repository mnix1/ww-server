package com.ww.model.container;

import com.ww.model.constant.social.ResourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Resources {
    private Long gold;
    private Long crystal;
    private Long wisdom;
    private Long elixir;

    public Resources(Long gold) {
        this.gold = gold;
    }

    public Resources(Long gold, Long crystal) {
        this(gold);
        this.crystal = crystal;
    }

    public Resources(Long gold, Long crystal, Long wisdom) {
        this(gold, crystal);
        this.wisdom = wisdom;
    }

    public Resources(Long gold, Long crystal, Long wisdom, Long elixir) {
        this(gold, crystal, wisdom);
        this.elixir = elixir;
    }

    public Resources(ResourceType type, Long value) {
        if (type == ResourceType.GOLD) {
            this.gold = value;
        } else if (type == ResourceType.CRYSTAL) {
            this.crystal = value;
        } else if (type == ResourceType.WISDOM) {
            this.wisdom = value;
        } else if (type == ResourceType.ELIXIR) {
            this.elixir = value;
        }
    }

    public boolean hasNotLessThan(Resources resources) {
        if (resources.gold != null && gold < resources.gold) {
            return false;
        }
        if (resources.crystal != null && crystal < resources.crystal) {
            return false;
        }
        if (resources.wisdom != null && wisdom < resources.wisdom) {
            return false;
        }
        if (resources.elixir != null && elixir < resources.elixir) {
            return false;
        }
        return true;
    }

    public Resources subtract(Resources resources) {
        if (resources.gold != null) {
            gold -= resources.gold;
        }
        if (resources.crystal != null) {
            crystal -= resources.crystal;
        }
        if (resources.wisdom != null) {
            wisdom -= resources.wisdom;
        }
        if (resources.elixir != null) {
            elixir -= resources.elixir;
        }
        return this;
    }

    public Resources add(Resources resources) {
        if (resources.gold != null) {
            gold += resources.gold;
        }
        if (resources.crystal != null) {
            crystal += resources.crystal;
        }
        if (resources.wisdom != null) {
            wisdom += resources.wisdom;
        }
        if (resources.elixir != null) {
            elixir += resources.elixir;
        }
        return this;
    }

    public boolean getEmpty() {
        return !((gold != null && gold > 0)
                || (crystal != null && crystal > 0)
                || (wisdom != null && wisdom > 0)
                || (elixir != null && elixir > 0));
    }

}