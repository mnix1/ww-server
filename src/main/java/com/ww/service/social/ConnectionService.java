package com.ww.service.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.game.auto.AutoManager;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.container.AutoProfileConnection;
import com.ww.model.container.Connection;
import com.ww.model.container.ProfileConnection;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.social.ProfileFriendRepository;
import com.ww.service.rival.global.RivalMessageService;
import com.ww.service.rival.init.RivalInitRandomOpponentService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class ConnectionService {
    private static Logger logger = LoggerFactory.getLogger(ConnectionService.class);
    private static final Map<Long, Connection> profileIdToConnectionMap = new ConcurrentHashMap<>();
    private static final Map<String, Connection> sessionIdToConnectionMap = new ConcurrentHashMap<>();

    private final ProfileService profileService;
    private final ProfileFriendRepository profileFriendRepository;
    private final RivalInitRandomOpponentService rivalInitRandomOpponentService;

    public synchronized ProfileConnection newProfileConnection(WebSocketSession session) {
        Profile profile = profileService.retrieveProfile(profileService.getAuthId(session.getPrincipal()));
        deleteConnection(profile.getId());
        ProfileConnection connection = new ProfileConnection(profile.getId(), profile.getTag(), session);
        profileIdToConnectionMap.put(profile.getId(), connection);
        sessionIdToConnectionMap.put(session.getId(), connection);
        logger.debug("profile connection newConnection: profileId: " + profile.getId() + ", sessionId: " + session.getId());
        return connection;
    }

    public synchronized AutoProfileConnection newAutoProfileConnection(RivalMessageService messageService, AutoManager manager) {
        Profile profile = manager.getProfile();
        deleteConnection(profile.getId());
        AutoProfileConnection connection = new AutoProfileConnection(messageService, manager);
        profileIdToConnectionMap.put(profile.getId(), connection);
        sessionIdToConnectionMap.put(connection.getSessionId(), connection);
        logger.debug("auto profile connection newConnection: profileId: " + profile.getId() + ", sessionId: " + connection.getSessionId());
        return connection;
    }

    private synchronized void deleteConnection(Long profileId) {
        findByProfileId(profileId).ifPresent(this::deleteConnection);
    }

    public synchronized void deleteConnection(Connection connection) {
        rivalInitRandomOpponentService.remove(connection.getProfileId());
        sessionIdToConnectionMap.remove(connection.getSessionId());
        profileIdToConnectionMap.remove(connection.getProfileId());
        logger.debug("connection deleteConnection: profileId: " + connection.getProfileId() + ", sessionId: " + connection.getSessionId());
        connection.close();
    }

    public Optional<Connection> findBySessionId(String sessionId) {
        if (sessionIdToConnectionMap.containsKey(sessionId)) {
            return Optional.ofNullable(sessionIdToConnectionMap.get(sessionId));
        }
        return Optional.empty();
    }

    public Optional<Connection> findByProfileId(Long profileId) {
        if (profileIdToConnectionMap.containsKey(profileId)) {
            return Optional.ofNullable(profileIdToConnectionMap.get(profileId));
        }
        return Optional.empty();
    }

    public Optional<Long> getProfileId(String sessionId) {
        if (sessionIdToConnectionMap.containsKey(sessionId)) {
            Connection connection = sessionIdToConnectionMap.get(sessionId);
            return Optional.ofNullable(connection.getProfileId());
        }
        return Optional.empty();
    }

    public void sendMessage(Long profileId, String msg) {
        findByProfileId(profileId).map(connection -> {
            connection.sendMessage(msg);
            return connection;
        });
    }

    public void send(Long profileId, Map<String, Object> model, Message message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            sendMessage(profileId, new MessageDTO(message, objectMapper.writeValueAsString(model)).toString());
        } catch (JsonProcessingException e) {
            logger.error("Error when sending message: " + e.toString());
        }
    }

    public void sendFriendConnectionChanged(String profileTag, Message message) {
        profileFriendRepository.findByProfile_TagAndStatus(profileTag, FriendStatus.ACCEPTED)
                .forEach(profileFriend ->
                        sendMessage(profileFriend.getFriendProfile().getId(), new MessageDTO(message, profileTag).toString())
                );
    }
}
