package multithreadingcore.multithreading.explicit;


import multithreadingcore.multithreading.ticket.entity.Ticket;
import multithreadingcore.multithreading.ticket.repository.TicketRepository;
import multithreadingcore.multithreading.user.entity.User;
import multithreadingcore.multithreading.user.repository.UserRepository;
import multithreadingcore.multithreading.user.repository.UserTicketRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class ExplicitSynchronizationServiceTest {


    @Autowired
    private ExplicitSynchronizationService explicitSynchronizationService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    UserTicketRepository userTicketRepository;

    @BeforeEach
    public void beforeTest() {

        String serial = UUID.randomUUID().toString();

        //한개 의 티켓
        Ticket ticket = Ticket.builder()
                .serial(serial)
                .quantity(20L)
                .build();
        ticketRepository.save(ticket);

        //20명의 사용자
        for (int i = 0; i < 20; i++) {
            User user = User.builder().build();
            userRepository.save(user);
        }
    }

    @AfterEach
    public void afterTest() {
        userTicketRepository.deleteAll();
        ticketRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void emPessimisticLockWriteDecrease() throws InterruptedException {
        int threadCount = 30;
        //thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    int count = 1;
                    explicitSynchronizationService.perform(1L, 1L , 1L);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Ticket ticket = ticketRepository.findById(1L).orElseThrow();

        assertEquals(0, ticket.getQuantity());
    }
}