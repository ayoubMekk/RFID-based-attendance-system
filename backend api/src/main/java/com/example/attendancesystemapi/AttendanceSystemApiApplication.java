package com.example.attendancesystemapi;

import com.example.attendancesystemapi.doa.LogsRepository;
import com.example.attendancesystemapi.entities.Log;
import com.example.attendancesystemapi.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class AttendanceSystemApiApplication  implements CommandLineRunner {

    @Autowired
    LogsRepository logsRepository;

    @Autowired
    private RepositoryRestConfiguration repositoryRestConfiguration;

    public static void main(String[] args) {
        SpringApplication.run(AttendanceSystemApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        repositoryRestConfiguration.exposeIdsFor(Utilisateur.class, Log.class);

    }
}
