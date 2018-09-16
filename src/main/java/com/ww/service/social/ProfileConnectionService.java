package com.ww.service.social;

import com.ww.model.constant.social.FriendStatus;
import com.ww.model.container.ProfileConnection;
import com.ww.model.entity.social.Profile;
import com.ww.repository.social.ProfileFriendRepository;
import com.ww.service.SessionService;
import com.ww.service.rival.GlobalRivalService;
import com.ww.service.rival.RivalRandomOpponentService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProfileConnectionService {
    private static Logger logger = LoggerFactory.getLogger(ProfileConnectionService.class);
    private final ConcurrentHashMap<Long, ProfileConnection> profileIdToProfileConnectionMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ProfileConnection> sessionIdToProfileConnectionMap = new ConcurrentHashMap<>();

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileFriendRepository profileFriendRepository;

    @Autowired
    private RivalRandomOpponentService rivalRandomOpponentService;

    public ProfileConnection newConnection(WebSocketSession session) {
        Profile profile = profileService.retrieveProfile(profileService.getAuthId(session.getPrincipal()));
        if (profileIdToProfileConnectionMap.containsKey(profile.getId())) {
            ProfileConnection profileConnection = profileIdToProfileConnectionMap.get(profile.getId());
            String oldSessionId = profileConnection.getSessionId();
            if (sessionIdToProfileConnectionMap.containsKey(oldSessionId)) {
                sessionIdToProfileConnectionMap.remove(oldSessionId);
            }
            profileConnection.close();
            profileIdToProfileConnectionMap.remove(profile.getId());
        }
        ProfileConnection profileConnection = new ProfileConnection(profile.getId(), session);
        profileIdToProfileConnectionMap.put(profile.getId(), profileConnection);
        sessionIdToProfileConnectionMap.put(session.getId(), profileConnection);
        logger.debug("ProfileConnection newConnection: sessionId: " + session.getId() + ", profileId: " + profile.getId());
        sendFriendConnectionChanged(profile, Message.FRIEND_SIGN_IN);
        return profileConnection;
    }

    public void deleteConnection(WebSocketSession session) {
        findBySessionId(session.getId()).ifPresent(profileConnection -> {
            rivalRandomOpponentService.remove(profileConnection.getProfileId());
            sessionIdToProfileConnectionMap.remove(session.getId());
            profileIdToProfileConnectionMap.remove(profileConnection.getProfileId());
            logger.debug("ProfileConnection deleteConnection: sessionId: " + session.getId() + ", profileId: " + profileConnection.getProfileId());
            sendFriendConnectionChanged(profileService.getProfile(profileConnection.getProfileId()), Message.FRIEND_SIGN_OUT);
        });
    }

    public Optional<ProfileConnection> findBySessionId(String sessionId) {
        ProfileConnection profileConnection = null;
        if (sessionIdToProfileConnectionMap.containsKey(sessionId)) {
            profileConnection = sessionIdToProfileConnectionMap.get(sessionId);
        }
        return Optional.ofNullable(profileConnection);
    }

    public Optional<ProfileConnection> findByProfileId(Long profileId) {
        ProfileConnection profileConnection = null;
        if (profileIdToProfileConnectionMap.containsKey(profileId)) {
            profileConnection = profileIdToProfileConnectionMap.get(profileId);
        }
        return Optional.ofNullable(profileConnection);
    }

    public Optional<Long> getProfileId(String sessionId) {
        Long profileId = null;
        if (sessionIdToProfileConnectionMap.containsKey(sessionId)) {
            ProfileConnection profileConnection = sessionIdToProfileConnectionMap.get(sessionId);
            profileId = profileConnection.getProfileId();
        }
        return Optional.ofNullable(profileId);
    }

    public boolean sendMessage(Long profileId, String msg) {
        return findByProfileId(profileId).map(profileConnection -> profileConnection.sendMessage(msg)).orElse(false);
    }

    public void sendFriendConnectionChanged(Profile profile, Message message) {
        profileFriendRepository.findByProfile_IdAndStatus(profile.getId(), FriendStatus.ACCEPTED).stream()
                .forEach(profileFriend ->
                        sendMessage(profileFriend.getFriendProfile().getId(), new MessageDTO(message, profile.getTag()).toString())
                );
    }
}
