package org.restro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "menu_picture")
public class MenuPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String picName;
    @ManyToOne
    public Menu menu;
}
