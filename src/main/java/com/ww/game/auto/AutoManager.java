package com.ww.game.auto;

import com.ww.game.auto.communication.AutoCommunication;
import com.ww.game.auto.container.AutoPlayContainer;
import com.ww.game.auto.flow.AutoFlow;
import com.ww.model.container.InsideConnection;
import com.ww.model.entity.inside.social.InsideProfile;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.auto.AutoService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class AutoManager {
    public static Logger logger = LoggerFactory.getLogger(AutoManager.class);
    private AutoService autoService;
    @Setter
    protected Profile profile;
    protected InsideProfile insideProfile;
    @Setter
    protected InsideConnection connection;
    protected AutoFlow flow;
    protected AutoCommunication communication;
    @Setter
    protected AutoPlayContainer autoPlayContainer;

    protected AutoManager(Profile profile, InsideProfile insideProfile) {
        this.profile = profile;
        this.insideProfile = insideProfile;
    }

    public AutoManager(Profile profile, InsideProfile insideProfile, AutoService autoService) {
        this(profile, insideProfile);
        this.autoService = autoService;
        this.flow = new AutoFlow(this);
        this.communication = new AutoCommunication(this);
    }

    public void disposeContainer() {
        if (autoPlayContainer != null) {
            autoPlayContainer.dispose();
            setAutoPlayContainer(null);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "profile=" + profile +
                '}';
    }

    public void manageBooks() {
        autoService.manageBooks(profile);
    }

    public void manageMails() {
        autoService.manageMails(profile);
    }

    public void startRival() {
        autoService.startRival(this);
    }

    public void upgradeWisies() {
        autoService.upgradeWisies(profile);
    }

    public void dispose() {
        autoService.disposeManager(this);
    }

    public void initAutoPlayContainer() {
        autoPlayContainer = new AutoPlayContainer(autoService.getPlayManager(profile), profile);
    }
}
