package com.ww.manager.wisieanswer.state;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class State {
    public static String STATE_TYPE_FLOWABLE = "flowable";
    public static String STATE_TYPE_DECISION = "decision";
    public static String STATE_TYPE_VOID = "void";

    private String type;
    private List<Consumer> onFlowableEndListeners = new ArrayList<>();
    private List<Disposable> disposables = new ArrayList<>();
    protected WisieAnswerManager manager;

    protected State(WisieAnswerManager manager, String type) {
        this.manager = manager;
        this.type = type;
    }

    public State addOnFlowableEndListener(Consumer<? super Long> onNext) {
        onFlowableEndListeners.add(onNext);
        return this;
    }

    public State dispose() {
        for (Disposable disposable : disposables) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        disposables.clear();
        return this;
    }

    public State startFlowable() {
        Flowable<Long> flowable = processFlowable();
        for (Consumer onFlowableEndListener : onFlowableEndListeners) {
            disposables.add(flowable.subscribe(onFlowableEndListener));
        }
        return this;
    }

    protected Flowable<Long> processFlowable() {
        return Flowable.empty();
    }

    public WisieAnswerAction startWisieAnswerAction() {
        return processWisieAnswerAction();
    }

    protected WisieAnswerAction processWisieAnswerAction() {
        return WisieAnswerAction.NONE;
    }

    public void startVoid() {
        processVoid();
    }

    protected void processVoid() {
    }
}
