package com.example.attendancesystemapi.controllers;


import com.example.attendancesystemapi.doa.LogsRepository;
import com.example.attendancesystemapi.doa.UtilisateurRepository;
import com.example.attendancesystemapi.entities.Log;
import com.example.attendancesystemapi.entities.Utilisateur;
import com.pusher.rest.Pusher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class LogsController {

    @Autowired
    LogsRepository logsRepository;

    @Autowired
    UtilisateurRepository utilisateurRepository;


    @PostMapping("/read")
    public Log check(@RequestBody Map<String,String> payload) {
        Log log = null;
        Utilisateur utilisateur = utilisateurRepository.findUtilisateurByIdcart(payload.get("idCart"));

        Pusher pusher = new Pusher("762880", "84bee67aad46ed497369", "5017a5ee0387085255ae");
        pusher.setCluster("eu");
        pusher.setEncrypted(true);

        if (utilisateur!=null) {
            List<Log> logs = utilisateur.getLogs();

            // si le nombre est pair   => utilisateur veut entrer
            if (logs.size() % 2 ==0 ) {
                log =  new Log(null,new Date(),"IN",utilisateur);
            } else { //  utilisateur veut sortir
                log =  new Log(null,new Date(),"OUT",utilisateur);
            }

        } else { // utilisateur inconnu  veur entrer !
            log =  new Log(null,new Date(),"Banned",null);

        }
        logsRepository.save(log);
        pusher.trigger("my-channel", "my-event", Collections.singletonMap("data",payload.get("idCart")));
        System.out.println("================" +pusher.toString());
        return  log ;
    }


    @GetMapping(value = "all-logs")
    public List<Log2> getAllLogs(){

        List<Log2> log2List =  new ArrayList<>();

        List<Log> logs =  logsRepository.findAll();

        if ( logs!= null){

            for(Log l:logs){

                if (l.getUtilisateur()!= null){

                    log2List.add(new Log2(l.getId(), l.getUtilisateur().getName(),l.getDate(),l.getEtat()));
                } else {
                    log2List.add(new Log2(l.getId(),"inconnu",l.getDate(),l.getEtat()));
                }
            }
        }
        return log2List;

    }



}

@Data @AllArgsConstructor @NoArgsConstructor
class Log2 {
    long id;
    String name;
    Date date;
    String status;


}