package com.ww.service.social;

import com.ww.model.constant.social.MailType;
import com.ww.model.container.rival.challenge.ChallengePosition;
import com.ww.model.dto.social.ProfileMailDTO;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.rival.season.SeasonGrade;
import com.ww.model.entity.outside.social.ProfileMail;
import com.ww.repository.outside.social.ProfileMailRepository;
import com.ww.websocket.message.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ww.websocket.message.Message.NEW_MAIL;

@Service
public class MailService {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileConnectionService profileConnectionService;
    @Autowired
    private ProfileMailRepository profileMailRepository;

    public List<ProfileMailDTO> list() {
        return profileMailRepository.findAllByProfile_Id(profileService.getProfileId()).stream().map(ProfileMailDTO::new).collect(Collectors.toList());
    }

    public void save(List<ProfileMail> mails) {
        profileMailRepository.saveAll(mails);
    }

    public void sendNewMailMessage(List<ProfileMail> mails) {
        for (ProfileMail mail : mails) {
            profileConnectionService.sendMessage(mail.getProfile().getId(), new MessageDTO(NEW_MAIL, "").toString());
        }
    }

    public ProfileMail prepareChallengeResultsMail(ChallengePosition challengePosition) {
        ChallengeProfile challengeProfile = challengePosition.getChallengeProfile();
        if (challengeProfile.getGainResources().getEmpty()) {
            return new ProfileMail(challengeProfile.getProfile(), MailType.CHALLENGE_ENDED, challengePosition.toJson());
        }
        return new ProfileMail(challengeProfile.getProfile(), MailType.CHALLENGE_ENDED, challengeProfile.getGainResources(), challengePosition.toJson());
    }

    public ProfileMail prepareSeasonResultsMail(ProfileSeason profileSeason, SeasonGrade seasonGrade) {
        if (seasonGrade.getGainResources().getEmpty()) {
            return new ProfileMail(profileSeason.getProfile(), MailType.SEASON_ENDED, seasonGrade.toJson());
        }
        return new ProfileMail(profileSeason.getProfile(), MailType.SEASON_ENDED, seasonGrade.getGainResources(), seasonGrade.toJson());
    }

}
