package multithreadingcore.multithreading.lock;

import lombok.extern.slf4j.Slf4j;
import multithreadingcore.multithreading.ticket.application.TicketService;
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

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class UserLevelLockJdbcAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    UserTicketRepository userTicketRepository;

    @Autowired
    TicketService ticketService;

    @MockBean
    private UserLevelLockJdbcAPI userLevelLockJdbcAPI;


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
    public void 구매() throws Exception {
        when(userLevelLockJdbcAPI.executeWithLock(anyString(), anyInt(), any())).thenReturn(1);

        int threadCount = 20;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            final long userId = i;
            executorService.submit(() -> {
                try {
                    mvc.perform(post("/buy")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(String.format("""
                                            {
                                                "userId": %d,
                                                "ticketId": 1,
                                                "quantity": 1
                                            }
                                            """, userId)))
                            .andExpect(status().isOk())  // HTTP 상태 코드 검증
                            .andDo(print());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(); // 기다림 모든 스레드``가 완료될 때까지

        // 여기서 필요한 경우에 모의 객체의 특정 메소드 호출을 검증
        verify(userLevelLockJdbcAPI, times(threadCount)).executeWithLock(anyString(), eq(3), any());
        executorService.shutdown();

        Ticket ticket = ticketRepository.findById(1L).orElseThrow();

        log.info("티켓 정보 = {}", ticket);

    }

}