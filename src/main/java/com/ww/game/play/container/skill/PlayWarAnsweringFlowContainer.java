package com.ww.game.play.container.skill;

import com.ww.game.GameFlow;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.flow.MemberWisieFlow;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class PlayWarAnsweringFlowContainer {
    private Map<Long, MemberWisieFlow> profileIdWisieFlowMap = new ConcurrentHashMap<>();
    private Map<String, GameFlow> flowMap = new ConcurrentHashMap<>();
    private Map<String, String> outerToInnerMap = new ConcurrentHashMap<>();

    public void stopAll() {
        for (GameFlow flow : flowMap.values()) {
            flow.stop();
        }
    }

    public void addWisieFlow(MemberWisieFlow flow) {
        flowMap.put(flow.getId(), flow);
        profileIdWisieFlowMap.put(flow.getManager().getContainer().getTeam().getProfileId(), flow);
    }

    public void addAndRunInner(GameFlow inner, Long profileId) {
        GameFlow outer = stopMostOuter(profileId);
        addInner(inner, outer);
    }

    private void addInner(GameFlow inner, GameFlow outer) {
        flowMap.put(inner.getId(), inner);
        outerToInnerMap.put(outer.getId(), inner.getId());
    }

    public void removeAllOutersAndRun(Long profileId) {
        MemberWisieFlow flow = profileIdWisieFlowMap.get(profileId);
        String id = flow.getId();
        while (outerToInnerMap.containsKey(id)) {
            String nextId = outerToInnerMap.get(id);
            flowMap.get(nextId).stop();
            outerToInnerMap.remove(id);
            id = nextId;
        }
        flow.resume();
    }

    public void runPrevious(Long profileId) {
        String previous = removeMostOuter(profileId);
        flowMap.get(previous).resume();
    }

    private GameFlow stopMostOuter(Long profileId) {
        GameFlow outer = findMostOuterByProfileId(profileId);
        outer.stop();
        return outer;
    }


    private String removeMostOuter(Long profileId) {
        String previous = findInnerOfMostOuter(findWisieFlowByProfileId(profileId).getId());
        if (outerToInnerMap.containsKey(previous)) {
            outerToInnerMap.remove(previous);
        }
        return previous;
    }

    public MemberWisieManager getWisieManager(Long profileId) {
        return findWisieFlowByProfileId(profileId).getManager();
    }

    public MemberWisieFlow findWisieFlowByProfileId(Long profileId) {
        return profileIdWisieFlowMap.get(profileId);
    }

    public GameFlow findMostOuterByProfileId(Long profileId) {
        MemberWisieFlow wisieFlow = findWisieFlowByProfileId(profileId);
        return flowMap.get(findMostOuter(wisieFlow.getId()));
    }

    public String findMostOuter(String id) {
        if (outerToInnerMap.containsKey(id)) {
            return findMostOuter(outerToInnerMap.get(id));
        }
        return id;
    }

    public String findInnerOfMostOuter(String id) {
        if (outerToInnerMap.containsKey(id)) {
            String nextId = outerToInnerMap.get(id);
            if (outerToInnerMap.containsKey(nextId)) {
                return findInnerOfMostOuter(nextId);
            }
            return id;
        }
        return id;
    }

}
