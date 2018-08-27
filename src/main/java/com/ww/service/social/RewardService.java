package com.ww.service.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.manager.BattleManager;
import com.ww.model.container.RewardObject;
import com.ww.model.dto.book.ProfileBookDTO;
import com.ww.model.entity.book.Book;
import com.ww.model.entity.book.ProfileBook;
import com.ww.model.entity.social.Profile;
import com.ww.repository.book.ProfileBookRepository;
import com.ww.service.SessionService;
import com.ww.service.book.BookService;
import com.ww.service.book.ProfileBookService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.service.book.BookService.BOOK_SHELF_COUNT;

@Service
public class RewardService {
    private static final Logger logger = LoggerFactory.getLogger(BattleManager.class);
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileConnectionService profileConnectionService;
    @Autowired
    ProfileBookRepository profileBookRepository;
    @Autowired
    BookService bookService;
    @Autowired
    ProfileBookService profileBookService;

    public void addRewardFromBattleWin(String profileTag) {
        RewardObject rewardObject = new RewardObject();
        rewardObject.setGainGold(1L);
        Profile profile = profileService.getProfile(profileTag);
        profile.changeResources(1L, null, null, null);
        profileService.save(profile);
        if (!profileBookService.isProfileBookShelfFull(profile.getId())) {
            Book book = giveBook(profile);
            rewardObject.setBookType(book.getType());
        }
        Map<String, Object> model = new HashMap<>();
        rewardObject.writeToMap(model);
        send(model, Message.REWARD, profile.getId());
    }

    public void addRewardFromWarWin(String profileTag) {
        RewardObject rewardObject = new RewardObject();
        rewardObject.setGainGold(2L);
        Profile profile = profileService.getProfile(profileTag);
        profile.changeResources(2L, null, null, null);
        profileService.save(profile);
        if (!profileBookService.isProfileBookShelfFull(profile.getId())) {
            Book book = giveBook(profile);
            rewardObject.setBookType(book.getType());
        }
        Map<String, Object> model = new HashMap<>();
        rewardObject.writeToMap(model);
        send(model, Message.REWARD, profile.getId());
    }

    public Book giveBook(Profile profile) {
        Book book = bookService.findRandomBook();
        profileBookService.giveBook(profile, book);
        return book;
    }

    public void send(Map<String, Object> model, Message message, Long profileId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            profileConnectionService.sendMessage(profileId, new MessageDTO(message, objectMapper.writeValueAsString(model)).toString());
        } catch (JsonProcessingException e) {
            logger.error("Error when sending message");
        }
    }

}
