package info.saladlam.example.spring.noticeboard.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import info.saladlam.example.spring.noticeboard.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

	@Query("SELECT m FROM Message m WHERE m.approvedBy IS NOT NULL AND m.publishDate <= :at AND (m.removeDate IS NULL OR m.removeDate > :at) ORDER BY m.publishDate DESC")
	List<Message> findPublished(@Param("at") LocalDateTime at);

	@Query("SELECT m FROM Message m WHERE m.approvedBy IS NULL ORDER BY m.publishDate DESC")
	List<Message> findWaitingApprove();

	@Query("SELECT m FROM Message m WHERE m.owner = :owner ORDER BY m.publishDate DESC")
	List<Message> findByOwner(@Param("owner") String owner);

}
