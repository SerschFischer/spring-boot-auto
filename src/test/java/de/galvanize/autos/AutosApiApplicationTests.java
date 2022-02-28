package de.galvanize.autos;

import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AutosApiApplicationTests {


    @Autowired
    TestRestTemplate testRestTemplate;
    RestTemplate patchRestTemplate;

    @Autowired
    AutomobileRepository automobileRepository;

    Random random = new Random();
    List<Automobile> automobileTestList;

    @BeforeEach
    void setUp() {
        this.automobileTestList = new ArrayList<>();
        Automobile automobile;
        String[] colors = {"RED", "BLUE", "GREEN", "ORANGE", "YELLOW", "BLACK", "BROWN", "ROOT BEER", "MAGENTA", "AMBER"};
        for (int i = 0; i < 50; i++) {
            if (i % 3 == 0) {
                automobile = new Automobile(1967, "Ford", "Mustang", "AABBCC" + (i * 13));
                automobile.setColor(colors[random.nextInt(10)]);
            } else if (i % 2 == 0) {
                automobile = new Automobile(1967, "Dodge", "Viper", "VVBBXX" + (i * 12));
                automobile.setColor(colors[random.nextInt(10)]);
            } else {
                automobile = new Automobile(1967, "Audi", "Quarto", "QQZZAA" + (i * 11));
                automobile.setColor(colors[random.nextInt(10)]);
            }
            this.automobileTestList.add(automobile);
        }
        automobileRepository.saveAll(this.automobileTestList);
    }

    @AfterEach
    void tearDown() {
        automobileRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void getAutomobiles_exists_returnsAutomobileList() {
        // GIVEN | ARRANGE
        // WHEN | ACT
        ResponseEntity<AutomobileList> response = testRestTemplate.getForEntity("/api/autos", AutomobileList.class);
        // THEN | ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isEmpty()).isFalse();
        for (Automobile auto : response.getBody().getAutomobiles()) {
            System.out.println(auto);
        }

    }

    @Test
    void getAutomobile_search_returnsAutomobileList() {
        // GIVEN | ARRANGE
        int sequence = random.nextInt(50);
        String color = automobileTestList.get(sequence).getColor();
        String make = automobileTestList.get(sequence).getMake();
        // WHEN | ACT
        ResponseEntity<AutomobileList> response = testRestTemplate
                .getForEntity(
                        String.format("/api/autos?color=%s&make=%s", color, make),
                        AutomobileList.class);
        // THEN | ASSERT
        // THEN | ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isEmpty()).isFalse();
        for (Automobile auto : response.getBody().getAutomobiles()) {
            System.out.println(auto);
        }
    }

    @Test
    void addAutomobile_returnsNewAutomobileDetails() {
        // GIVEN | ARRANGE
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "BLUE", "Unknown", "ABC123XYZ");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Automobile> request = new HttpEntity<>(automobile, headers);

        // WHEN | ACT
        ResponseEntity<Automobile> response = testRestTemplate.postForEntity(
                "/api/autos",
                request,
                Automobile.class);

        // THEN | ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getVin()).isEqualTo(automobile.getVin());
    }

    @Test
    void updateAutomobile_withObject_returnAutomobile() {
        // GIVEN | ARRANGE
        Automobile automobileToUpdate = automobileTestList.get(random.nextInt(50));
        automobileToUpdate.setColor("RED");

        System.out.println(automobileToUpdate);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Automobile> request = new HttpEntity<>(automobileToUpdate, headers);

        // WHEN | ACT
        this.patchRestTemplate = testRestTemplate.getRestTemplate();
        this.patchRestTemplate.setRequestFactory(
                new HttpComponentsClientHttpRequestFactory(
                        HttpClientBuilder.create().build()
                ));

        ResponseEntity<Automobile> response = patchRestTemplate.exchange(
                "/api/autos/" + automobileToUpdate.getVin(),
                HttpMethod.PATCH, request, Automobile.class);

        // THEN | ASSERT
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
