package com.ww.service.wisie;

import com.ww.model.constant.wisie.WisieType;
import com.ww.model.dto.social.ProfileResourcesDTO;
import com.ww.model.dto.wisie.WisieDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.model.entity.outside.wisie.Wisie;
import com.ww.repository.outside.wisie.WisieRepository;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putCode;
import static com.ww.helper.ModelHelper.putSuccessCode;
import static com.ww.helper.RandomHelper.randomElement;

@Service
public class WisieService {

    @Autowired
    private WisieRepository wisieRepository;

    public List<WisieDTO> listDTOs() {
        return list().stream()
                .map(WisieDTO::new)
                .collect(Collectors.toList());
    }

    public List<Wisie> list() {
        return wisieRepository.findAll();
    }

    public Wisie getWisie(WisieType type) {
        return wisieRepository.findByType(type);
    }

    public List<Wisie> getWisies(List<WisieType> types) {
        return wisieRepository.findByTypeIn(types);
    }

}
