package multithreadingcore.multithreading.ticket.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TicketRequest {

    private Long userId;
    private Long ticketId;
    private Long quantity;
}
