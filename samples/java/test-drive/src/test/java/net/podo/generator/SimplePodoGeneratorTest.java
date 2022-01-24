package net.podo.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import generated.pojos.Product;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class SimplePodoGeneratorTest
{
    /**
     * Small basic test
     */
    @Test
    public void shouldAnswerWithTrue() throws JsonProcessingException
    {
        final String jsonInput = "{\"productId\":123,\"productName\":\"prodName\",\"price\":123.0,\"tags\":[\"first tag\",\"second tag\",\"third tag\"],\"reviews\":[{\"rate\":10,\"user\":\"gigi\"},{\"rate\":8,\"user\":\"duru\"}],\"dimensions\":{\"length\":200.0,\"width\":150.0,\"height\":100.0,\"subDim\":{\"weight\":200.0,\"density\":100.0}}}";
        final ObjectMapper objectMapper = new ObjectMapper();
        final Product productWithJackson = objectMapper.readValue(jsonInput, Product.class);
        final String objectToString = objectMapper.writeValueAsString(productWithJackson);
        final Gson gson = new Gson();
        final Product productWithGson = gson.fromJson(objectToString, Product.class);
        assertTrue("Objects must be equal:", productWithJackson.equals(productWithGson));
    }
}
