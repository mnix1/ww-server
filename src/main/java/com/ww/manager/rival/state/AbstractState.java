package com.ww.manager.rival.state;

import com.ww.helper.Loggable;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

@Setter
@Getter
public abstract class AbstractState implements FlowRunnable, Loggable {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractState.class);
    public static String STATE_TYPE_FLOWABLE = "flowable";
    public static String STATE_TYPE_DECISION = "decision";
    public static String STATE_TYPE_VOID = "void";

    private String type;
    private CopyOnWriteArrayList<Consumer> onFlowableEndListeners = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Disposable> disposables = new CopyOnWriteArrayList<>();

    public AbstractState addOnFlowableEndListener(Consumer<? super Long> onNext) {
        onFlowableEndListeners.add(onNext);
        return this;
    }

    public AbstractState dispose() {
        logger.trace("AbstractState in dispose before for, disposablesCount=" + disposables.size());
        for (Disposable disposable : disposables) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        disposables.clear();
        return this;
    }

    protected boolean startIfRunning() {
        boolean isRunning = isRunning();
        logger.trace(describe() + ", isRunning=" + isRunning);
        return isRunning;
    }

    public AbstractState startFlowable() {
        if (!startIfRunning()) {
            return this;
        }
        Flowable<Long> flowable = processFlowable();
        for (Consumer onFlowableEndListener : onFlowableEndListeners) {
            disposables.add(flowable.subscribe(onFlowableEndListener));
        }
        return this;
    }

    protected Flowable<Long> processFlowable() {
        return Flowable.empty();
    }

    public void startVoid() {
        if (!startIfRunning()) {
            return;
        }
        processVoid();
    }

    protected void processVoid() {
    }

    public Boolean startBoolean() {
        if (!startIfRunning()) {
            return null;
        }
        return processBoolean();
    }

    protected Boolean processBoolean() {
        return null;
    }
}
