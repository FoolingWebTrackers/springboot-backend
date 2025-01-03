package com.jester.backendserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "marketplace")
public class Marketplace {

    @EmbeddedId
    @JsonIgnore
    private MarketplaceId id;

    @ManyToOne
    @MapsId("sellerId")
    @JoinColumn(name = "seller_id", insertable = false, updatable = false)
    @JsonIgnore
    private User seller;

    @ManyToOne
    @MapsId("personaId")
    @JoinColumn(name = "persona_id", insertable = false, updatable = false)
    @JsonIgnore
    private Persona persona;

    @Column(name = "user_num")
    private Integer userNum;

    // Getters and Setters
    public MarketplaceId getId() {
        return id;
    }

    public void setId(MarketplaceId id) {
        this.id = id;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }
}
