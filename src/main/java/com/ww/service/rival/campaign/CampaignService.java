package com.ww.service.rival.campaign;

import com.ww.model.constant.rival.campaign.CampaignDestination;
import com.ww.model.constant.rival.campaign.CampaignType;
import com.ww.model.dto.rival.campaign.CampaignDTO;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.repository.rival.campaign.CampaignRepository;
import com.ww.service.wisie.ProfileWisieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ProfileWisieService profileWisieService;

    public Map<String, Object> list() {
        Map<String, Object> model = new HashMap<>();
        model.put("campaigns", campaignRepository.findAll().stream().map(CampaignDTO::new).collect(Collectors.toList()));
        return model;
    }

    public Map<String, Object> init(CampaignType type, CampaignDestination destination, Set<Long> ids) {
        Map<String, Object> model = new HashMap<>();
        List<ProfileWisie> wisies = profileWisieService.findByIds(ids);
        if (wisies == null) {
            return putErrorCode(model);
        }
        return putSuccessCode(model);
    }

}
