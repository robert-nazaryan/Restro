package org.restro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String name;
    public double price;
    public String description;
    @ManyToOne
    public Category category;

    @OneToMany(mappedBy = "menu",cascade = CascadeType.ALL)
    private List<MenuPicture> menuPictures;
}
