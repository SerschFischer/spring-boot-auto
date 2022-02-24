package de.galvanize.autos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutomobilesController.class)
public class AutomobilesControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AutomobilesService automobilesService;

    ObjectMapper mapper = new ObjectMapper();

    // GET: /api/autos
    // GET: /api/autos returns list of all autos in db
    @Test
    void getAuto_noParams_exists_returnAutosList() throws Exception {
        // GIVEN | ARRANGE
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(1999, "Ford", "Mustang", "Red", "Nobody", "123123"));
        }
        when(automobilesService.getAutos()).thenReturn(new AutomobileList(automobiles));
        // WHEN | ACT
        mockMvc.perform(get("/api/autos"))
                .andDo(print())
                // THEN | ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    // GET: /api/autos returns 204 no autos found
    @Test
    void getAutos_noParams_none_returnsNoContent() throws Exception {
        // GIVEN | ARRANGE
        when(automobilesService.getAutos()).thenReturn(new AutomobileList());
        // WHEN | ACT
        mockMvc.perform(get("/api/autos"))
                .andDo(print())
                // THEN | ASSERT
                .andExpect(status().isNoContent());
    }

    // GET: /api/autos?color=RED returns red cars
    // GET: /api/autos?make=Ford returns fords
    // GET: /api/autos?make=Ford&color=GREEN
    @Test
    void getAutos_searchParams_returnsAutomobileList() throws Exception {
        // GIVEN | ARRANGE
        List<Automobile> automobileList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobileList.add(new Automobile(1999, "Ford", "Mustang", "RED", "Nobody", "ABBCC" + i));
        }
        when(automobilesService.getAutos(anyString(), anyString())).thenReturn(new AutomobileList(automobileList));
        // WHEN | ACT
        mockMvc.perform(get("/api/autos?color=RED&make=Ford"))
                // THEN | ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }


    // POST: /api/autos
// POST: /api/autos/{vin} returns created automobile
    @Test
    void addAuto_valid_returnAuto() throws Exception {
        // GIVEN | ARRANGE
        Automobile automobile = new Automobile(1999, "Ford", "Mustang", "RED", "Nobody", "ACC");
        when(automobilesService.addAuto(any(Automobile.class)))
                .thenReturn(automobile);
        // WHEN | ACT
        mockMvc.perform(post("/api/autos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(automobile))
                )
                .andDo(print())
                // THEN | ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("make").value("Ford"));
    }

    // POST: /api/autos/{vin} returns error message due to bad request (400)
    @Test
    void addAuto_badRequest_return400() throws Exception {
        // GIVEN | ARRANGE
        when(automobilesService.addAuto(any(Automobile.class)))
                .thenThrow(InvalidAutomobileException.class);
        String json = "{\"year\":1999,\"make\":\"Ford\",\"model\":\"Mustang\",\"color\":\"RED\",\"owner\":\"Nobody\",\"vin\":\"ACC\"}";
        // WHEN | ACT
        mockMvc.perform(post("/api/autos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                // THEN | ASSERT
                .andExpect(status().isBadRequest());
    }

// GET: /api/autos/{vin}
// GET: /api/autos/{vin} returns the requested automobile
// GET: /api/autos/{vin} returns not content auto not found

// PATCH: /api/autos{vin}
// PATCH: /api/autos/{vin} returns patched automobile
// PATCH: /api/autos/{vin} returns no content auto not found
// PATCH: /api/autos/{vin} returns 400 bad request (no payload, no changes, or already done)

// DELETE: /api/autos/{vin}
// DELETE: /api/autos/{vin} returns 202, delete request accepted
// DELETE: /api/autos/{vin} returns 204, vehicle not found

}
