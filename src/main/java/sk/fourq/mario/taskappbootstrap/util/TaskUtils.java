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

    private DefaultMessageParticipant messageParticipant;

    public Message<Long> createMessage(Task task, Integer id) {
        DefaultMessage message = new DefaultMessage();
        message.setBody(localizer.get(
            LocalizationKeys.BOOTSTRAP_ENTITY_DELETED,
            configs.getLogLocale(),
            task.getClass().getSimpleName(), task.getId()
        ));
        message.setSubject("deleted task");
        Set<MessageParticipant> recipients = new HashSet<>();
        messageParticipant = new DefaultMessageParticipant();
        messageParticipant.setEmail("mario.abraham@outlook.com");
        messageParticipant.setTel("+421944264747");
        recipients.add(messageParticipant);
        message.setRecipients(recipients);

        return message;
    }

}
