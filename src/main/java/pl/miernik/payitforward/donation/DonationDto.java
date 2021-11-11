package pl.miernik.payitforward.donation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.miernik.payitforward.category.Category;
import pl.miernik.payitforward.institution.Institution;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DonationDto {

    private Long id;

    @NotNull(message = "Field cannot be blank")
    @Min(value = 1, message = "Value higher than 0.")
    private Integer quantity;

    @NotEmpty(message = "Choose minimum 1.")
    private Set<Category> categories;

    private Institution institution;

    @NotBlank(message = "Field cannot be blank")
    private String city;

    @NotBlank(message = "Field cannot be blank")
    private String street;

    @Pattern(regexp = "(\\d){2}-(\\d){3}", message = "Invalid zip code.")
    @NotBlank(message = "Field cannot be blank")
    private String zipCode;

    @NotBlank(message = "Field cannot be blank")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$", message = "Invalid phone number.")
    private String phoneNumber;

    @NotNull(message = "Date cannot be null.")
    @Future(message = "Date must be in the future.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickUpDate;

    @NotNull(message = "Time cannot be null.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime pickUpTime;

    private String pickUpComment;

    private LocalDate actualPickUpDate;
}