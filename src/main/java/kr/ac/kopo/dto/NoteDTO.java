package kr.ac.kopo.dto;

import java.time.LocalDateTime;

public class NoteDTO {
    private Long id;
    private String note;
    private LocalDateTime updatedAt;

    public NoteDTO(Long id, String note, LocalDateTime updatedAt) {
        this.id = id;
        this.note = note;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getNote() { return note; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setNote(String note) { this.note = note; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
