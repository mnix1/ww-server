package com.ww.service.rival.init;

import com.ww.helper.TeamHelper;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.constant.wisie.WisorType;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
@AllArgsConstructor
public class RivalInitTrainingService {
    private final ProfileService profileService;
    private final RivalGlobalService rivalGlobalService;
    private final RivalRunService rivalRunService;

    public Map<String, Object> start(RivalType type, Long profileId) {
        Map<String, Object> model = new HashMap<>();
        if (rivalGlobalService.contains(profileId)) {
            return putErrorCode(model);
        }
        Profile profile = profileService.getProfile(profileId);
        RivalTwoInit init = new RivalTwoInit(type, RivalImportance.TRAINING, profile, prepareTrainer(profile.getWisorType(), profile.getLanguage()));
        rivalRunService.run(init);
        return putSuccessCode(model);
    }

    private Profile prepareTrainer(WisorType usedWisorType, Language language) {
        Profile profile = TeamHelper.prepareComputerProfile();
        String name = language == Language.POLISH ? "Pan Trener Zdzisiek" : "Trainer John";
        profile.setName(name);
        profile.setWisorType(usedWisorType == WisorType.WISOR_17 ? WisorType.WISOR_8 : WisorType.WISOR_17);
        return profile;
    }
}
