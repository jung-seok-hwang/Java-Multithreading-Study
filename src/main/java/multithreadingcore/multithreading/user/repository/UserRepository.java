package multithreadingcore.multithreading.user.repository;

import multithreadingcore.multithreading.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
