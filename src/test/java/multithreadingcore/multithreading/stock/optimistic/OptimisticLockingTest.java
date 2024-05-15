package multithreadingcore.multithreading.stock.optimistic;

import multithreadingcore.multithreading.coupon.entity.Coupon;
import multithreadingcore.multithreading.coupon.repository.CouponRepository;
import multithreadingcore.multithreading.stock.application.PessimisticLockStockService;
import multithreadingcore.multithreading.stock.entity.Stock;
import multithreadingcore.multithreading.stock.repository.StockRepository;
import multithreadingcore.multithreading.util.OptimisticLockConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
public class OptimisticLockingTest {


    @Autowired
    private OptimisticLockConfig optimisticLockConfig;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    public void before() {
        couponRepository.save(new Coupon(1L, 100L));
    }

    @AfterEach
    public void after() {
        couponRepository.deleteAll();
    }

    @Test
    @DisplayName("EntityManager PessimisticLock 을 이용한 동시에 100개의 요청 처리")
    public void emPessimisticLockWriteDecrease() throws InterruptedException {
        int threadCount = 100;
        //thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    optimisticLockConfig.decrease(1L, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Coupon coupon = couponRepository.findById(1L).orElseThrow();

        Assertions.assertEquals(0, coupon.getQuantity());
    }
}
