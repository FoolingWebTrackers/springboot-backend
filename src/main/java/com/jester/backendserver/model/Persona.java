package com.jester.backendserver.model;

import jakarta.persistence.*;

import java.util.List;

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
    private byte[] image;

    // Relationship to User through user_personas
    @ManyToMany
    @JoinTable(
            name = "user_personas",
            joinColumns = @JoinColumn(name = "persona_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    // Relationship to PersonaLinks
    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<PersonaLink> getLinks() {
        return links;
    }

    public void setLinks(List<PersonaLink> links) {
        this.links = links;
    }

}
