package com.ww.manager.rival.war.state;

import com.ww.manager.rival.state.AbstractState;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.RivalTeam;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Getter
public abstract class WarState extends AbstractState {
    protected static final Logger logger = LoggerFactory.getLogger(WarState.class);

    protected WarManager manager;

    protected WarState(WarManager manager, String type) {
        this.manager = manager;
        setType(type);
    }

    @Override
    public String describe() {
        return manager.toString();
    }

    @Override
    public boolean isRunning() {
        return !manager.isClosed();
    }

    protected void sendNewSkillsModel(BiConsumer<Map<String, Object>, ? super RivalTeam> fillMethod) {
        manager.getModel().getTeams().forEachTeam(rivalTeam -> {
            Map<String, Object> model = new HashMap<>();
            fillMethod.accept(model, rivalTeam);
            manager.getModelFactory().fillModelSkills(model, rivalTeam);
            manager.send(model, manager.getMessageContent(), rivalTeam.getProfileId());
        });
    }

    protected void sendNewSkillsModel() {
        sendNewSkillsModel((m, wT) -> {
        });
    }
}

