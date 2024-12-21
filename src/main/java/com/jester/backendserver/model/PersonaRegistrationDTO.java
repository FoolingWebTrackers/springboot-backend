package com.jester.backendserver.model;

// (uname TEXT, pname TEXT, pDesc TEXT)
public class PersonaRegistrationDTO {
    private String userName;
    private String personaName;
    private String description;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonaName() {
        return personaName;
    }

    public void setPersonaName(String personaName) {
        this.personaName = personaName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
