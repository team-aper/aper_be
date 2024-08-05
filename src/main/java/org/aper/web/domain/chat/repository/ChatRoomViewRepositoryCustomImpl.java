package org.aper.web.domain.chat.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ChatRoomViewRepositoryCustomImpl implements ChatRoomViewRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void updateChatRoomParticipantsView() {
        String sql = "CREATE OR REPLACE VIEW chat_room_participants_view AS " +
                "SELECT " +
                "    cr.id AS chat_room_id, " +
                "    CONCAT( " +
                "        COALESCE( " +
                "            GROUP_CONCAT(CASE WHEN cp.is_tutor = TRUE THEN u.user_id END ORDER BY u.user_id SEPARATOR '&'), " +
                "            '' " +
                "        ), " +
                "        CASE " +
                "            WHEN COUNT(CASE WHEN cp.is_tutor = TRUE THEN 1 END) > 0 " +
                "                AND COUNT(CASE WHEN cp.is_tutor = FALSE THEN 1 END) > 0 " +
                "            THEN '-' " +
                "            ELSE '' " +
                "        END, " +
                "        COALESCE( " +
                "            GROUP_CONCAT(CASE WHEN cp.is_tutor = FALSE THEN u.user_id END ORDER BY u.user_id SEPARATOR '&'), " +
                "            '' " +
                "        ) " +
                "    ) AS participants " +
                "FROM " +
                "    chat_room cr " +
                "JOIN " +
                "    chat_participant cp ON cr.id = cp.chat_room_id " +
                "JOIN " +
                "    users u ON cp.user_id = u.user_id " +
                "GROUP BY " +
                "    cr.id;";

        entityManager.createNativeQuery(sql).executeUpdate();
    }
}
