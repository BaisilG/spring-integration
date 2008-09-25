/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.mail;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.DataInputStream;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.integration.message.MessageBuilder;
import org.springframework.mail.SimpleMailMessage;

/**
 * @author Marius Bogoevici
 */
public class MailSendingMessageConsumerTests {

	private MailSendingMessageConsumer consumer;

	private StubJavaMailSender mailSender;


	@Before
	public void setUp() throws Exception {
		this.mailSender = new StubJavaMailSender(new MimeMessage((Session) null));
		this.consumer = new MailSendingMessageConsumer(this.mailSender);
	}

	@After
	public void reset() {
		this.mailSender.reset();
	}


	@Test
	public void textMessage() {
		this.consumer.onMessage(MailTestsHelper.createIntegrationMessage());
		SimpleMailMessage mailMessage = MailTestsHelper.createSimpleMailMessage();
		assertEquals("no mime message should have been sent",
				0, mailSender.getSentMimeMessages().size());
		assertEquals("only one simple message must be sent",
				1, mailSender.getSentSimpleMailMessages().size());
		assertEquals("message content different from expected",
				mailMessage, mailSender.getSentSimpleMailMessages().get(0));
	}

	@Test
	public void byteArrayMessage() throws Exception {
		byte[] payload = {1, 2, 3};
		org.springframework.integration.message.Message<byte[]> message =
				MessageBuilder.withPayload(payload)
				.setHeader(MailHeaders.ATTACHMENT_FILENAME, "attachment.txt")
				.setHeader(MailHeaders.TO, MailTestsHelper.TO)
				.build();
		this.consumer.onMessage(message);
		byte[] buffer = new byte[1024];
		MimeMessage mimeMessage = this.mailSender.getSentMimeMessages().get(0);
		assertTrue("message must be multipart", mimeMessage.getContent() instanceof Multipart);
		int size = new DataInputStream(((Multipart) mimeMessage.getContent()).getBodyPart(0).getInputStream()).read(buffer);
		assertEquals("buffer size does not match", payload.length, size);
		byte[] messageContent = new byte[size];
		System.arraycopy(buffer, 0, messageContent, 0, payload.length);
		assertArrayEquals("buffer content does not match", payload, messageContent);
		assertEquals(mimeMessage.getRecipients(Message.RecipientType.TO).length, MailTestsHelper.TO.length);
	}

	@Test
	public void mailHeaders() {
		this.consumer.onMessage(MailTestsHelper.createIntegrationMessage());
		SimpleMailMessage mailMessage = MailTestsHelper.createSimpleMailMessage();
		assertEquals("no mime message should have been sent",
				0, mailSender.getSentMimeMessages().size());
		assertEquals("only one simple message must be sent",
				1, mailSender.getSentSimpleMailMessages().size());
		assertEquals("message content different from expected",
				mailMessage, mailSender.getSentSimpleMailMessages().get(0));
	}

}
