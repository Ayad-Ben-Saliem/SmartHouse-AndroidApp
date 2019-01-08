package ly.rqmana.smarthouse;

import java.util.concurrent.atomic.AtomicBoolean;

public interface InterruptableRunnable extends Runnable {

    AtomicBoolean interrupt = new AtomicBoolean(false);

    default public void interrupt() {
        interrupt.set(false);
    }

    default public boolean isInterrupted() {
        return interrupt.get();
    }
}
