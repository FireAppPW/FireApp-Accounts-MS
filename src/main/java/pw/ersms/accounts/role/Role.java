package pw.ersms.accounts.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pw.ersms.accounts.account.Account;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Roles\"")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    @SequenceGenerator(
            name = "Role_id_seq",
            sequenceName = "Role_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Role_id_seq"
    )
    private Integer id;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<Account> accounts = new LinkedHashSet<>();

}