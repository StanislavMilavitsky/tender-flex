package pl.exadel.milavitsky.tenderflex.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class CompanyDto implements Serializable {

    @NotEmpty(message = "Official name should not be empty")
    @Size(min = 2, max = 50, message = "Official name should be between 2 and 50 characters")
    private String officialName;

    @NotEmpty(message = "National registration number should not be empty")
    @Size(min = 2, max = 20, message = "National registration number be between 2 and 50 characters")
    private String nationalRegistrationNumber;

    private String country;

    @NotEmpty(message = "City should not be empty")
    @Size(min = 2, max = 50, message = "City should be between 2 and 50 characters")
    private String city;

    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Size(min = 2, max = 50, message = "Surname should be between 2 and 50 characters")
    @NotEmpty(message = "Surname should not be empty")
    private String surname;

    @Size(min = 2, max = 20, message = "Phone number should be between 2 and 20 characters")
    @NotEmpty(message = "Phone number should not be empty")
    private String phoneNumber;
}
