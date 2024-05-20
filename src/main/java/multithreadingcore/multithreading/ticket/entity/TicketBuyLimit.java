package multithreadingcore.multithreading.ticket.entity;


import lombok.Getter;

@Getter
public enum TicketBuyLimit {

    MAXIMUM_TICKET_COUNT(2);

    private final int value;

    TicketBuyLimit(int value) {
        this.value = value;
    }
}
