package org.restro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String name;
    public String surname;
    public int phoneNumber;
    public String email;
    public String password;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    public UserType userType;
    public String token;
    public boolean active;
}
