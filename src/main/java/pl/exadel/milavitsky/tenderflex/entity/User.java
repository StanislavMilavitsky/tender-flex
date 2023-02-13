package pl.exadel.milavitsky.tenderflex.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.exadel.milavitsky.tenderflex.entity.enums.Role;
import pl.exadel.milavitsky.tenderflex.validation.CreateAction;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User implements Serializable {

    @Positive(message = "Should be positive")
    private Long id;

    @Email
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 30, message = "Username should be between 2 and 30 characters")
    private String username;

    @NotBlank(groups = CreateAction.class)
    private String password;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfRegistration;

    private Role role;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate lastLoginDate;
}