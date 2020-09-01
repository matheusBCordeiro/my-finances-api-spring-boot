package services;

import com.matheuscordeiro.myfinancesapi.exceptions.AuthenticationErrorException;
import com.matheuscordeiro.myfinancesapi.exceptions.BusinessException;
import com.matheuscordeiro.myfinancesapi.model.entities.User;
import com.matheuscordeiro.myfinancesapi.model.repositories.UserRepository;
import com.matheuscordeiro.myfinancesapi.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

    @SpyBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Test
    public void ShouldSaveUser() {
        Mockito.doNothing().when(userService).validateEmail(Mockito.anyString());
        User user = User.builder()
                .id(1l)
                .name("name")
                .email("email@email.com")
                .password("pass").build();

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User userSave = userService.saveUser(new User());

        Assertions.assertThat(userSave).isNotNull();
        Assertions.assertThat(userSave.getId()).isEqualTo(1l);
        Assertions.assertThat(userSave.getName()).isEqualTo("name");
        Assertions.assertThat(userSave.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(userSave.getPassword()).isEqualTo("pass");
    }
    @Test
    public void MustNotSaveUserWithEmailAlreadyRegistered() {
        String email = "email@email.com";
        User user = User.builder().email(email).build();
        Mockito.doThrow(BusinessException.class).when(userService).validateEmail(email);

        org.junit.jupiter.api.Assertions
                .assertThrows(BusinessException.class, () -> userService.saveUser(user) ) ;

        Mockito.verify( userRepository, Mockito.never() ).save(user);
    }

    @Test
    public void mustAuthenticateAUserWithSuccess() {
        String email = "email@email.com";
        String password = "pass";

        User user = User.builder().email(email).password(password).id(1L).build();
        Mockito.when( userRepository.findByEmail(email) ).thenReturn(Optional.of(user));

        User result = userService.authenticate(email, password);

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void shouldThrowAnErrorWhenCannotFindARegisteredUserWithAnInformedEmail() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable( () -> userService.authenticate("email@email.com", "pass") );

        Assertions.assertThat(exception)
                .isInstanceOf(AuthenticationErrorException.class)
                .hasMessage("Usuário não encontrado para o email informado.");
    }

    @Test
    public void mustValidateAnEmail() {
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        userService.validateEmail("user@email.com");
    }

    @Test
    public void shouldThrowErrorWhenPasswordIsIncorrect() {
        String password = "pass";
        User user = User.builder().email("email@email.com").password(password).build();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

        Throwable exception = Assertions.catchThrowable( () ->  userService.authenticate("email@email.com", "123") );
        Assertions.assertThat(exception).isInstanceOf(AuthenticationErrorException.class).hasMessage("Senha inválida.");
    }

    @Test
    public void shouldThrowErrorWhenValidatingEmailWhenThereIsRegisteredEmail() {
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        userService.validateEmail("user@email.com");
    }
}
