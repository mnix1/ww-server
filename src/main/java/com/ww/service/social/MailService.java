package com.ww.service.social;

import com.ww.model.constant.social.MailType;
import com.ww.model.container.rival.challenge.ChallengePosition;
import com.ww.model.dto.social.ProfileMailDTO;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.rival.season.SeasonGrade;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileMail;
import com.ww.repository.outside.social.ProfileMailRepository;
import com.ww.websocket.message.MessageDTO;
import lombok.AllArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;
import static com.ww.websocket.message.Message.NEW_MAIL;

@Service
@AllArgsConstructor
public class MailService {
    private final ProfileService profileService;
    private final ConnectionService connectionService;
    private final ProfileMailRepository profileMailRepository;

    public List<ProfileMailDTO> list() {
        return list(profileService.getProfileId()).stream().map(ProfileMailDTO::new).collect(Collectors.toList());
    }

    public List<ProfileMail> list(Long profileId) {
        return profileMailRepository.findAllByProfile_Id(profileId);
    }

    public void save(List<ProfileMail> mails) {
        profileMailRepository.saveAll(mails);
    }

    public void sendNewMailMessage(List<ProfileMail> mails) {
        for (ProfileMail mail : mails) {
            connectionService.sendMessage(mail.getProfile().getId(), new MessageDTO(NEW_MAIL, "").toString());
        }
    }

    @Transactional
    public Map<String, Object> delete(Long deleteId, Long profileId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileMail> optionalProfileMail = profileMailRepository.findById(deleteId);
        if (!optionalProfileMail.isPresent()) {
            return putErrorCode(model);
        }
        ProfileMail profileMail = optionalProfileMail.get();
        if (!profileMail.getProfile().getId().equals(profileId)) {
            return putErrorCode(model);
        }
        profileMailRepository.delete(profileMail);
        return putSuccessCode(model);
    }

    @Transactional
    public Map<String, Object> displayed() {
        Map<String, Object> model = new HashMap<>();
        List<ProfileMail> profileMails = profileMailRepository.findAllByProfile_Id(profileService.getProfileId());
        List<ProfileMail> toSaveProfileMails = new ArrayList<>();
        for (ProfileMail profileMail : profileMails) {
            if (!profileMail.getDisplayed()) {
                profileMail.setDisplayed(true);
                toSaveProfileMails.add(profileMail);
            }
        }
        profileMailRepository.saveAll(toSaveProfileMails);
        return putSuccessCode(model);
    }

    @Transactional
    public Map<String, Object> claimReward(Long claimRewardId, Long profileId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileMail> optionalProfileMail = profileMailRepository.findById(claimRewardId);
        if (!optionalProfileMail.isPresent()) {
            return putErrorCode(model);
        }
        ProfileMail profileMail = optionalProfileMail.get();
        if (profileMail.getClaimed() || !profileMail.getHasResources() || !profileMail.getProfile().getId().equals(profileId)) {
            return putErrorCode(model);
        }
        Profile profile = profileService.getProfile(profileId);
        profile.addResources(profileMail.getGainResources());
        profileService.save(profile);
        profileMail.setClaimed(true);
        profileMailRepository.save(profileMail);
        return putSuccessCode(model);
    }

    public ProfileMail prepareChallengeResultsMail(ChallengePosition challengePosition) {
        ChallengeProfile challengeProfile = challengePosition.getChallengeProfile();
        if (challengeProfile.getGainResources().getEmpty()) {
            return new ProfileMail(challengeProfile.getProfile(), MailType.CHALLENGE_ENDED, challengePosition.toJson());
        }
        return new ProfileMail(challengeProfile.getProfile(), MailType.CHALLENGE_ENDED, challengeProfile.getGainResources(), challengePosition.toJson());
    }

    public ProfileMail prepareSeasonResultsMail(ProfileSeason profileSeason, SeasonGrade seasonGrade) {
        Map<String, Object> model = seasonGrade.toMap();
        model.putAll(profileSeason.toMap());
        String content = "";
        try {
            content = new ObjectMapper().writeValueAsString(model);
        } catch (IOException e) {
        }
        if (seasonGrade.getGainResources().getEmpty()) {
            return new ProfileMail(profileSeason.getProfile(), MailType.SEASON_ENDED, content);
        }
        return new ProfileMail(profileSeason.getProfile(), MailType.SEASON_ENDED, seasonGrade.getGainResources(), content);
    }

}
