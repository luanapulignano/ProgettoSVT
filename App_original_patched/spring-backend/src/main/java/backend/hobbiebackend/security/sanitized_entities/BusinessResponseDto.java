package backend.hobbiebackend.security.sanitized_entities;

public class BusinessResponseDto {
    private String businessName;
    private String address;
    private String username;
    private String email;

    public BusinessResponseDto() {
    }

    public BusinessResponseDto(String businessName, String address, String username, String email) {
        this.businessName = businessName;
        this.address = address;
        this.username = username;
        this.email = email;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
