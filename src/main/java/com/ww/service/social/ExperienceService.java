package com.ww.service.social;

import com.ww.model.dto.social.ExperienceDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileMail;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.ww.helper.TeamHelper.isBotProfile;

@Service
@AllArgsConstructor
public class ExperienceService {
    private final ConnectionService connectionService;
    private final ProfileService profileService;
    private final MailService mailService;

    @Transactional
    public void add(Long profileId, long experienceGain) {
        if (isBotProfile(profileId)) {
            return;
        }
        Profile profile = profileService.getProfile(profileId);
        long newProfileExperience = profile.getExperience() + experienceGain;
        long levelGain = 0;
        long nextLevelExperience = nextLevelExperience(profile.getLevel());
        while (newProfileExperience >= nextLevelExperience) {
            levelGain++;
            newProfileExperience -= nextLevelExperience;
            nextLevelExperience = nextLevelExperience(profile.getLevel() + levelGain);
        }
        profile.setLevel(profile.getLevel() + levelGain);
        profile.setExperience(newProfileExperience);
        profileService.save(profile);
        connectionService.sendMessage(profileId, new MessageDTO(Message.EXPERIENCE, new ExperienceDTO(profile, experienceGain, levelGain).toString()).toString());
        sendNewLevelMail(profile, levelGain);
    }

    public void sendNewLevelMail(Profile profile, long levelGain) {
        List<ProfileMail> mails = new ArrayList<>();
        for (long i = levelGain; i > 0; i--) {
            mails.add(mailService.prepareNewLevelMail(profile, profile.getLevel() - i + 1));
        }
        mailService.save(mails);
        mailService.sendNewMailMessages(mails);
    }

    private long nextLevelExperience(Long level) {
        return (long) (5 * Math.pow(2, level - 1));
    }
}
