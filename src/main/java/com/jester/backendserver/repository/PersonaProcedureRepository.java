package com.jester.backendserver.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;

@Repository
public class PersonaProcedureRepository {
    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    // Procedure
    public void createPersona(String userName, String personaName, String description) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("create_persona");
        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);

        query.setParameter(1, userName);
        query.setParameter(2, personaName);
        query.setParameter(3, description);

        query.execute();
    }

}
