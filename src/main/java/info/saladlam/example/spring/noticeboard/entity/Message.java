package info.saladlam.example.spring.noticeboard.entity;

import java.time.LocalDateTime;

public class Message {

	private Long id;
	private LocalDateTime publishDate;
	private LocalDateTime removeDate;
	private String owner;
	private String description;
	private String approvedBy;
	private LocalDateTime approvedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(LocalDateTime publishDate) {
		this.publishDate = publishDate;
	}

	public LocalDateTime getRemoveDate() {
		return removeDate;
	}

	public void setRemoveDate(LocalDateTime removeDate) {
		this.removeDate = removeDate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public LocalDateTime getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(LocalDateTime approvedDate) {
		this.approvedDate = approvedDate;
	}

}
