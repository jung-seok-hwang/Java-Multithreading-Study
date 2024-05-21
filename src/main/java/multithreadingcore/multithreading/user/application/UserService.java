package multithreadingcore.multithreading.user.application;


import lombok.RequiredArgsConstructor;
import multithreadingcore.multithreading.ticket.entity.Ticket;
import multithreadingcore.multithreading.user.entity.UserTicket;
import multithreadingcore.multithreading.ticket.repository.TicketRepository;
import multithreadingcore.multithreading.user.repository.UserTicketRepository;
import multithreadingcore.multithreading.user.entity.User;
import multithreadingcore.multithreading.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final UserTicketRepository userTicketRepository;

    @Transactional
    public void buy(Long userId, Long ticketId, Long quantity) {
        User user = userRepository.findById(userId).orElseThrow();

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();

        ticket.decrease(quantity);
        user.maxTicketCount(quantity);

        ticketRepository.saveAndFlush(ticket);

        UserTicket userTicket = UserTicket.builder()
                .user(user)
                .ticket(ticket)
                .build();

        userTicket.addUser(user);

        userTicketRepository.save(userTicket);
    }
}
