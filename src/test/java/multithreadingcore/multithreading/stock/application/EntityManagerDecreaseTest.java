package multithreadingcore.multithreading.stock.application;

import jakarta.persistence.EntityManager;
import multithreadingcore.multithreading.stock.entity.Stock;
import multithreadingcore.multithreading.stock.repository.StockRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
public class EntityManagerDecreaseTest {

    @Autowired
    private PessimisticLockStockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
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
                    int count = 1;
                    stockService.emPessimisticLockWriteDecrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Stock stock = stockRepository.findById(1L).orElseThrow();

        Assertions.assertEquals(0, stock.getQuantity());
    }
}
