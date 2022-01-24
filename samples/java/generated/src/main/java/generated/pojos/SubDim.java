package generated.pojos;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubDim {

    /**
     * The weight of the product
     */
    private double weight;

    /**
     * The density of the product
     */
    private double density;
}