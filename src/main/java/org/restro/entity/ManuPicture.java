package org.restro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "manu_picture")
public class ManuPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String picName;
    @ManyToOne
    public Manu manu;
}
