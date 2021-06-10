package kafkafirst.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



/**
 * Standard API response.
 * 
 * @param http_code Integer
 * @param status String
 * @param message String
 * 
 * @author Fabrizio Sosa
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {
	
	private Integer http_code;
	private String status;
	private String message;
	
	
}
