package com.ww.service.social;

import com.ww.model.constant.social.FriendStatus;
import com.ww.model.dto.social.FriendDTO;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.social.ProfileFriend;
import com.ww.repository.social.ProfileFriendRepository;
import com.ww.repository.social.ProfileRepository;
import com.ww.service.SessionService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FriendService {
    private static final Logger logger = LoggerFactory.getLogger(FriendService.class);

    @Autowired
    private ProfileFriendRepository profileFriendRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ProfileConnectionService profileConnectionService;

    public Map<String, Object> add(String tag) {
        return add(sessionService.getProfileId(), tag);
    }

    public Map<String, Object> add(Long profileId, String tag) {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile(profileId);
        Profile friendProfile = profileService.getProfile(tag);
        if (friendProfile == null) {
            model.put("code", -2); // no profile with this tag
            logger.error("Not existing profile tag requested: sessionProfileId: {} tag: {}", profileId, tag);
            return model;
        }
        if (friendProfile.getId().equals(profileId)) {
            model.put("code", -3); // added yourself
            logger.error("Requested to add yourself: sessionProfileId: {} tag: {}", profileId, tag);
            return model;
        }
        ProfileFriend profileFriend = profileFriendRepository.findByProfile_IdAndFriendProfile_Tag(profileId, tag);
        if (profileFriend != null) {
            if (profileFriend.getStatus() == FriendStatus.ACCEPTED) {
                model.put("code", -1); // already friends
                logger.error("Requested to add already friend: sessionProfileId: {} tag: {}", profileId, tag);
                return model;
            }
            if (profileFriend.getStatus() == FriendStatus.REQUESTED) {
                profileFriend.setStatus(FriendStatus.ACCEPTED);
                profileFriendRepository.save(profileFriend);
                profileFriend = new ProfileFriend(FriendStatus.ACCEPTED, friendProfile, profile);
                profileFriendRepository.save(profileFriend);
                sendWebSocketFriendAdd(profileFriend);
                model.put("code", 1); // accept
                return model;
            }
        }
        profileFriend = profileFriendRepository.findByProfile_IdAndFriendProfile_Id(friendProfile.getId(), profileId);
        if (profileFriend != null) {
            model.put("code", -1); // request already sent
            return model;
        }
        profileFriend = new ProfileFriend(FriendStatus.REQUESTED, friendProfile, profile);
        profileFriendRepository.save(profileFriend);
        sendWebSocketFriendAdd(profileFriend);
        model.put("code", 1);
        return model;
    }

    public void sendWebSocketFriendAdd(ProfileFriend profileFriend) {
        FriendDTO friendDTO = new FriendDTO(profileFriend.getFriendProfile(), profileFriend.getStatus(), profileFriend.getStatus() == FriendStatus.ACCEPTED ? true : null);
        profileConnectionService.sendMessage(profileFriend.getProfile().getId(), new MessageDTO(Message.FRIEND_ADD, friendDTO.toString()).toString());
    }

    public void sendWebSocketFriendDelete(Long profileId, String tag) {
        profileConnectionService.sendMessage(profileId, new MessageDTO(Message.FRIEND_DELETE, tag).toString());
    }

    public List<FriendDTO> list() {
        Set<ProfileFriend> profileFriends = profileService.getProfile().getFriends();
        return profileFriends.stream()
                .map(profileFriend -> {
                    Boolean isOnline = null;
                    if (profileFriend.getStatus() == FriendStatus.ACCEPTED) {
                        isOnline = profileConnectionService.findByProfileId(profileFriend.getFriendProfile().getId()).isPresent();
                    }
                    return new FriendDTO(profileFriend, isOnline);
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> delete(String tag) {
        Map<String, Object> model = new HashMap<>();
        ProfileFriend profileFriend = profileFriendRepository.findByProfile_IdAndFriendProfile_Tag(sessionService.getProfileId(), tag);
        if (profileFriend != null) {
            Long friendProfileId = profileFriend.getFriendProfile().getId();
            profileFriendRepository.delete(profileFriend);
            profileFriend = profileFriendRepository.findByProfile_IdAndFriendProfile_Id(friendProfileId, sessionService.getProfileId());
            if (profileFriend != null) {
                profileFriendRepository.delete(profileFriend);
                sendWebSocketFriendDelete(friendProfileId, profileFriend.getFriendProfile().getTag());
            }
        }
        model.put("code", 1);
        return model;
    }


    public Map<String, Object> suggest() {
        Map<String, Object> model = new HashMap<>();
        List<Long> notSuggestIds = profileService.getProfile().getFriends().stream()
                .map(e -> e.getFriendProfile().getId()).collect(Collectors.toList());
        notSuggestIds.add(sessionService.getProfileId());
        List<FriendDTO> possibleNewFriends = profileRepository.findAllByIdNotIn(notSuggestIds)
                .stream()
                .limit(5)
                .map(profile -> new FriendDTO(profile, FriendStatus.SUGGESTED, null))
                .collect(Collectors.toList());
        model.put("suggestedFriends", possibleNewFriends);
        return model;
    }

}
