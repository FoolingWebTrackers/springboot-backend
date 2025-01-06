package com.jester.backendserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "persona")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name="name", unique = true, nullable = false)
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="image")
    @JsonIgnore
    private byte[] image;

    // Relationship to User through user_personas
    @ManyToMany
    @JoinTable(
            name = "user_personas",
            joinColumns = @JoinColumn(name = "persona_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private List<User> users;

    // Relationship to PersonaLinks
    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PersonaLink> links;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @JsonProperty("imageBase64")
    public String getImageBase64() {
        if (image == null || image.length == 0) {
            return null;
        }
        return Base64.getEncoder().encodeToString(image);
    }

    @JsonProperty("imageBase64")
    public void setImageBase64(String base64Image) {
        if (base64Image != null && !base64Image.isEmpty()) {
            this.image = Base64.getDecoder().decode(base64Image);
        } else {
            this.image = null;
        }
    }

    @JsonProperty
    public String getOwnerName() {
        if (owner == null) {
            return null;
        }
        else {
            return owner.getUsername();
        }
    }

    @JsonProperty("users")
    public List<String> getUserStrings() {
        if (users == null) {
            return Collections.emptyList();
        }
        return users.stream().map(User::getUsername).collect(Collectors.toList());
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @JsonProperty("links")
    public List<String> getLinkStrings() {
        if (links == null) {
            return Collections.emptyList();
        }
        return links.stream()
                .map(PersonaLink::getLink) // Extract the link string
                .collect(Collectors.toList());
    }

    public List<PersonaLink> getLinks() {
        return links;
    }

    public void setLinks(List<PersonaLink> links) {
        this.links = links;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image=" + (image != null ? image.length + " bytes" : "null") +
                ", owner=" + (owner != null ? owner.getUsername() : "null") +
                ", users=" + (users != null ? users.size() + " users" : "null") +
                ", links=" + (links != null ? links.size() + " links" : "null") +
                '}';
    }
}
