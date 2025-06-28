package kr.ac.kopo.model;

import java.time.LocalDateTime;

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
@Table(name = "CANVAS_NOTE")
public class CanvasNote {
	  @Id
	  @SequenceGenerator(name="noteSeqGen", sequenceName="SEQ_CANVAS_NOTE", allocationSize=1)
	  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="noteSeqGen")
	  @Column(name="ID")
	  private Long id;	

    @ManyToOne
    @JoinColumn(name = "ROOM_ID", nullable = false)
    @JsonManagedReference
    private ChatRoom room;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
    
    
}

