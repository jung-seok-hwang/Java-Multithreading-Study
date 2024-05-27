package multithreadingcore.multithreading.ticket.entity;


import lombok.Getter;

@Getter
public enum TicketBuyLimit {

    MAXIMUM_TICKET_COUNT(1);

    private final int value;

    TicketBuyLimit(int value) {
        this.value = value;
    }
}
