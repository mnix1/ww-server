package com.ww.websocket;

import com.ww.model.entity.social.Profile;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ProfileConnectionService {
    private final CopyOnWriteArrayList<ProfileConnection> profileConnections = new CopyOnWriteArrayList<ProfileConnection>();

    @Autowired
    ProfileService profileService;

    public void newConnection(WebSocketSession session) {
        Profile profile = profileService.createOrRetrieveProfile(profileService.getAuthId(session.getPrincipal()));
        profileConnections.stream()
                .filter(profileCommunication -> profileCommunication.getProfileId().equals(profile.getId()))
                .findFirst()
                .ifPresent(profileCommunication -> {
                    try {
                        profileCommunication.getWebSocketSession().close();
                    } catch (IOException e) {
                    }
                });
        profileConnections.add(new ProfileConnection(profile.getId(), session));
    }

    public void deleteConnection(WebSocketSession session) {
        profileConnections.removeIf(profileCommunication -> profileCommunication.getSessionId().equals(session.getId()));
    }

    public Optional<ProfileConnection> findBySessionId(String sessionId) {
        return profileConnections.stream().filter(profileCommunication -> profileCommunication.getSessionId().equals(sessionId)).findAny();
    }

    public Optional<ProfileConnection> findByProfileId(Long profileId) {
        return profileConnections.stream().filter(profileCommunication -> profileCommunication.getProfileId().equals(profileId)).findAny();
    }

    public boolean sendMessage(Long profileId, String msg) {
        return findByProfileId(profileId).map(profileConnection1 -> profileConnection1.sendMessage(msg)).orElse(false);
    }
}
