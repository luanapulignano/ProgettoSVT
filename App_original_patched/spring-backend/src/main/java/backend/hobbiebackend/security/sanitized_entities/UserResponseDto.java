package backend.hobbiebackend.security.sanitized_entities;

import backend.hobbiebackend.model.entities.enums.GenderEnum;

public class UserResponseDto {
    private String fullName;
    private GenderEnum gender;
    private String email;
    private String username;
    private Long id;

    public UserResponseDto() {
    }

    public UserResponseDto(String fullName, GenderEnum gender, String email, String username,Long id) {
        this.fullName = fullName;
        this.gender = gender;
        this.email = email;
        this.username = username;
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
