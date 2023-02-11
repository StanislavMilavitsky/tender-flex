package pl.exadel.milavitsky.tenderflex.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Company {

    @Positive(message = "Should be positive")
    private Long id;

    @Size(min = 2, max = 50, message = "Official name should be between 2 and 50 characters")
    private String officialName;

    @Size(min = 2, max = 20, message = "National registration number should be between 2 and 20 characters")
    private String nationalRegistrationNumber;

    @Size(min = 2, max = 30, message = "Country should be between 2 and 30 characters") //todo Enum?
    private String country;

    @Size(min = 2, max = 50, message = "City should be between 2 and 30 characters")
    private String city;

}
