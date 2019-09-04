package com.example.attendancesystemapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log {

    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private Date date;

    private String etat;

    @ManyToOne
    Utilisateur utilisateur;




}
