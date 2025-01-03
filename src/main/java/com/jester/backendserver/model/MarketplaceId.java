package com.jester.backendserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MarketplaceId implements Serializable {

    @Column(name = "seller_id", nullable = false)
    private Integer sellerId;

    @Column(name = "persona_id", nullable = false)
    private Integer personaId;

    // Getters and Setters
    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Integer personaId) {
        this.personaId = personaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketplaceId that = (MarketplaceId) o;
        return Objects.equals(sellerId, that.sellerId) && Objects.equals(personaId, that.personaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellerId, personaId);
    }
}
