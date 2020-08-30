package model.repositories;

import com.matheuscordeiro.myfinancesapi.model.entities.User;
import com.matheuscordeiro.myfinancesapi.model.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.assertj.core.api.Assertions;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void shouldCheckForAnEmail(){
        User user = createUser();
        entityManager.persist(user);

        boolean result  = userRepository.existsByEmail("user@email.com");

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenThereIsNoUserRegisteredWithEmail() {
        boolean result  = userRepository.existsByEmail("user@email.com");

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void mustPersistAUserInBaseData() {
        User user = createUser();

        User userSave =  userRepository.save(user);

        Assertions.assertThat(userSave.getId()).isNotNull();
    }

    @Test
    public void  mustSearchAUserByEmail() {
        User user = createUser();
        entityManager.persist(user);

        Optional<User> result =  userRepository.findByEmail("user@email.com");

        Assertions.assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void mustReturnEmptyWhenSearchingUserByEmailWhenItDoesNotExistInTheDatabase () {
        Optional<User> result =  userRepository.findByEmail("user@email.com");

        Assertions.assertThat(result.isPresent()).isFalse();
    }

    @Test
    public static User createUser() {
        return User.builder().name("user").email("user@email.com").password("pass").build();
    }
}
