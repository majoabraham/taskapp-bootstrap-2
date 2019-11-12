/*
 * Created by 4Q developer (dev@4q.sk)
 * Copyright (c) 2019
 * 4Q s.r.o. All rights reserved.
 * http://www.4q.eu
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package sk.fourq.mario.taskappbootstrap.util;

import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import sk.fourq.bootstrap.l10n.LocalizationKeys;
import sk.fourq.bootstrap.l10n.Localizer;
import sk.fourq.bootstrap.messaging.DefaultMessage;
import sk.fourq.bootstrap.messaging.DefaultMessageParticipant;
import sk.fourq.bootstrap.messaging.Message;
import sk.fourq.bootstrap.messaging.MessageParticipant;
import sk.fourq.bootstrap.security.Configs;
import sk.fourq.mario.taskappbootstrap.domain.Task;

@RequestScoped
public class TaskUtils {

    @Inject
    private Configs configs;
    @Inject
    private Localizer localizer;

    public Message<Long> createMessage(Task task, Integer id) {
        DefaultMessage message = new DefaultMessage();
        message.setBody(localizer.get(
            LocalizationKeys.BOOTSTRAP_ENTITY_DELETED,
            configs.getLogLocale(),
            task.getClass().getSimpleName(), task.getId()
        ));
        message.setSubject("deleted task");
        Set<MessageParticipant> recipients = new HashSet<>();
        DefaultMessageParticipant messageParticipant = new DefaultMessageParticipant();
        messageParticipant.setEmail("mario.abraham@outlook.com");
        messageParticipant.setTel("+421944264747");
        recipients.add(messageParticipant);
        message.setRecipients(recipients);

        return message;
    }

}
