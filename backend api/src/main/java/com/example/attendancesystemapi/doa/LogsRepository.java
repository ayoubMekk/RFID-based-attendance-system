package com.example.attendancesystemapi.doa;

import com.example.attendancesystemapi.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface LogsRepository extends JpaRepository<Log,Long> {

}
