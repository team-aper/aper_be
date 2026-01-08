package org.aper.web.domain.chat.repository;

import org.aper.web.domain.chat.entity.ChatRoomView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomViewRepository extends JpaRepository<ChatRoomView, Long>, ChatRoomViewRepositoryCustom {
    List<ChatRoomView> findByParticipants(String participants);
}
