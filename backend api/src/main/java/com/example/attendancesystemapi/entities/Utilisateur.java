package com.example.attendancesystemapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String idcart;
    private String name;
    private String email;
    private Date bday;
    private Date dateRegistry;

    @JsonIgnore
    @OneToMany(mappedBy = "utilisateur" , cascade = CascadeType.ALL)
    List<Log> logs;
}
