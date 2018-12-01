package com.ww.controller.rival;

import com.ww.model.constant.rival.campaign.CampaignDestination;
import com.ww.model.constant.rival.campaign.CampaignType;
import com.ww.model.container.rival.init.RivalCampaignWarInit;
import com.ww.model.dto.rival.campaign.CampaignDTO;
import com.ww.model.dto.rival.campaign.ProfileCampaignDTO;
import com.ww.service.rival.campaign.CampaignService;
import com.ww.service.rival.campaign.RivalCampaignWarService;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.init.RivalRunService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@RestController
@RequestMapping(value = "/campaign")
@AllArgsConstructor
public class CampaignController {
    private final RivalCampaignWarService rivalCampaignWarService;
    private final CampaignService campaignService;
    private final RivalRunService rivalRunService;
    private final RivalGlobalService rivalGlobalService;
    private final ProfileService profileService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public Map start() {
        Map<String, Object> model = new HashMap<>();
        if (rivalGlobalService.contains(profileService.getProfileId())) {
            return putErrorCode(model);
        }
        Optional<RivalCampaignWarInit> init = rivalCampaignWarService.init();
        init.ifPresent(rivalRunService::run);
        if (!init.isPresent()) {
            return putErrorCode(model);
        }
        return putSuccessCode(model);
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public Map init(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("type") || !payload.containsKey("destination") || !payload.containsKey("ids")) {
            throw new IllegalArgumentException();
        }
        CampaignType type = CampaignType.valueOf((String) payload.get("type"));
        CampaignDestination destination = CampaignDestination.valueOf((String) payload.get("destination"));
        List<Long> ids = ((List<Integer>) payload.get("ids")).stream().map(Integer::longValue).collect(Collectors.toList());
        return campaignService.init(type, destination, ids);
    }

    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public Map close() {
        return campaignService.close();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<CampaignDTO> list() {
        return campaignService.list();
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ProfileCampaignDTO active() {
        return campaignService.activeDTO().orElse(null);
    }
}
