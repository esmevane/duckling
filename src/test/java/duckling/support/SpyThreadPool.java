package duckling.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SpyThreadPool extends ThreadPoolExecutor {
    private List<Runnable> threads = new ArrayList<>();

    public SpyThreadPool() {
        this(new SynchronousQueue<>());
    }

    private SpyThreadPool(
        BlockingQueue<Runnable> workQueue
    ) {
        super(1, 1, (long) 1, TimeUnit.SECONDS, workQueue);
    }

    @Override
    public void execute(Runnable runnable) {
        this.threads.add(runnable);
    }

    public int getThreadCount() {
        return this.threads.size();
    }
}
