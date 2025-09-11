package org.example.ch3schedulerdevelopprojectexplanation.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.ch3schedulerdevelopprojectexplanation.common.entity.BaseEntity;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    @Column(unique = true)
    private String email;

    private String password;

    public void update(
            String userName,
            String email,
            String password
    ) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}
