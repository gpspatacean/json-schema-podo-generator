package net.gspatace.json.schema.podo.generator.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring REST API over
 * json-schema-podo-generator logic
 *
 * @author George Spătăcean
 */
@SpringBootApplication
public class PodoGeneratorRestApi
{
    public static void main( String[] args )
    {
        SpringApplication.run(PodoGeneratorRestApi.class, args);
    }
}
