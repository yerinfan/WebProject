package kr.ac.kopo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.ac.kopo.model.CanvasNote;

public interface CanvasNoteRepository extends JpaRepository<CanvasNote, Long> {
    // 여기를 반드시 선언해야 findByRoomId() 호출 가능
    List<CanvasNote> findByRoomId(Long roomId);
    
    Optional<CanvasNote> findByIdAndRoomId(Long id, Long roomId);
}