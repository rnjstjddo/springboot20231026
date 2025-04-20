package org.example.springboot231026.domain.message;

import org.example.springboot231026.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {


    @Query("select m from Message m where m.recipient= :recipient")
    Optional<List<Message>> findByRecipient(@Param("recipient") Member recipient);

    @Query("select m from Message m where m.sender= :sender")
    Optional<List<Message>> findBySender(@Param("sender") Member sender);


    @Modifying
    @Query("delete from Message m where m.recipient =:recipient and m.name=:name and m.number=:number")
    void deleteRecipient(@Param("recipient") Member recipient, @Param("name") String name, @Param("number") String number);

    @Modifying
    @Query("delete from Message m where m.sender =:sender and m.name=:name and m.number=:number")
    void deleteSender(@Param("sender") Member sender, @Param("name") String name, @Param("number") String number);

    @Query("select m.email from Message m where m.sender= :sender")
    List<Long> getEmailBySender(@Param("sender") Member sender);

    @Query("select m.recipient from Message m where m.recipient= :recipient")
    List<Long> getEmailByRecipient(@Param("recipient") Member recipient);



}
