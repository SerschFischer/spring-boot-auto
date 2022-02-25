package de.galvanize.autos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutomobilesServiceTest {

    @Mock
    AutomobileRepository automobileRepository;
    private AutomobileService automobileService;

    @BeforeEach
    void setUp() {
        automobileService = new AutomobileService(automobileRepository);
    }

    @Test
    void getAutomobiles_noArgs_returnList() {
        // GIVEN | ARRANGE
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "RED", "Bob", "ACC");
        // WHEN | ACT
        when(automobileRepository.findAll()).thenReturn(List.of(automobile));
        AutomobileList automobileList = automobileService.getAutomobiles();
        // THEN | ASSERT
        assertThat(automobileList).isNotNull();
        assertThat(automobileList.isEmpty()).isFalse();
    }

    @Test
    void getAutomobiles_search_returnList() {
        // GIVEN | ARRANGE
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "RED", "Bob", "ACC");
        automobile.setColor("RED");
        // WHEN | ACT
        when(automobileRepository.findByColorContainsAndMakeContains(anyString(), anyString()))
                .thenReturn(List.of(automobile));
        AutomobileList automobileList = automobileService.getAutomobiles("RED", "Ford");
        // THEN | ASSERT
        assertThat(automobileList).isNotNull();
        assertThat(automobileList.isEmpty()).isFalse();
    }

    @Test
    void addAutomobile_valid_returnsAutomobile() {
        // GIVEN | ARRANGE
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "RED", "Bob", "ACC");
        automobile.setColor("RED");
        // WHEN | ACT
        when(automobileRepository.save(any(Automobile.class)))
                .thenReturn(automobile);
        Automobile returnedAutomobile = automobileService.addAutomobile(automobile);
        // THEN | ASSERT
        assertThat(returnedAutomobile).isNotNull();
        assertThat(returnedAutomobile.getMake()).isEqualTo("Ford");
    }

    @Test
    void getAutomobile_withVin_returnAutomobile() {
        // GIVEN | ARRANGE
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "RED", "Bob", "ACC");
        automobile.setColor("RED");
        // WHEN | ACT
        when(automobileRepository.findByVin(anyString()))
                .thenReturn(Optional.of(automobile));
        Automobile returnedAutomobile = automobileService.getAutomobile(automobile.getVin());
        // THEN | ASSERT
        assertThat(returnedAutomobile).isNotNull();
        assertThat(returnedAutomobile.getMake()).isEqualTo("Ford");
    }

    @Test
    void updateAutomobile_returnsAutomobile() {
        // GIVEN | ARRANGE
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "RED", "Bob", "ACC");
        automobile.setColor("RED");
        // WHEN | ACT
        when(automobileRepository.findByVin(anyString()))
                .thenReturn(Optional.of(automobile));
        when(automobileRepository.save(any(Automobile.class))).thenReturn(automobile);
        Automobile returnedAutomobile = automobileService.updateAutomobile(
                automobile.getVin(), "PURPLE", "ANYBODY");
        // THEN | ASSERT
        assertThat(returnedAutomobile).isNotNull();
        assertThat(returnedAutomobile.getMake()).isEqualTo("Ford");
    }

    @Test
    void deleteAutomobile_byVin() {
        // GIVEN | ARRANGE
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "RED", "Bob", "ACC");
        automobile.setColor("RED");
        when(automobileRepository.findByVin(anyString()))
                .thenReturn(Optional.of(automobile));
        // WHEN | ACT
        automobileService.deleteAutomobile(automobile.getVin());
        // THEN | ASSERT
        verify(automobileRepository).delete(any(Automobile.class));
    }

    @Test
    void deleteAutomobile_byVin_notExists() {
        // GIVEN | ARRANGE
        when(automobileRepository.findByVin(anyString()))
                .thenReturn(Optional.empty());
        // WHEN | ACT

        // THEN | ASSERT
        assertThatExceptionOfType(AutomobileNotFountException.class)
                .isThrownBy(() -> {
                    automobileService.deleteAutomobile("VIN-NOT-EXISTS");
                });
    }
}