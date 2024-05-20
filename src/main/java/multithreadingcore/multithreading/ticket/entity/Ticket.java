package multithreadingcore.multithreading.ticket.entity;


import jakarta.persistence.*;
import lombok.*;
import multithreadingcore.multithreading.user.entity.User;

@Entity
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long serial;



}
