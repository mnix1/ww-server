package com.ww.game.training;

import com.ww.game.auto.AutoManager;
import com.ww.game.auto.communication.AutoCommunication;
import com.ww.game.auto.container.AutoPlayContainer;
import com.ww.game.auto.flow.AutoFlow;
import com.ww.game.play.PlayManager;
import com.ww.model.container.TrainingProfileConnection;
import com.ww.model.entity.inside.social.InsideProfile;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

@Getter
public class TrainingManager extends AutoManager {
    public static Logger logger = LoggerFactory.getLogger(TrainingManager.class);

    private PlayManager playManager;

    public TrainingManager(Profile profile, PlayManager playManager) {
        super(profile, new InsideProfile().initStats(.2, .2, .6, .5, .5, new HashSet<>()));
        this.playManager = playManager;
        this.connection = new TrainingProfileConnection(this);
        this.flow = new AutoFlow(this);
        this.communication = new AutoCommunication(this);
        this.flow.resume();
    }

    @Override
    public void dispose() {
        disposeContainer();
    }

    @Override
    public void initAutoPlayContainer() {
        autoPlayContainer = new AutoPlayContainer(playManager, profile);
    }

}
