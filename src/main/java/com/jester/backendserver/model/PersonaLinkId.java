package com.jester.backendserver.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PersonaLinkId implements Serializable {
    @Column(name = "persona_id")
    private Integer personaId;

    @Column(name = "link")
    private String link;

    // Getters, Setters, equals, and hashCode methods

    public Integer getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Integer personaId) {
        this.personaId = personaId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonaLinkId that = (PersonaLinkId) o;
        return Objects.equals(personaId, that.personaId) &&
                Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personaId, link);
    }
}

