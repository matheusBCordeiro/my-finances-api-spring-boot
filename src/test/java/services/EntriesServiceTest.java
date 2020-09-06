package services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.matheuscordeiro.myfinancesapi.exceptions.BusinessException;
import com.matheuscordeiro.myfinancesapi.model.entities.Entries;
import com.matheuscordeiro.myfinancesapi.model.entities.User;
import com.matheuscordeiro.myfinancesapi.model.entities.enums.EntriesStatus;
import com.matheuscordeiro.myfinancesapi.model.entities.enums.EntriesType;
import com.matheuscordeiro.myfinancesapi.model.repositories.EntriesRepository;
import com.matheuscordeiro.myfinancesapi.services.EntriesService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class EntriesServiceTest {
    @SpyBean
    EntriesService entriesService;

    @MockBean
    EntriesRepository entriesRepository;

    @Test
    public void mustSaveAnEntries() {
        Entries entriesSave = createEntries();
        doNothing().when(entriesService).validateEntries(entriesSave);

        Entries saveEntries = createEntries();
        saveEntries.setId(1l);
        saveEntries.setStatus(EntriesStatus.PENDING);
        when(entriesRepository.save(entriesSave)).thenReturn(saveEntries);

        Entries entries = entriesService.saveEntries(entriesSave);

        assertThat( entries.getId() ).isEqualTo(saveEntries.getId());
        assertThat(entries.getStatus()).isEqualTo(EntriesStatus.PENDING);
    }

    @Test
    public void mustSaveAEntriesWhenThereIsAValidationError() {
        Entries saveEntries = createEntries();
        doThrow( BusinessException.class ).when(entriesService).validateEntries(saveEntries);

        catchThrowableOfType( () -> entriesService.saveEntries(saveEntries), BusinessException.class );
        verify(entriesRepository, never()).save(saveEntries);
    }

    @Test
    public void mustUpdateAEntries() {
        Entries saveEntries = createEntries();
        saveEntries.setId(1l);
        saveEntries.setStatus(EntriesStatus.PENDING);

        doNothing().when(entriesService).validateEntries(saveEntries);

        when(entriesRepository.save(saveEntries)).thenReturn(saveEntries);

        entriesService.updateEntries(saveEntries);

        verify(entriesRepository, times(1)).save(saveEntries);

    }

    @Test
    public void shouldThrowErrorWhenTryingToUpdateAEntriesThatHasNotBeenSaved() {
        Entries entries = createEntries();

        catchThrowableOfType( () -> entriesService.updateEntries(entries), NullPointerException.class );
        verify(entriesRepository, never()).save(entries);
    }

    @Test
    public void mustDeleteAnEntries() {
       Entries entries = createEntries();
        entries.setId(1l);

        entriesService.deleteEntries(entries);

        verify( entriesRepository ).delete(entries);
    }

    @Test
    public void shouldThrowErrorWhenTryingToDeleteEntriesThatHaveNotBeenSaved(){
        Entries entries = createEntries();

        catchThrowableOfType( () -> entriesService.deleteEntries(entries), NullPointerException.class );

        verify( entriesRepository, never() ).delete(entries);
    }


    @Test
    public void mustFilterEntries() {
        Entries entries = createEntries();
        entries.setId(1l);

        List<Entries> entriesList = Arrays.asList(entries);
        when( entriesRepository.findAll(any(Example.class)) ).thenReturn(entriesList);

        List<Entries> result = entriesService.getEntries(entries);

        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .contains(entries);

    }

    @Test
    public void deveAtualizarOStatusDeUmLancamento() {
        Entries entries = createEntries();
        entries.setId(1l);
        entries.setStatus(EntriesStatus.PENDING);

        EntriesStatus status = EntriesStatus.PAID;
        doReturn(entries).when(entriesService).updateEntries(entries);

        entriesService.updateStatus(entries, status);

        assertThat(entries.getStatus()).isEqualTo(status);
        verify(entriesService).updateEntries(entries);

    }

    @Test
    public void mustGetEntriesById() {
        Long id = 1l;

        Entries entries = createEntries();
        entries.setId(id);

        when(entriesRepository.findById(id)).thenReturn(Optional.of(entries));

        Optional<Entries> result =  entriesService.getById(id);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void shouldReturnEmptyWhenEntriesDoesNotExist() {
        Long id = 1l;

        Entries entries = createEntries();
        entries.setId(id);

        when( entriesRepository.findById(id) ).thenReturn( Optional.empty() );

        Optional<Entries> result = entriesService.getById(id);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void shouldLaunchErrorsWhenValidatingEntries() {
        Entries entries = new Entries();

        Throwable error = Assertions.catchThrowable( () -> entriesService.validateEntries(entries) );
        assertThat(error).isInstanceOf(BusinessException.class).hasMessage("Informe uma Descrição válida.");

        entries.setDescription("");

        error = Assertions.catchThrowable( () -> entriesService.validateEntries(entries) );
        assertThat(error).isInstanceOf(BusinessException.class).hasMessage("Informe uma Descrição válida.");

        entries.setDescription("Salario");

        error = Assertions.catchThrowable( () -> entriesService.validateEntries(entries) );
        assertThat(error).isInstanceOf(BusinessException.class).hasMessage("Informe um Mês válido.");

        entries.setYear(0);

        error = catchThrowable( () -> entriesService.validateEntries(entries) );
        assertThat(error).isInstanceOf(BusinessException.class).hasMessage("Informe um Mês válido.");

        entries.setYear(13);

        error = catchThrowable( () -> entriesService.validateEntries(entries) );
        assertThat(error).isInstanceOf(BusinessException.class).hasMessage("Informe um Mês válido.");

        entries.setMonth(1);

        error = catchThrowable( () -> entriesService.validateEntries(entries) );
        assertThat(error).isInstanceOf(BusinessException.class).hasMessage("Informe um Ano válido.");

        entries.setYear(202);

        error = catchThrowable( () -> entriesService.validateEntries(entries) );
        assertThat(error).isInstanceOf(BusinessException.class).hasMessage("Informe um Ano válido.");

        entries.setYear(2020);

        error = catchThrowable( () -> entriesService.validateEntries(entries) );
        assertThat(error).isInstanceOf(BusinessException.class).hasMessage("Informe um Usuário.");

        entries.setUser(new User());

        error = catchThrowable( () -> entriesService.validateEntries(entries) );
        assertThat(error).isInstanceOf(BusinessException.class).hasMessage("Informe um Usuário.");

        entries.getUser().setId(1l);

        error = catchThrowable( () -> entriesService.validateEntries(entries) );
        assertThat(error).isInstanceOf(BusinessException.class).hasMessage("Informe um Valor válido.");

        entries.setValue(BigDecimal.ZERO);

        error = catchThrowable( () -> entriesService.validateEntries(entries) );
        assertThat(error).isInstanceOf(BusinessException.class).hasMessage("Informe um Valor válido.");

        entries.setValue(BigDecimal.valueOf(1));

        error = catchThrowable( () -> entriesService.validateEntries(entries) );
        assertThat(error).isInstanceOf(BusinessException.class).hasMessage("Informe um tipo de Lançamento.");

    }

    @Test
    public void mustGetBalanceByUser() {
        Long userId = 1l;

        when( entriesRepository
                .getBalanceByEntriesTypeAndUserAndStatus(userId, EntriesType.REVENUE, EntriesStatus.PAID))
                .thenReturn(BigDecimal.valueOf(100));

        when( entriesRepository
                .getBalanceByEntriesTypeAndUserAndStatus(userId, EntriesType.EXPENDITURE, EntriesStatus.PAID))
                .thenReturn(BigDecimal.valueOf(50));

        BigDecimal balance = entriesService.getBalanceByUser(userId);

        assertThat(balance).isEqualTo(BigDecimal.valueOf(50));

    }

    private Entries createEntries() {
        return Entries.builder()
                .year(2020)
                .month(9)
                .description("Pagamento")
                .value(BigDecimal.valueOf(10))
                .type(EntriesType.REVENUE)
                .status(EntriesStatus.PENDING)
                .dateRegister(LocalDate.now())
                .build();
    }
}