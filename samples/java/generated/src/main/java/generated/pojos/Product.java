package generated.pojos;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    /**
     * The unique identifier for a product
     */
    private int productId;

    /**
     * Name of the product
     */
    private String productName;

    /**
     * The price of the product
     */
    private double price;

    /**
     * Tags for the product
     */
    private ArrayList<String> tags;

    /**
     * Reviews of the product
     */
    private ArrayList<Review> reviews;

    /**
     * 
     */
    private Dimensions dimensions;
}