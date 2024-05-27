package multithreadingcore.multithreading.lock;

import lombok.extern.slf4j.Slf4j;
import multithreadingcore.multithreading.ticket.entity.Ticket;
import multithreadingcore.multithreading.ticket.repository.TicketRepository;
import multithreadingcore.multithreading.user.entity.User;
import multithreadingcore.multithreading.user.repository.UserRepository;
import multithreadingcore.multithreading.user.repository.UserTicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class UserLevelLockJdbcAPITest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserTicketRepository userTicketRepository;

    private final List<User> users = new ArrayList<>();

    @BeforeEach
    public void beforeTest() {
        String serial = UUID.randomUUID().toString();
        Ticket testTicket = Ticket.builder()
                .serial(serial)
                .quantity(20L)
                .build();
        ticketRepository.save(testTicket);

        for (int i = 0; i < 20; i++) {
            User user = User.builder().build();
            userRepository.save(user);
            users.add(user);
        }
    }

    @AfterEach
    public void afterTest() {
        userTicketRepository.deleteAll();
        ticketRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void 구매() throws Exception {
        int threadCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<Future<ResultActions>> futures = new ArrayList<>();

        for (long i = 1; i <= threadCount; i++) {
            long userid = i;
            Callable<ResultActions> task = () -> {
                ResultActions result = mvc.perform(post("/buy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("""
                                        {
                                            "userId": %d,
                                            "ticketId": 1,
                                            "quantity": 1
                                        }
                                        """, userid)))
                        .andExpect(status().isOk())
                        .andDo(print());
                latch.countDown();
                return result;
            };
            futures.add(executorService.submit(task));
        }

        latch.await();
        executorService.shutdown();

        for (Future<ResultActions> future : futures) {
            future.get().andExpect(jsonPath("$.ticket").value(1));
        }
    }
}