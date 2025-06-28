package kr.ac.kopo.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {
	  @Id
	  @SequenceGenerator(name="msgSeqGen", sequenceName="SEQ_MESSAGE", allocationSize=1)
	  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="msgSeqGen")
	  @Column(name="ID")
	  private Long id;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password; // stored encrypted

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role;

    // --- 아래 추가 ---
    @ManyToMany(mappedBy = "participants")
    @JsonBackReference
    private Set<ChatRoom> rooms = new HashSet<>();

    public Set<ChatRoom> getRooms() {
        return rooms;
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}


    
}
