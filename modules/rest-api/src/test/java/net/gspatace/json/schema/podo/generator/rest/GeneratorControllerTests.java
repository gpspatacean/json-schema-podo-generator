package net.gspatace.json.schema.podo.generator.rest;

import net.gspatace.json.schema.podo.generator.core.services.GeneratorDescription;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorInstantiationException;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorNotFoundException;
import net.gspatace.json.schema.podo.generator.core.services.OptionDescription;
import net.gspatace.json.schema.podo.generator.rest.controllers.GeneratorController;
import net.gspatace.json.schema.podo.generator.rest.services.GeneratorsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GeneratorController.class)
class GeneratorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeneratorsService service;

    @Test
    void getAvailableGenerators() throws Exception {
        final GeneratorDescription generator = GeneratorDescription.builder()
                .name("test-generator")
                .description("test-generator description")
                .build();

        final List<GeneratorDescription> mockedResult = Collections.singletonList(generator);
        when(service.listGenerators()).thenReturn(mockedResult);
        mockMvc.perform(get("/generators"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(generator.getName())));
    }

    @Test
    void getGeneratorDescription() throws Exception {
        final String dummyGenerator = "dummy-target-generator";
        final GeneratorDescription mockedGenerator = GeneratorDescription.builder()
                .name(dummyGenerator)
                .description("Dummy generator description")
                .build();

        when(service.listGeneratorDescription(dummyGenerator)).thenReturn(mockedGenerator);
        mockMvc.perform(get("/generators/" + dummyGenerator))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is(mockedGenerator.getName())));
    }

    @Test
    void getSpecificGeneratorOptions() throws Exception {
        final OptionDescription optionDescription = OptionDescription
                .builder()
                .name("--option-name")
                .description("--option-name description")
                .defaultValue("--option-name defaultValue")
                .build();

        final Set<OptionDescription> mockedResult = Collections.singleton(optionDescription);
        when(service.listGeneratorProperties("test-generator")).thenReturn(mockedResult);
        mockMvc.perform(get("/generators/test-generator/options"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(optionDescription.getName())));
    }

    @Test
    void getDummyResourceUsingJsonPayload() throws Exception {
        final byte[] resourceContents = new byte[]{'a', 'b', 'c'};
        final InputStreamResource mockResource = new InputStreamResource(new ByteArrayInputStream(resourceContents));
        when(service.buildCodeArchive(anyString(), anyString(), anyString())).thenReturn(mockResource);

        mockMvc.perform(
                        post("/generators/test-generator")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"dummy\":\"schema\"}")
                                .queryParam("options", "[]")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(resourceContents));
    }

    @Test
    void getDummyResourceUsingMultiPartFormPayload() throws Exception {
        final byte[] resourceContents = new byte[]{'a', 'b', 'c'};
        final InputStreamResource mockResource = new InputStreamResource(new ByteArrayInputStream(resourceContents));
        when(service.buildCodeArchive(anyString(), anyString(), anyString())).thenReturn(mockResource);

        final MockMultipartFile mockFile =
                new MockMultipartFile("schemaFile", "schemaFile.txt",MediaType.APPLICATION_JSON_VALUE, "{\"dummy\":\"schema\"}".getBytes(StandardCharsets.UTF_8));
        mockMvc.perform(
                        multipart("/generators/test-generator")
                                .file("schema", mockFile.getBytes())
                                .param("options", "[]")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(resourceContents));
    }

    @Test
    void testGeneratorNotFoundException() throws Exception {
        when(service.listGeneratorDescription("nonExistent"))
                .thenThrow(GeneratorNotFoundException.class);
        mockMvc.perform(get("/generators/nonExistent" ))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("title", is("Generator not found.")));
    }

    @Test
    void testGeneratorInstantiationException() throws Exception {
        when(service.buildCodeArchive(anyString(), anyString(), anyString()))
                .thenThrow(GeneratorInstantiationException.class);
        mockMvc.perform(
                        post("/generators/test-generator")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"dummy\":\"schema\"}")
                                .queryParam("options", "[]")
                )
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("title", is("Generator Instantiation error.")));
    }
}
