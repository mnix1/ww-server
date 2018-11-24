package com.ww.service.auto.command;

import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileMail;
import com.ww.service.social.MailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AutoManageMailService {

    private final MailService mailService;

    public void manage(Profile profile) {
        for (ProfileMail mail : mailService.list(profile.getId())) {
            if (mail.getHasResources()) {
                mailService.claimReward(mail.getId(), profile.getId());
            }
            mailService.delete(mail.getId(), profile.getId());
        }
    }

}
