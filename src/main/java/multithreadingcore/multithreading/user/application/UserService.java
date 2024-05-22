package multithreadingcore.multithreading.user.application;


import lombok.RequiredArgsConstructor;
import multithreadingcore.multithreading.ticket.entity.Ticket;
import multithreadingcore.multithreading.user.entity.UserTicket;
import multithreadingcore.multithreading.ticket.repository.TicketRepository;
import multithreadingcore.multithreading.user.repository.UserTicketRepository;
import multithreadingcore.multithreading.user.entity.User;
import multithreadingcore.multithreading.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;


@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
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


    @Transactional
    public void buyWithPessimisticLock(Long userId, Long ticketId, Long quantity) {

        User user = userRepository.findById(userId).orElseThrow();
        log.info("사용자에 대한 정보 = {}" , user);

        Ticket ticket = ticketRepository.findByIdWithPessimisticLock(ticketId);
        log.info("티켓 수량 대한 정보 = {}" , ticket.getQuantity());

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
