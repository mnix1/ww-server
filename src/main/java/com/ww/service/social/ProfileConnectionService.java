package com.ww.service.social;

import com.ww.model.constant.social.FriendStatus;
import com.ww.model.entity.social.Profile;
import com.ww.repository.social.ProfileFriendRepository;
import com.ww.service.SessionService;
import com.ww.service.social.ProfileService;
import com.ww.model.container.ProfileConnection;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ProfileConnectionService {
    private final CopyOnWriteArrayList<ProfileConnection> profileConnections = new CopyOnWriteArrayList<ProfileConnection>();

    @Autowired
    ProfileService profileService;

    @Autowired
    SessionService sessionService;

    @Autowired
    ProfileFriendRepository profileFriendRepository;

    public List<ProfileConnection> getProfileConnections() {
        return profileConnections;
    }

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
        sendFriendConnectionChanged(profile, Message.FRIEND_ONLINE);
    }

    public void deleteConnection(WebSocketSession session) {
        findBySessionId(session.getId()).ifPresent(profileConnection -> {
            profileConnections.remove(profileConnection);
            sendFriendConnectionChanged(profileService.getProfile(profileConnection.getProfileId()), Message.FRIEND_OFFLINE);
        });
    }

    public Optional<ProfileConnection> findBySessionId(String sessionId) {
        return profileConnections.stream().filter(profileCommunication -> profileCommunication.getSessionId().equals(sessionId)).findAny();
    }

    public Optional<ProfileConnection> findByProfileId(Long profileId) {
        return profileConnections.stream().filter(profileCommunication -> profileCommunication.getProfileId().equals(profileId)).findAny();
    }

    public ProfileConnection findByProfileId() {
        return findByProfileId(sessionService.getProfileId()).get();
    }

    public boolean sendMessage(Long profileId, String msg) {
        return findByProfileId(profileId).map(profileConnection1 -> profileConnection1.sendMessage(msg)).orElse(false);
    }

    public void sendFriendConnectionChanged(Profile profile, Message message) {
        profileFriendRepository.findByProfile_IdAndStatus(profile.getId(), FriendStatus.ACCEPTED).stream()
                .forEach(profileFriend ->
                        sendMessage(profileFriend.getFriendProfile().getId(), new MessageDTO(message, profile.getTag()).toString())
                );
    }
}
