package com.jester.backendserver.repository;


import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;

@Repository
public class UserProcedureRepository {

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    // Procedure
    public void createUser(String username, String password) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("create_user");
        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);

        query.setParameter(1, username);
        query.setParameter(2, password);
        query.setParameter(3, Timestamp.from(Instant.now()).toString());

        query.execute();
    }

    // Function
    public Boolean authenticateUser(String username, String password) {
        Query query = entityManager.createNativeQuery(
                "SELECT authenticate_user(:uname, :pwd)", Boolean.class);
        query.setParameter("uname", username);
        query.setParameter("pwd", password);

        return (Boolean) query.getSingleResult();
    }
}