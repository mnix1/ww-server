package com.ww.service.rival;

import com.ww.model.constant.wisie.WisorType;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.rival.task.TaskService;
import com.ww.service.social.ConnectionService;
import com.ww.service.wisie.ProfileWisieService;
import lombok.Setter;
import org.springframework.stereotype.Service;

import static com.ww.helper.TeamHelper.BOT_PROFILE_ID;

@Service
public class RivalWisieComputerService extends RivalWisieService {


    public RivalWisieComputerService(ConnectionService connectionService, TaskGenerateService taskGenerateService, TaskRendererService taskRendererService, RivalGlobalService rivalGlobalService, RivalProfileSeasonService rivalProfileSeasonService, ProfileWisieService profileWisieService, TaskService taskService) {
        super(connectionService, taskGenerateService, taskRendererService, rivalGlobalService, rivalProfileSeasonService, profileWisieService, taskService);
    }

    public Profile prepareComputerProfile() {
        Profile computerProfile = new Profile();
        computerProfile.setId(BOT_PROFILE_ID);
        computerProfile.setName("");
        computerProfile.setWisorType(WisorType.random());
        computerProfile.setTag("0");
        return computerProfile;
    }
}
