package pw.ersms.accounts;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "\"Accounts\"")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @SequenceGenerator(
            name = "Account_id_seq",
            sequenceName = "Account_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Account_id_seq"
    )
    private Integer id;

    @Column(name = "email", length = Integer.MAX_VALUE)
    private String email;

    @Column(name = "first_name", nullable = false, length = Integer.MAX_VALUE)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = Integer.MAX_VALUE)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "fire_department_id", nullable = false)
    private Integer fireDepartmentId;

    @Column(name = "fire_department_name", nullable = false, length = Integer.MAX_VALUE)
    private String fireDepartmentName;

    @Column(name = "shift", nullable = false)
    private Integer shift;

    @Column(name = "\"position\"", nullable = false, length = Integer.MAX_VALUE)
    private String position;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "address_line_1", nullable = false, length = Integer.MAX_VALUE)
    private String addressLine1;

    @Column(name = "address_line_2", length = Integer.MAX_VALUE)
    private String addressLine2;

    @Column(name = "city", nullable = false, length = Integer.MAX_VALUE)
    private String city;

    @Column(name = "country", nullable = false, length = Integer.MAX_VALUE)
    private String country;

    @Column(name = "profile_picture", length = Integer.MAX_VALUE)
    private String profilePicture;

    @Column(name = "is_activated", nullable = false)
    private Boolean isActivated = false;

    @Column(name = "activation_code", nullable = false, length = Integer.MAX_VALUE)
    private String activationCode;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}