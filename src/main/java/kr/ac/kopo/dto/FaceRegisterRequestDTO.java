package kr.ac.kopo.dto;

import java.util.List;

public class FaceRegisterRequestDTO {
    private String username;
    private List<String> images;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
}
