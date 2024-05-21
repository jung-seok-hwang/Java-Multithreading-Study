package multithreadingcore.multithreading.user.repository;

import multithreadingcore.multithreading.user.entity.UserTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTicketRepository extends JpaRepository<UserTicket, Long> {
}
