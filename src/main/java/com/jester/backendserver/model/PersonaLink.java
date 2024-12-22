package com.jester.backendserver.model;

import jakarta.persistence.*;

@Entity
@Table(name = "persona_links")
public class PersonaLink {

    @EmbeddedId
    private PersonaLinkId id;

    @ManyToOne
    @MapsId("personaId")
    @JoinColumn(name = "persona_id", insertable = false, updatable = false)
    private Persona persona;

    // Removed direct column mapping for 'link' to avoid duplication
    public PersonaLinkId getId() {
        return id;
    }

    public void setId(PersonaLinkId id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getLink() {
        return id.getLink(); // Access link via the composite key
    }

    public void setLink(String link) {
        this.id.setLink(link); // Set link via the composite key
    }
}

