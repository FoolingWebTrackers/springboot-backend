package com.jester.backendserver.repository;

import com.jester.backendserver.model.Persona;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.sql.Timestamp;
import java.time.Instant;

@Repository
public class PersonaProcedureRepository {
    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    // Function
    public Integer createPersona(String userName, String personaName, String description) {
        Query query = entityManager.createNativeQuery("SELECT create_persona(:uname, :pname, :pDesc)");
        query.setParameter("uname", userName);
        query.setParameter("pname", personaName);
        query.setParameter("pDesc", description);

        // Cast the result to Integer
        return (Integer) query.getSingleResult();
    }


    // Procedure for inserting links into a persona
    public void insertPersonaLinks(Integer pid, List<String> links) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("insert_persona_links");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, String[].class, ParameterMode.IN);

        query.setParameter(1, pid);
        query.setParameter(2, links.toArray(new String[0])); // Convert list to array
        query.execute();
    }

}
