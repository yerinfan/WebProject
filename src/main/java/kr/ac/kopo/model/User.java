package kr.ac.kopo.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;


    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role;

    @ManyToMany(mappedBy = "participants")
    @JsonBackReference
    private Set<ChatRoom> rooms = new HashSet<>();
    
    @Column(name = "FACE_REGISTERED")
    private boolean faceRegistered = false;

    public boolean isFaceRegistered() {
        return faceRegistered;
    }

    public void setFaceRegistered(boolean faceRegistered) {
        this.faceRegistered = faceRegistered;
    }

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
