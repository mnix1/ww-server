package com.ww.service.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileStatus;
import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.dto.rival.challenge.ChallengeInfoDTO;
import com.ww.model.dto.rival.challenge.ChallengePositionDTO;
import com.ww.model.dto.rival.challenge.ChallengeSummaryDTO;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileFriend;
import com.ww.repository.outside.rival.challenge.ChallengeProfileRepository;
import com.ww.repository.outside.rival.challenge.ChallengeRepository;
import com.ww.service.social.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
public class ChallengeService {
    private static final Logger logger = LoggerFactory.getLogger(ChallengeService.class);

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeProfileRepository challengeProfileRepository;

    @Autowired
    private RivalChallengeService rivalChallengeService;

    @Autowired
    private ProfileService profileService;

    public Map<String, Object> friendInit(List<String> tags) {
        Map<String, Object> model = new HashMap<>();
        if (tags.isEmpty()) {
            logger.error("Empty tags: {}", profileService.getProfileId());
            return putErrorCode(model);
        }
        Set<String> tagSet = new HashSet<>(tags);
        Profile profile = profileService.getProfile();
        List<Profile> friends = profile.getFriends().stream()
                .filter(profileFriend -> profileFriend.getStatus() == FriendStatus.ACCEPTED)
                .map(ProfileFriend::getFriendProfile)
                .filter(e -> tagSet.contains(e.getTag()))
                .collect(Collectors.toList());
        if (friends.isEmpty()) {
            logger.error("Empty friends: {}", profileService.getProfileId());
            return putErrorCode(model); //error no one to fight
        }
        List<Profile> profiles = new ArrayList<>();
        profiles.add(profile);
        profiles.addAll(friends);
        create(profile, profiles);
        return putSuccessCode(model);
    }

    public Map<String, Object> response(Long challengeId) {
        Map<String, Object> model = new HashMap<>();
        ChallengeProfile challengeProfile = challengeProfileRepository.findByProfile_IdAndChallenge_Id(profileService.getProfileId(), challengeId);
        if (challengeProfile == null || challengeProfile.getStatus() != ChallengeProfileStatus.OPEN) {
            return putErrorCode(model);
        }
        challengeProfile.setStatus(ChallengeProfileStatus.IN_PROGRESS);
        challengeProfileRepository.save(challengeProfile);
        rivalChallengeService.init(challengeProfile);
        return putSuccessCode(model);
    }

    private void create(Profile creator, List<Profile> profiles) {
        Challenge challenge = new Challenge();
        challenge.setCreatorProfile(creator);
        challenge.setProfiles(profiles.stream()
                .map(profile -> new ChallengeProfile(challenge, profile, ChallengeProfileStatus.OPEN))
                .collect(Collectors.toSet()));
        challengeRepository.save(challenge);
        challengeProfileRepository.saveAll(challenge.getProfiles());
    }

    public List<ChallengeInfoDTO> list(ChallengeStatus status) {
        if (status == ChallengeStatus.CLOSED) {
            List<ChallengeProfile> challengeProfiles = challengeProfileRepository.findAllByProfile_IdAndStatusAndChallenge_Status(profileService.getProfileId(), ChallengeProfileStatus.CLOSED, ChallengeStatus.CLOSED);
            return challengeProfiles.stream()
                    .map(challengeProfile -> new ChallengeInfoDTO(challengeProfile.getChallenge()))
                    .distinct()
                    .collect(Collectors.toList());
        }
        List<ChallengeProfile> challengeProfiles = challengeProfileRepository.findAllByProfile_IdAndChallenge_Status(profileService.getProfileId(), ChallengeStatus.IN_PROGRESS);
        return challengeProfiles.stream()
                .map(challengeProfile -> new ChallengeInfoDTO(challengeProfile.getChallenge(), challengeProfile.getStatus() != ChallengeProfileStatus.CLOSED))
                .distinct()
                .collect(Collectors.toList());
    }

    public ChallengeSummaryDTO summary(Long challengeId) {
        return summary(challengeProfileRepository.findByProfile_IdAndChallenge_Id(profileService.getProfileId(), challengeId));
    }

    public ChallengeSummaryDTO summary(ChallengeProfile challengeProfile) {
        if (challengeProfile == null) {
            return null;
        }
        Challenge challenge = challengeProfile.getChallenge();
        List<ChallengePositionDTO> positions = new ArrayList<>();
        Set<ChallengeProfile> challengeProfiles = challenge.getProfiles();
        for (ChallengeProfile cp : challengeProfiles) {
            ChallengePositionDTO position = new ChallengePositionDTO(cp);
            positions.add(position);
        }
        positions.sort((o1, o2) -> {
            if (o1.getStatus() != ChallengeProfileStatus.CLOSED) {
                if (o2.getStatus() != ChallengeProfileStatus.CLOSED) {
                    return 0;
                }
                return 1;
            }
            if (o2.getStatus() != ChallengeProfileStatus.CLOSED) {
                return -1;
            }
            if (o1.getScore().equals(o2.getScore())) {
                return 0;
            }
            return o2.getScore().compareTo(o1.getScore());
        });
        return new ChallengeSummaryDTO(challenge.getId(), positions);
    }

}
