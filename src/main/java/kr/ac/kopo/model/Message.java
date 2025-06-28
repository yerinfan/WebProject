package kr.ac.kopo.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "MESSAGE")
public class Message {
	  @Id
	  @SequenceGenerator(name="msgSeqGen", sequenceName="SEQ_MESSAGE", allocationSize=1)
	  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="msgSeqGen")
	  @Column(name="ID")
	  private Long id;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID", nullable = false)
    @JsonManagedReference
    private ChatRoom room;

    @ManyToOne
    @JoinColumn(name = "SENDER_ID", nullable = false)
    @JsonManagedReference
    private User sender;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "TYPE")
    private String type; // TEXT, IMAGE

    @CreationTimestamp
    @Column(name = "TIMESTAMP", updatable = false)
    private LocalDateTime timestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ChatRoom getRoom() {
		return room;
	}

	public void setRoom(ChatRoom room) {
		this.room = room;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
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