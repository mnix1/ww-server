import com.ww.model.constant.social.ResourceType;
import com.ww.model.container.Resources;
import com.ww.model.container.rival.challenge.ChallengePosition;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;

import java.util.ArrayList;
import java.util.List;

public class ChallengeRewardTest {
    private void test(int players, long cost) {
        List<ChallengeProfile> challengeProfiles = new ArrayList<>();
        List<ChallengePosition> challengePositions = new ArrayList<>();
        for (int i = 0; i < players; i++) {
            ChallengeProfile challengeProfile = new ChallengeProfile();
            ChallengePosition challengePosition = new ChallengePosition(challengeProfile);
            challengeProfiles.add(challengeProfile);
            challengePositions.add(challengePosition);
        }
        Resources prizePool = new Resources(20L, 20L, 20 + players * cost, 20L);
        Resources joinCost = new Resources(ResourceType.WISDOM, cost);
        List<ChallengeProfile> rewardedChallengeProfiles = new ArrayList<>();
        int profilesWithReward = Math.max(1, challengePositions.size() / 10);
        for (int i = profilesWithReward - 1; i >= 0; i--) {
            ChallengeProfile challengeProfile = challengePositions.get(i).getChallengeProfile();
            Resources resources = new Resources(0L).add(joinCost);
            if (i == 2) {
                for (int k = 1; k < challengePositions.size() / 6; k++) {
                    resources.add(joinCost);
                }
            } else if (i == 1) {
                for (int k = 1; k < challengePositions.size() / 3; k++) {
                    resources.add(joinCost);
                }
            } else if (i == 0) {
                resources = prizePool;
            }
            rewardedChallengeProfiles.add(challengeProfile);
            challengeProfile.setGainResources(resources);
            prizePool.subtract(resources);
        }
        int a = 0;
    }
}
