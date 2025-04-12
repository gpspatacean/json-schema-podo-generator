package com.spatacean.json.schema.podo.generator.rest;

import com.spatacean.json.schema.podo.generator.core.services.*;
import com.spatacean.json.schema.podo.generator.rest.adapter.CustomOptionsTransformer;
import com.spatacean.json.schema.podo.generator.rest.controllers.GeneratorController;
import com.spatacean.json.schema.podo.generator.rest.services.GeneratorsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GeneratorController.class)
class GeneratorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GeneratorsService service;

    @MockitoBean
    private CustomOptionsTransformer optionsTransformer;

    @Test
    void getAvailableGenerators() throws Exception {
        final GeneratorDescription generator = new GeneratorDescription("test-generator", "test-generator description");

        final List<GeneratorDescription> mockedResult = Collections.singletonList(generator);
        when(service.listGenerators()).thenReturn(mockedResult);
        mockMvc.perform(get("/generators"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(generator.name())));
    }

    @Test
    void getGeneratorDescription() throws Exception {
        final String dummyGenerator = "dummy-target-generator";
        final GeneratorDescription mockedGenerator = new GeneratorDescription(dummyGenerator, "Dummy generator description");

        when(service.listGeneratorDescription(dummyGenerator)).thenReturn(mockedGenerator);
        mockMvc.perform(get("/generators/" + dummyGenerator))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is(mockedGenerator.name())));
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
        when(service.buildCodeArchive(anyString(), any(String[].class), anyString()))
                .thenReturn(mockResource);
        when(optionsTransformer.getCommandOptions(anyString(), anyMap()))
                .thenReturn(new String[0]);
        mockMvc.perform(
                        post("/generators/test-generator")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"dummy\":\"schema\"}")
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
        when(service.buildCodeArchive(anyString(), any(String[].class), anyString()))
                .thenReturn(mockResource);
        when(optionsTransformer.getCommandOptions(anyString(), anyMap()))
                .thenReturn(new String[0]);

        final MockMultipartFile mockFile =
                new MockMultipartFile("schemaFile", "schemaFile.txt", MediaType.APPLICATION_JSON_VALUE, "{\"dummy\":\"schema\"}".getBytes(StandardCharsets.UTF_8));
        mockMvc.perform(
                        multipart("/generators/test-generator")
                                .file("schema", mockFile.getBytes())
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
        mockMvc.perform(get("/generators/nonExistent"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("title", is("Generator not found.")));
    }

    @Test
    void testGeneratorInstantiationException() throws Exception {
        when(service.buildCodeArchive(anyString(), any(String[].class), anyString()))
                .thenThrow(GeneratorInstantiationException.class);
        when(optionsTransformer.getCommandOptions(anyString(), anyMap()))
                .thenReturn(new String[0]);
        mockMvc.perform(
                        post("/generators/test-generator")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"dummy\":\"schema\"}")
                )
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("title", is("Generator Instantiation error.")));
    }

    @Test
    void testCustomOptionsInstantiationException() throws Exception {
        when(service.listGeneratorProperties(anyString()))
                .thenThrow(CustomOptionsInstantiationException.class);
        mockMvc.perform(get("/generators/test-generator/options"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("title", is("Custom Properties Object Instantiation error.")));
    }
}
