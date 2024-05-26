package multithreadingcore.multithreading.ticket.controller;


import io.micrometer.observation.Observation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import multithreadingcore.multithreading.lock.UserLevelLockJdbcAPI;
import multithreadingcore.multithreading.ticket.application.TicketService;
import multithreadingcore.multithreading.ticket.dto.repsonse.TicketResponse;
import multithreadingcore.multithreading.ticket.dto.request.TicketRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final UserLevelLockJdbcAPI userLevelLockJdbcAPI;

    private static final int LOCK_TIMEOUT_SECONDS = 3;

    @PostMapping("/buy")
    public ResponseEntity<TicketResponse> ticketBuy(
            @RequestBody TicketRequest ticketRequest
            ) {

        Integer lock = userLevelLockJdbcAPI.executeWithLock(
                String.valueOf(ticketRequest.getUserId()),
                LOCK_TIMEOUT_SECONDS,
                () -> ticketService.userLevelLockBuy(ticketRequest.getUserId(), ticketRequest.getTicketId(), ticketRequest.getQuantity()));

        log.info("구매 정보 = {}" , lock);

        TicketResponse result = TicketResponse.builder().ticket(lock).build();


        return ResponseEntity.ok().body(result);
    }
}
