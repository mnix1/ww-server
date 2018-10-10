package com.ww.service.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.container.ProfileConnection;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.social.ProfileFriendRepository;
import com.ww.service.rival.init.RivalInitRandomOpponentService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProfileConnectionService {
    private static Logger logger = LoggerFactory.getLogger(ProfileConnectionService.class);
    private final Map<Long, ProfileConnection> profileIdToProfileConnectionMap = new ConcurrentHashMap<>();
    private final Map<String, ProfileConnection> sessionIdToProfileConnectionMap = new ConcurrentHashMap<>();

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileFriendRepository profileFriendRepository;

    @Autowired
    private RivalInitRandomOpponentService rivalInitRandomOpponentService;

    public synchronized ProfileConnection newConnection(WebSocketSession session) {
        Profile profile = profileService.retrieveProfile(profileService.getAuthId(session.getPrincipal()));
        deleteConnection(profile.getId());
        ProfileConnection profileConnection = new ProfileConnection(profile.getId(), profile.getTag(), session);
        profileIdToProfileConnectionMap.put(profile.getId(), profileConnection);
        sessionIdToProfileConnectionMap.put(session.getId(), profileConnection);
        logger.debug("ProfileConnection newConnection: profileId: " + profile.getId() + ", sessionId: " + session.getId());
        return profileConnection;
    }

    private synchronized void deleteConnection(Long profileId) {
        findByProfileId(profileId).ifPresent(this::deleteConnection);
    }

    public synchronized void deleteConnection(ProfileConnection profileConnection) {
        rivalInitRandomOpponentService.remove(profileConnection.getProfileId());
        sessionIdToProfileConnectionMap.remove(profileConnection.getSessionId());
        profileIdToProfileConnectionMap.remove(profileConnection.getProfileId());
        logger.debug("ProfileConnection deleteConnection: profileId: " + profileConnection.getProfileId() + ", sessionId: " + profileConnection.getSessionId());
        profileConnection.close();
    }

    public Optional<ProfileConnection> findBySessionId(String sessionId) {
        if (sessionIdToProfileConnectionMap.containsKey(sessionId)) {
            return Optional.ofNullable(sessionIdToProfileConnectionMap.get(sessionId));
        }
        return Optional.empty();
    }

    public Optional<ProfileConnection> findByProfileId(Long profileId) {
        if (profileIdToProfileConnectionMap.containsKey(profileId)) {
            return Optional.ofNullable(profileIdToProfileConnectionMap.get(profileId));
        }
        return Optional.empty();
    }

    public Optional<Long> getProfileId(String sessionId) {
        if (sessionIdToProfileConnectionMap.containsKey(sessionId)) {
            ProfileConnection profileConnection = sessionIdToProfileConnectionMap.get(sessionId);
            return Optional.ofNullable(profileConnection.getProfileId());
        }
        return Optional.empty();
    }

    public boolean sendMessage(Long profileId, String msg) {
        return findByProfileId(profileId).map(profileConnection -> profileConnection.sendMessage(msg)).orElse(false);
    }

    public void send(Map<String, Object> model, Message message, Long profileId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            sendMessage(profileId, new MessageDTO(message, objectMapper.writeValueAsString(model)).toString());
        } catch (JsonProcessingException e) {
            logger.error("Error when sending message");
        }
    }

    public void sendFriendConnectionChanged(String profileTag, Message message) {
        profileFriendRepository.findByProfile_TagAndStatus(profileTag, FriendStatus.ACCEPTED)
                .forEach(profileFriend ->
                        sendMessage(profileFriend.getFriendProfile().getId(), new MessageDTO(message, profileTag).toString())
                );
    }
}
