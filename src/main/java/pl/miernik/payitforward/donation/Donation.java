package pl.miernik.payitforward.donation;

import lombok.*;
import pl.miernik.payitforward.category.Category;
import pl.miernik.payitforward.institution.Institution;
import pl.miernik.payitforward.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "donations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToMany
    private Set<Category> categories;

    @ManyToOne
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Institution institution;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false, name = "pick_up_date")
    private LocalDate pickUpDate;

    @Column(nullable = false, name = "pick_up_time")
    private LocalTime pickUpTime;

    @Column(name = "pick_up_comment")
    private String pickUpComment;

    @Column(name = "actual_pick_up_date")
    private LocalDate actualPickUpDate;

    @Column (name = "is_picked_up")
    private Boolean isPickedUp;

    @Column(nullable = false)
    private LocalDate created;

    private LocalDate updated;


    @PrePersist
    private void prePersist() {
        isPickedUp = false;
        created = LocalDate.now();
    }

    @PreUpdate
    private void preUpdate() {
        created = LocalDate.now();
    }
}
