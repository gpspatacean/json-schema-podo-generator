package generated.pojos;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dimensions {

    /**
     * 
     */
    private double length;

    /**
     * 
     */
    private double width;

    /**
     * 
     */
    private double height;

    /**
     * 
     */
    private SubDim subDim;
}