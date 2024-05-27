package multithreadingcore.multithreading.ticket.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import multithreadingcore.multithreading.lock.UserLevelLockJdbcAPI;
import multithreadingcore.multithreading.ticket.application.TicketService;
import multithreadingcore.multithreading.ticket.dto.repsonse.TicketResponse;
import multithreadingcore.multithreading.ticket.dto.request.TicketRequest;
import org.springframework.http.ResponseEntity;
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

        Integer userTicket = userLevelLockJdbcAPI.executeWithLock(
                String.valueOf(ticketRequest.getUserId()),
                LOCK_TIMEOUT_SECONDS,
                () -> ticketService.userLevelLockBuy(ticketRequest));

        TicketResponse result = TicketResponse.builder().ticket(userTicket).build();


        return ResponseEntity.ok().body(result);
    }
}
