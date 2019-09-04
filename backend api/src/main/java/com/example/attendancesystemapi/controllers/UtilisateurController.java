package com.example.attendancesystemapi.controllers;

import com.example.attendancesystemapi.doa.UtilisateurRepository;
import com.example.attendancesystemapi.entities.Utilisateur;
import com.pusher.rest.Pusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@RestController
public class UtilisateurController {

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @PostMapping("/write")
    public Utilisateur enregistrerUtilisateur(@RequestBody Map<String,String> payload) {

        Utilisateur utilisateur = null;
        String idCart = payload.get("idCart");
        String nom = payload.get("nom");
        String email = payload.get("email");

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date=null;
        try {
            date =dateFormat.parse(payload.get("bday"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Utilisateur u = utilisateurRepository.findUtilisateurByIdcart(idCart);

        if (u == null) {
             utilisateur = new Utilisateur(null,idCart,nom,email,date, new Date(),null);
             utilisateurRepository.save(utilisateur);
        }

        Pusher pusher = new Pusher("762880", "84bee67aad46ed497369", "5017a5ee0387085255ae");
        pusher.setCluster("eu");
        pusher.setEncrypted(true);

        pusher.trigger("my-channel", "add-user", Collections.singletonMap("data",payload.get("idCart")));
        return  utilisateur;
    }

}
