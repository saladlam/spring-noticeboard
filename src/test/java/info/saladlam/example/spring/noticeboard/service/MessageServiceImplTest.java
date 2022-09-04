package info.saladlam.example.spring.noticeboard.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import info.saladlam.example.spring.noticeboard.dto.MessageDto;
import info.saladlam.example.spring.noticeboard.entity.Message;
import info.saladlam.example.spring.noticeboard.repository.MessageRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class MessageServiceImplTest {

	@TestConfiguration
	static class MessageServiceImplTestContextConfiguration {

		@Bean
		public MessageService messageService() {
			return new MessageServiceImpl();
		}

		@Bean
		public Mapper mapper() {
			List<String> mappingFiles = new ArrayList<>();
			mappingFiles.add("dozerJdk8Converters.xml");

			DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
			dozerBeanMapper.setMappingFiles(mappingFiles);
			return dozerBeanMapper;
		}

	}

	@Autowired
	private MessageService messageService;

	@MockBean
	private MessageRepository messageRepository;

	private Message buildPreApprovMessage() {
		Message msg = new Message();
		msg.setId(1L);
		msg.setPublishDate(LocalDateTime.of(2021, 2, 1, 12, 0));
		msg.setRemoveDate(LocalDateTime.of(2021, 2, 28, 12, 0));
		msg.setOwner("staff1");
		msg.setDescription("Test 1");
		return msg;
	}

	private Message buildApprovedMessage() {
		Message msg = buildPreApprovMessage();
		msg.setApprovedBy("supervisor1");
		msg.setApprovedDate(LocalDateTime.of(2021, 1, 31, 12, 0));
		return msg;
	}

	@Test
	public void findOne_published() {
		Mockito.when(messageRepository.findOne(1L)).thenReturn(buildApprovedMessage());
		MessageDto res = messageService.findOne(1L, LocalDateTime.of(2021, 2, 2, 12, 0));

		Mockito.verify(messageRepository).findOne(1L);
		assertEquals(1L, res.getId(), "id is not match");
		assertEquals(MessageDto.PUBLISHED, res.getStatus(), "message is not published");
	}

	@Test
	public void findOne_waitForApprove() {
		Mockito.when(messageRepository.findOne(1L)).thenReturn(buildPreApprovMessage());
		MessageDto res = messageService.findOne(1L, LocalDateTime.of(2021, 2, 2, 12, 0));

		assertEquals(MessageDto.WAITING_APPROVE, res.getStatus(), "message is approved");
	}

	@Test
	public void findOne_expired() {
		Mockito.when(messageRepository.findOne(1L)).thenReturn(buildApprovedMessage());
		MessageDto res = messageService.findOne(1L, LocalDateTime.of(2021, 2, 28, 12, 1));

		assertEquals(MessageDto.EXPIRED, res.getStatus(), "message is not expired");
	}

}
