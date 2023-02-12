package pl.exadel.milavitsky.tenderflex.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.exadel.milavitsky.tenderflex.entity.enums.Country;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public abstract class Company implements Serializable {

    @Positive(message = "Should be positive")
    protected Long idTender;

    @NotEmpty(message = "Official name should not be empty")
    @Size(min = 2, max = 50, message = "Official name should be between 2 and 50 characters")
    protected String officialName;

    @NotEmpty(message = "National registration number should not be empty")
    @Size(min = 2, max = 20, message = "National registration number be between 2 and 50 characters")
    protected String nationalRegistrationNumber;

    protected Country country;

    @NotEmpty(message = "City should not be empty")
    @Size(min = 2, max = 50, message = "City should be between 2 and 50 characters")
    protected String city;

    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
    @NotEmpty(message = "Name should not be empty")
    protected String name;

    @Size(min = 2, max = 50, message = "Surname should be between 2 and 50 characters")
    @NotEmpty(message = "Surname should not be empty")
    protected String surname;

    @Size(min = 2, max = 20, message = "Phone number should be between 2 and 20 characters")
    @NotEmpty(message = "Phone number should not be empty")
    protected String phoneNumber;
}
