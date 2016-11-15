package duckling.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MockThreadPool extends ThreadPoolExecutor {
    private List<Runnable> threads = new ArrayList<>();

    public MockThreadPool() {
        this(1, 1, 1, TimeUnit.SECONDS, new SynchronousQueue<>());
    }

    public MockThreadPool(
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue
    ) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public void execute(Runnable runnable) {
        this.threads.add(runnable);
    }

    public int getThreadCount() {
        return this.threads.size();
    }
}
