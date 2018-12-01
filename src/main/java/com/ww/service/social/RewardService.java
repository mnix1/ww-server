package com.ww.service.social;

import com.ww.helper.JSONHelper;
import com.ww.model.container.Resources;
import com.ww.model.container.Reward;
import com.ww.model.entity.outside.book.Book;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.book.BookService;
import com.ww.service.book.ProfileBookService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class RewardService {
    private final ProfileService profileService;
    private final ConnectionService connectionService;
    private final BookService bookService;
    private final ProfileBookService profileBookService;

    public void addSendRewardFromRivalWin(Profile winner, ProfileSeason winnerSeason, Resources resources) {
        Reward reward = new Reward();
        reward.setResources(resources);
        Profile profile = profileService.getProfile(winner.getId());
        profile.addResources(resources);
        profileService.save(profile);
        if (winnerSeason != null && winnerSeason.getPreviousGrade() != winnerSeason.getGrade()) {
            Book book = giveBook(profile);
            reward.setBookType(book.getType());
        }
        Map<String, Object> model = new HashMap<>();
        reward.writeToMap(model);
        model.put("resources", profile.getResources());
        connectionService.sendMessage(profile.getId(), new MessageDTO(Message.REWARD, JSONHelper.toJSON(model)).toString());
    }

    public Book giveBook(Profile profile) {
        Book book = bookService.findBook(4);
        profileBookService.giveBook(profile, book);
        return book;
    }

}
