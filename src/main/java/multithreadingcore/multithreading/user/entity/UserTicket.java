package multithreadingcore.multithreading.user.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import multithreadingcore.multithreading.ticket.entity.Ticket;

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
        this.user = user;
        user.getTickets().add(this);
    }


}
