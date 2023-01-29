package net.gspatace.json.schema.podo.generator.rest;

import net.gspatace.json.schema.podo.generator.rest.controllers.GeneratorController;

import net.gspatace.json.schema.podo.generator.rest.services.GeneratorsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GeneratorController.class)
class GeneratorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeneratorsService service;

    @Test
    void getAvailableGenerators() throws Exception {
        mockMvc.perform(get("/generators")).andDo(print()).andExpect(status().isOk());
    }
}
