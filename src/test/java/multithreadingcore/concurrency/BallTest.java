package multithreadingcore.concurrency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class BallTest {
    private final Ball ball = new Ball();

    @BeforeEach
    public void init() {
        ball.save("레드");
        ball.save("블루");
        ball.save("레드");
        ball.save("레드");
        ball.save("그린");
        ball.save("레드");
        ball.save("블루");
    }

    @Test
    public void ball_check() throws InterruptedException {

        int threadCount = 2;

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    ball.check();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        System.out.println(ball.getState());
    }
}