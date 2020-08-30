package services;

import com.matheuscordeiro.myfinancesapi.model.entities.User;
import com.matheuscordeiro.myfinancesapi.model.repositories.UserRepository;
import com.matheuscordeiro.myfinancesapi.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    public void mustValidateAnEmail() {
        userRepository.deleteAll();

        userService.validateEmail("user@email.com");
    }

    @Test
    public void shouldThrowErrorWhenValidatingEmailWhenThereIsRegisteredEmail() {
        User user = User.builder().name("user").email("user@email.com").build();
        userRepository.save(user);

        userService.validateEmail("user@email.com");
    }
}
