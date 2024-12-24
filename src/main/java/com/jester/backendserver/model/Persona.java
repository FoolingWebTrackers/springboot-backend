package com.jester.backendserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

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

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image=" + (image != null ? image.length + " bytes" : "null") +
                ", users=" + (users != null ? users.size() + " users" : "null") +
                ", links=" + (links != null ? links.size() + " links" : "null") +
                '}';
    }

}
