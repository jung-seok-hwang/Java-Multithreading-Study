package multithreadingcore.multithreading.user.application;


import multithreadingcore.multithreading.ticket.entity.Ticket;
import multithreadingcore.multithreading.ticket.repository.TicketRepository;
import multithreadingcore.multithreading.user.repository.UserTicketRepository;
import multithreadingcore.multithreading.user.entity.User;
import multithreadingcore.multithreading.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    UserTicketRepository userTicketRepository;

    @Autowired
    UserService userService;

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
    @DisplayName("Race Condition Occurs on Ticket Purchase")
    public void 구매 () throws InterruptedException {

        int threadCount = 20;
        //thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            final long userId = i; // assuming user IDs are assigned sequentially starting from 1
            executorService.submit(() -> {
                try {
                    userService.buy(userId , 1L , 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Ticket ticket = ticketRepository.findById(1L).orElseThrow();

        Assertions.assertEquals(0, ticket.getQuantity());
    }


}