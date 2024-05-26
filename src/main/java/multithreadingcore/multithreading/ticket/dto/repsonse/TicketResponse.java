package multithreadingcore.multithreading.ticket.dto.repsonse;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class TicketResponse {

    private Integer ticket;

    @Builder
    public TicketResponse(Integer ticket) {
        this.ticket = ticket;
    }
}
