package backend.hobbiebackend.security.sanitized_entities;

import java.math.BigDecimal;

public class HobbyResponseDto {
    private int id;
    private String name;
    private String slogan;
    private String intro;
    private String description;
    private BigDecimal price;
    private String profileImgUrl;
    private String galleryImgUrl1;
    private String galleryImgUrl2;
    private String galleryImgUrl3;
    private String profileImg_id;
    private String galleryImg1_id;
    private String galleryImg2_id;
    private String galleryImg3_id;
    private String contactInfo;

    // Costruttore
    public HobbyResponseDto() {
    }

    // Metodi getter e setter
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id=id;
    }
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public String getGalleryImgUrl1() {
        return galleryImgUrl1;
    }

    public void setGalleryImgUrl1(String galleryImgUrl1) {
        this.galleryImgUrl1 = galleryImgUrl1;
    }

    public String getGalleryImgUrl2() {
        return galleryImgUrl2;
    }

    public void setGalleryImgUrl2(String galleryImgUrl2) {
        this.galleryImgUrl2 = galleryImgUrl2;
    }

    public String getGalleryImgUrl3() {
        return galleryImgUrl3;
    }

    public void setGalleryImgUrl3(String galleryImgUrl3) {
        this.galleryImgUrl3 = galleryImgUrl3;
    }

    public String getProfileImg_id() {
        return profileImg_id;
    }

    public void setProfileImg_id(String profileImg_id) {
        this.profileImg_id = profileImg_id;
    }

    public String getGalleryImg1_id() {
        return galleryImg1_id;
    }

    public void setGalleryImg1_id(String galleryImg1_id) {
        this.galleryImg1_id = galleryImg1_id;
    }

    public String getGalleryImg2_id() {
        return galleryImg2_id;
    }

    public void setGalleryImg2_id(String galleryImg2_id) {
        this.galleryImg2_id = galleryImg2_id;
    }

    public String getGalleryImg3_id() {
        return galleryImg3_id;
    }

    public void setGalleryImg3_id(String galleryImg3_id) {
        this.galleryImg3_id = galleryImg3_id;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
