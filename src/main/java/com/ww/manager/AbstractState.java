package com.ww.manager;

import com.ww.helper.Loggable;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Setter
@Getter
public abstract class AbstractState implements FlowRunnable, Loggable {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractState.class);
    public static String STATE_TYPE_FLOWABLE = "flowable";
    public static String STATE_TYPE_DECISION = "decision";
    public static String STATE_TYPE_VOID = "void";

    private String type;
    private List<Consumer<? super Long>> onFlowableEndListeners = new CopyOnWriteArrayList<>();
    private List<Disposable> disposables = new CopyOnWriteArrayList<>();

    public long intervalMultiply() {
        return 1000;
    }

    public AbstractState addOnFlowableEndListener(Consumer<? super Long> onNext) {
        onFlowableEndListeners.add(onNext);
        return this;
    }

    public AbstractState dispose() {
        logger.trace("AbstractState dispose " + describe());
        for (Disposable disposable : disposables) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        disposables.clear();
        return this;
    }

    public String describe() {
        return "class=" + this.getClass().getName() + "disposablesCount=" + disposables.size() + ", isRunning=" + isRunning() + ", type=" + type + ", onFlowableEndListenersCount=" + onFlowableEndListeners.size();
    }

    protected boolean startIfRunning() {
        boolean isRunning = isRunning();
        logger.trace("AbstractState startIfRunning " + describe());
        return isRunning;
    }

    public void startFlowableEndListeners() {
        logger.trace("AbstractState startFlowableEndListeners " + describe());
        for (Consumer<? super Long> onFlowableEndListener : onFlowableEndListeners) {
            try {
                onFlowableEndListener.accept(0L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startFlowable() {
        if (!startIfRunning()) {
            return;
        }
        Flowable<Long> flowable = processFlowable();
        for (Consumer onFlowableEndListener : onFlowableEndListeners) {
            disposables.add(flowable.subscribe(onFlowableEndListener));
        }
        logger.trace("AbstractState startFlowable end " + describe());
    }

    protected Flowable<Long> processFlowable() {
        return Flowable.empty();
    }

    public void startVoid() {
        if (!startIfRunning()) {
            return;
        }
        processVoid();
        logger.trace("AbstractState startVoid end " + describe());
    }

    protected void processVoid() {
    }

    public Boolean startBoolean() {
        if (!startIfRunning()) {
            return null;
        }
        Boolean result = processBoolean();
        logger.trace("AbstractState startBoolean end " + describe() + ", result=" + result);
        return result;
    }

    protected Boolean processBoolean() {
        return null;
    }
}
