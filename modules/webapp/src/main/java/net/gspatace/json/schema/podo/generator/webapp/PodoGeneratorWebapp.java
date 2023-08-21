package net.gspatace.json.schema.podo.generator.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Angular webapp over JSON Schema PODO Generator REST API
 *
 * @author George Spătăcean
 */
@SpringBootApplication
public class PodoGeneratorWebapp {
    public static void main(String[] args) {
        SpringApplication.run(PodoGeneratorWebapp.class, args);
    }
}
