// kr.ac.kopo.dto.ChatMessageResponseDTO.java
package kr.ac.kopo.dto;

import java.time.LocalDateTime;

public class ChatMessageResponseDTO {
    private Long id;
    private Long roomId;
    private String sender;
    private String content;
    private String type;
    private LocalDateTime timestamp;

    public ChatMessageResponseDTO() {}

    public ChatMessageResponseDTO(Long id, Long roomId, String sender,
                                  String content, String type, LocalDateTime timestamp) {
        this.id        = id;
        this.roomId    = roomId;
        this.sender    = sender;
        this.content   = content;
        this.type      = type;
        this.timestamp = timestamp;
    }
    // getters and setters omitted for brevity

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
