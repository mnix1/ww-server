package com.ww.manager;

import com.ww.helper.Describe;
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
public abstract class AbstractState implements FlowRunnable, Describe {
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
        logger.trace(describe() + " dispose");
        for (Disposable disposable : disposables) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        disposables.clear();
        return this;
    }

    public String describe() {
        return "class=" + this.getClass().getName() + ", type=" + type;
    }

    protected boolean startIfRunning() {
        return isRunning();
    }

    public void startFlowableEndListeners() {
        logger.trace(describe() + " startFlowableEndListeners");
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
            logger.trace(describe() + " NO startFlowable");
            return;
        }
        Flowable<Long> flowable = processFlowable();
        for (Consumer onFlowableEndListener : onFlowableEndListeners) {
            disposables.add(flowable.subscribe(onFlowableEndListener));
        }
        logger.trace(describe() + " startFlowable");
    }

    protected Flowable<Long> processFlowable() {
        return Flowable.empty();
    }

    public void startVoid() {
        if (!startIfRunning()) {
            logger.trace(describe() + " NO startVoid");
            return;
        }
        processVoid();
        logger.trace(describe() + " startVoid");
    }

    protected void processVoid() {
    }

    public Boolean startBoolean() {
        if (!startIfRunning()) {
            logger.trace(describe() + " NO startBoolean");
            return null;
        }
        Boolean result = processBoolean();
        logger.trace(describe() + " startBoolean, result=" + result);
        return result;
    }

    protected Boolean processBoolean() {
        return null;
    }
}
