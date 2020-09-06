package model.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.matheuscordeiro.myfinancesapi.model.entities.Entries;
import com.matheuscordeiro.myfinancesapi.model.entities.enums.EntriesStatus;
import com.matheuscordeiro.myfinancesapi.model.entities.enums.EntriesType;
import com.matheuscordeiro.myfinancesapi.model.repositories.EntriesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class EntriesRepositoryTest {
    @Autowired
    EntriesRepository entriesRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void mustSaveAnEntries() {
        Entries entries = createEntries();

        entries = entriesRepository.save(entries);

        assertThat(entries.getId()).isNotNull();
    }

    @Test
    private void mustDeleteAnEntries() {
        Entries entries = createAndPersistAEntries();

        entries = testEntityManager.find(Entries.class, entries.getId());

        entriesRepository.delete(entries);

        Entries nonexistentEntries = testEntityManager.find(Entries.class, entries.getId());
        assertThat(nonexistentEntries).isNull();
    }

    @Test
    private void mustUpdateAEntries() {
        Entries entries = createAndPersistAEntries();

        entries.setYear(2018);
        entries.setDescription("Teste Atualizar");
        entries.setStatus(EntriesStatus.CANCELED);

        entriesRepository.save(entries);

        Entries entriesUpdate = testEntityManager.find(Entries.class, entries.getId());

        assertThat(entriesUpdate.getYear()).isEqualTo(2018);
        assertThat(entriesUpdate.getDescription()).isEqualTo("Teste Atualizar");
        assertThat(entriesUpdate.getStatus()).isEqualTo(EntriesStatus.CANCELED);
    }

    @Test
    private void mustGetEntriesById() {
        Entries entries = createAndPersistAEntries();

        Optional<Entries> entriesFound = entriesRepository.findById(entries.getId());

        assertThat(entriesFound.isPresent()).isTrue();
    }

    private Entries createAndPersistAEntries() {
        Entries entries = createEntries();
        testEntityManager.persist(entries);
        return entries;
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
