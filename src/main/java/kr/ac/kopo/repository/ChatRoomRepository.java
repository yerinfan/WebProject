package kr.ac.kopo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.ac.kopo.model.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 참가자(user.id)에 속한 방만 찾기
    List<ChatRoom> findByParticipants_Id(Long userId);

    // 초대 코드로 방 찾기
    Optional<ChatRoom> findByInviteCode(String inviteCode);

}