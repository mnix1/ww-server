package com.ww.manager.rival.war.skill;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.skill.state.*;
import com.ww.model.constant.rival.RivalStatus;
import lombok.Getter;

import java.util.Map;

import static com.ww.service.rival.global.RivalMessageService.*;

@Getter
public class WarSkillFlow {

    private WarManager manager;

    public WarSkillFlow(WarManager manager) {
        this.manager = manager;
    }

    public boolean processMessage(Long profileId, Map<String, Object> content) {
        String id = (String) content.get("id");
        RivalStatus status = getManager().getModel().getStatus();
        if (id.equals(HINT) && status == RivalStatus.ANSWERING) {
            hint(profileId, content);
        } else if (id.equals(WATER_PISTOL) && status == RivalStatus.ANSWERING) {
            waterPistol(profileId);
        } else if (id.equals(LIFEBUOY) && status == RivalStatus.CHOOSING_WHO_ANSWER) {
            lifebuoy(profileId, content);
        } else if (id.equals(KIDNAPPING) && status == RivalStatus.ANSWERING) {
            kidnapping(profileId);
        } else if (id.equals(GHOST) && status == RivalStatus.ANSWERING) {
            ghost(profileId);
        } else {
            return false;
        }
        return true;
    }

    public synchronized void hint(Long profileId, Map<String, Object> content) {
        new WarStateHintUsed(manager, profileId, content).startVoid();
    }

    public synchronized void waterPistol(Long profileId) {
        new WarStateWaterPistolUsed(manager, profileId).startVoid();
    }

    public synchronized void lifebuoy(Long profileId, Map<String, Object> content) {
        new WarStateLifebuoyUsed(manager, profileId, content).startVoid();
    }

    public synchronized void kidnapping(Long profileId) {
        new WarStateKidnappingUsed(manager, profileId).startVoid();
    }

    public synchronized void ghost(Long profileId) {
        new WarStateGhostUsed(manager, profileId).startVoid();
    }
}
