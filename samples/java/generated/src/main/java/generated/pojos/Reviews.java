package generated.pojos;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reviews {

    /**
     * Rating from 1 to 10
     */
    private int rate;

    /**
     * User that gave this review
     */
    private String user;
}