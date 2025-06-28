// kr.ac.kopo.dto.ChatMessageDTO.java
package kr.ac.kopo.dto;

public class ChatMessageDTO {
    private String sender;
    private String content;
    private String type;    // 필요하다면

    // Jackson 바인딩용 기본 생성자
    public ChatMessageDTO() {}

    // getters / setters
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
