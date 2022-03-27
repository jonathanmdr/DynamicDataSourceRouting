package br.com.multidatasources.controller;

import br.com.multidatasources.controller.dto.BillionaireInputDto;
import br.com.multidatasources.controller.dto.BillionaireOutputDto;
import br.com.multidatasources.controller.exceptionhandler.ApiError;
import br.com.multidatasources.controller.mapper.BillionaireMapper;
import br.com.multidatasources.model.Billionaire;
import br.com.multidatasources.service.BillionaireService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BillionaireController.class)
class BillionaireControllerTest {

    private static ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillionaireService billionaireService;

    @MockBean
    private BillionaireMapper billionaireMapper;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void givenAEmptyDatabase_whenGet_shouldBeReturnHttpStatus200AndEmptyList() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/billionaires"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    void givenAEmptyRequestPayload_whenPost_shouldBeReturnHttpStatus400AndResponseBodyFieldsAreInvalid() throws Exception {
        String response = """
            {
              "status": 400,
              "timestamp": "2022-03-27T14:45:34.528975-03:00",
              "detail": "One or more fields are invalid. fill in correctly and try again.",
              "userMessage": "One or more fields are invalid. fill in correctly and try again.",
              "fields": [
                {
                  "name": "career",
                  "userMessage": "Career is mandatory"
                },
                {
                  "name": "lastName",
                  "userMessage": "Last name is mandatory"
                },
                {
                  "name": "firstName",
                  "userMessage": "First name is mandatory"
                }
              ]
            }
            """;

        MvcResult mvcResult = this.mockMvc.perform(
                post("/billionaires")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
            )
            .andReturn();

        ApiError expected = objectMapper.readValue(response, ApiError.class);
        ApiError actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ApiError.class);
        expected.setTimestamp(actual.getTimestamp());

        assertThat(actual)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expected);
    }

    @Test
    void givenAValidRequestPayload_whenPost_shouldBeReturnHttpStatus201AndResponseBodyResourceCreated() throws Exception {
        String request = """
            {
               "firstName": "John",
               "lastName": "Doe",
               "career": "Software Engineer"
            }
            """;

        String response = """
            {
               "id": 1,
               "firstName": "John",
               "lastName": "Doe",
               "career": "Software Engineer"
            }
            """;

        Billionaire expected = objectMapper.readValue(response, Billionaire.class);
        BillionaireOutputDto out = objectMapper.readValue(response, BillionaireOutputDto.class);
        when(billionaireMapper.toModel(any(BillionaireInputDto.class))).thenReturn(expected);
        when(billionaireService.save(any(Billionaire.class))).thenReturn(expected);
        when(billionaireMapper.toDto(any(Billionaire.class))).thenReturn(out);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/billionaires")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
            )
            .andReturn();

        Billionaire actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Billionaire.class);

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(mvcResult.getResponse().getHeader("Location")).isEqualTo("http://localhost/billionaires/1");
    }

}
