package multithreadingcore.multithreading.user.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import multithreadingcore.multithreading.ticket.entity.Ticket;
import multithreadingcore.multithreading.ticket.entity.TicketBuyLimit;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public void addUser(User user) {
//        if (user.getTickets().size() < TicketBuyLimit.MAXIMUM_TICKET_COUNT.getValue()) {
//            throw new IllegalArgumentException("2개 이상 구매 불가능합니다.");
//        }
        this.user = user;
        user.getTickets().add(this);
    }


}
