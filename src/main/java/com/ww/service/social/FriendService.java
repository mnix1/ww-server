package com.ww.service.social;

import com.ww.model.constant.social.FriendStatus;
import com.ww.model.dto.social.FriendDTOExtended;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileFriend;
import com.ww.repository.outside.social.ProfileFriendRepository;
import com.ww.repository.outside.social.ProfileRepository;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putCode;
import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;
import static com.ww.helper.TagHelper.isCorrectTag;
import static com.ww.helper.TagHelper.prepareTag;

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
    private ProfileConnectionService profileConnectionService;

    public Map<String, Object> add(String tag) {
        return add(profileService.getProfileId(), tag);
    }

    @Transactional
    public Map<String, Object> add(Long profileId, String tag) {
        Map<String, Object> model = new HashMap<>();
        tag = prepareTag(tag);
        if (!isCorrectTag(tag)) {
            return putCode(model, -2);
        }
        Profile profile = profileService.getProfile(profileId);
        Profile friendProfile = profileService.getProfile(tag);
        if (friendProfile == null) {
            logger.error("Not existing profile tag requested: sessionProfileId: {} tag: {}", profileId, tag);
            // no profile with this tag
            return putCode(model, -2);
        }
        if (friendProfile.getId().equals(profileId)) {
            logger.error("Requested to add yourself: sessionProfileId: {} tag: {}", profileId, tag);
            // added yourself
            return putCode(model, -3);
        }
        ProfileFriend profileFriend = profileFriendRepository.findByProfile_IdAndFriendProfile_Tag(profileId, tag);
        if (profileFriend != null) {
            if (profileFriend.getStatus() == FriendStatus.ACCEPTED) {
                logger.error("Requested to add already friend: sessionProfileId: {} tag: {}", profileId, tag);
                // already friends
                return putErrorCode(model);
            }
            if (profileFriend.getStatus() == FriendStatus.REQUESTED) {
                profileFriend.setStatus(FriendStatus.ACCEPTED);
                profileFriendRepository.save(profileFriend);
                profileFriend = new ProfileFriend(FriendStatus.ACCEPTED, friendProfile, profile);
                profileFriendRepository.save(profileFriend);
                sendWebSocketFriendAdd(profileFriend);
                // acceptFriend
                return putSuccessCode(model);
            }
        }
        profileFriend = profileFriendRepository.findByProfile_IdAndFriendProfile_Id(friendProfile.getId(), profileId);
        if (profileFriend != null) {
            // request already sent
            return putErrorCode(model);
        }
        profileFriend = new ProfileFriend(FriendStatus.REQUESTED, friendProfile, profile);
        profileFriendRepository.save(profileFriend);
        sendWebSocketFriendAdd(profileFriend);
        return putSuccessCode(model);
    }

    public void sendWebSocketFriendAdd(ProfileFriend profileFriend) {
        FriendDTOExtended friendDTO = new FriendDTOExtended(profileFriend.getFriendProfile(), profileFriend.getStatus(), profileFriend.getStatus() == FriendStatus.ACCEPTED ? true : null);
        profileConnectionService.sendMessage(profileFriend.getProfile().getId(), new MessageDTO(Message.FRIEND_ADD, friendDTO.toString()).toString());
    }

    public void sendWebSocketFriendDelete(Long profileId, String tag) {
        profileConnectionService.sendMessage(profileId, new MessageDTO(Message.FRIEND_DELETE, tag).toString());
    }

    public List<FriendDTOExtended> list() {
        Set<ProfileFriend> profileFriends = profileService.getProfile().getFriends();
        return profileFriends.stream()
                .map(profileFriend -> {
                    Boolean isOnline = null;
                    if (profileFriend.getStatus() == FriendStatus.ACCEPTED) {
                        isOnline = profileConnectionService.findByProfileId(profileFriend.getFriendProfile().getId()).isPresent();
                    }
                    return new FriendDTOExtended(profileFriend, isOnline);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> delete(String tag) {
        Map<String, Object> model = new HashMap<>();
        ProfileFriend profileFriend = profileFriendRepository.findByProfile_IdAndFriendProfile_Tag(profileService.getProfileId(), tag);
        if (profileFriend != null) {
            Long friendProfileId = profileFriend.getFriendProfile().getId();
            profileFriendRepository.delete(profileFriend);
            profileFriend = profileFriendRepository.findByProfile_IdAndFriendProfile_Id(friendProfileId, profileService.getProfileId());
            if (profileFriend != null) {
                profileFriendRepository.delete(profileFriend);
                sendWebSocketFriendDelete(friendProfileId, profileFriend.getFriendProfile().getTag());
            }
        }
        return putSuccessCode(model);
    }

    public Map<String, Object> suggest() {
        Map<String, Object> model = new HashMap<>();
        List<Long> notSuggestIds = profileService.getProfile().getFriends().stream()
                .map(e -> e.getFriendProfile().getId()).collect(Collectors.toList());
        notSuggestIds.add(profileService.getProfileId());
        List<Profile> profiles = profileRepository.findAllByIdNotIn(notSuggestIds);
        Collections.shuffle(profiles);
        List<FriendDTOExtended> possibleNewFriends = profiles
                .stream()
                .limit(3)
                .map(profile -> new FriendDTOExtended(profile, FriendStatus.SUGGESTED, null))
                .collect(Collectors.toList());
        model.put("suggestedFriends", possibleNewFriends);
        return model;
    }

}
