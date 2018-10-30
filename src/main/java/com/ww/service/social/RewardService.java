package com.ww.service.social;

import com.ww.model.container.Resources;
import com.ww.model.container.Reward;
import com.ww.model.entity.outside.book.Book;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.book.BookService;
import com.ww.service.book.ProfileBookService;
import com.ww.websocket.message.Message;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class RewardService {
    private final ProfileService profileService;
    private final ProfileConnectionService profileConnectionService;
    private final BookService bookService;
    private final ProfileBookService profileBookService;

    public void addSendRewardFromRivalWin(Profile winner, Resources resources) {
        Reward reward = new Reward();
        reward.setResources(resources);
        Profile profile = profileService.getProfile(winner.getId());
        profile.addResources(resources);
        profileService.save(profile);
        if (!profileBookService.isProfileBookShelfFull(profile.getId())) {
            Book book = giveBook(profile);
            reward.setBookType(book.getType());
        }
        Map<String, Object> model = new HashMap<>();
        reward.writeToMap(model);
        profileConnectionService.send(profile.getId(), model, Message.REWARD);
    }

//    public void addRewardFromSeason(Profile profile, Grade grade) {
//        if (grade.getResources() == null) {
//            return;
//        }
//        profile.addResources(grade.getResources());
//    }

    public Book giveBook(Profile profile) {
        Book book = bookService.findRandomBook();
        profileBookService.giveBook(profile, book);
        return book;
    }

}
