package kr.ac.kopo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.ac.kopo.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
	
    List<Message> findByRoomId(Long roomId);
    List<Message> findByRoomIdOrderByTimestampAsc(Long roomId);
}

