package kr.ac.kopo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "CHAT_ROOM")
@JsonIgnoreProperties({ "participants" })
public class ChatRoom {
	@Id
	@SequenceGenerator(name = "chatRoomSeqGen", sequenceName = "SEQ_CHAT_ROOM", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chatRoomSeqGen")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "INVITE_CODE", nullable = false, unique = true)
	private String inviteCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private User owner;
	
	@ManyToMany
	@JoinTable(name = "CHAT_ROOM_PARTICIPANTS", joinColumns = @JoinColumn(name = "ROOM_ID"), inverseJoinColumns = @JoinColumn(name = "USER_ID"))
	@JsonManagedReference
	private Set<User> participants = new HashSet<>();

	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL, // 방 삭제 시 연관된 메시지 전부 삭제
			orphanRemoval = true)
	private List<Message> messages = new ArrayList<>();

	// ★ CanvasNote 연관관계 추가
    @OneToMany(mappedBy = "room",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<CanvasNote> notes = new ArrayList<>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public Set<User> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<User> participants) {
		this.participants = participants;
	}

	// 기존 getter/setter 외에 아래 getter/setter도 추가
    public List<CanvasNote> getNotes() {
        return notes;
    }

    public void setNotes(List<CanvasNote> notes) {
        this.notes = notes;
    }

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
    
    
}
