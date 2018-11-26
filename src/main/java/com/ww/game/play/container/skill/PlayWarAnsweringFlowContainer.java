package com.ww.game.play.container.skill;

import com.ww.game.GameFlow;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.flow.MemberWisieFlow;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class PlayWarAnsweringFlowContainer {
    private List<MemberWisieFlow> wisieFlows = new CopyOnWriteArrayList<>();
    private Map<Long, MemberWisieFlow> profileIdWisieFlowMap = new ConcurrentHashMap<>();
    private Map<String, GameFlow> flowMap = new ConcurrentHashMap<>();
    private Map<String, String> outerToInnerMap = new ConcurrentHashMap<>();

    public void addWisieFlow(MemberWisieFlow flow) {
        flowMap.put(flow.getId(), flow);
        wisieFlows.add(flow);
        profileIdWisieFlowMap.put(flow.getManager().getContainer().getTeam().getProfileId(), flow);
    }

    public void addInner(GameFlow inner, GameFlow outer) {
        flowMap.put(inner.getId(), inner);
        outerToInnerMap.put(outer.getId(), inner.getId());
    }

    public void removeAllOuters(Long profileId) {
        String id = profileIdWisieFlowMap.get(profileId).getId();
        GameFlow outer = findMostOuterByProfileId(profileId);
        while (!id.equals(outer.getId())) {
            outer.stop();
            removeMostOuter(profileId);
        }
    }

    public void addAndRunInner(GameFlow inner, Long profileId) {
        GameFlow outer = findMostOuterByProfileId(profileId);
        outer.stop();
        addInner(inner, outer);
    }

    public void runPrevious(Long profileId) {
        String previous = removeMostOuter(profileId);
        flowMap.get(previous).resume();
    }

    public void runMostOuter(Long profileId) {
        GameFlow outer = findMostOuterByProfileId(profileId);
        outer.resume();
    }

    public String removeMostOuter(Long profileId) {
        GameFlow outer = findMostOuterByProfileId(profileId);
        outer.stop();
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
