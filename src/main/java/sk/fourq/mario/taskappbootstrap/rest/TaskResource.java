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
package sk.fourq.mario.taskappbootstrap.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sk.fourq.bootstrap.l10n.LocalizationKeys;
import sk.fourq.bootstrap.l10n.Localizer;
import sk.fourq.bootstrap.messaging.DefaultMessage;
import sk.fourq.bootstrap.messaging.MailService;
import sk.fourq.bootstrap.messaging.Message;
import sk.fourq.bootstrap.messaging.MessageParticipant;
import sk.fourq.bootstrap.messaging.NotificationException;
import sk.fourq.bootstrap.search.FindParams;
import sk.fourq.bootstrap.security.Configs;
import sk.fourq.bootstrap.security.RequestContext;
import sk.fourq.bootstrap.service.UserService;
import sk.fourq.mario.taskappbootstrap.domain.Task;
import sk.fourq.mario.taskappbootstrap.service.TaskService;

@Path("task")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TaskResource {

    @Inject
    private TaskService taskService;
    @Inject
    private MailService mailService;
    @Inject
    private Configs configs;
    @Inject
    private Localizer localizer;
    @Inject
    private RequestContext context;
    @Inject
    private UserService userService;

    public TaskResource() {

    }

    @GET
    public Response getTasks() {

        FindParams findParams = FindParams.create();
        List<Task> tasks = taskService.findAll(findParams).getItems();

        return Response.ok(tasks).build();
    }

    @Path("/{id}")
    @GET
    public Response getTask(@PathParam("id") Integer id) {

        Task task = taskService.find(id);

        if (task != null) {
            return Response.ok(task).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response createTask(Task task) {

        taskService.create(task);

        return Response.ok().status(Response.Status.CREATED).build();
    }

    @Path("/{id}")
    @DELETE
    public Response deleteTask(@PathParam("id") Integer id) throws NotificationException {

        Task task = taskService.delete(id);

        if (task != null) {
            Message<Long> message = createMessage(task, id);
            mailService.send(message);
            return Response.ok().status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private Message<Long> createMessage(Task task, Integer id) {
        DefaultMessage message = new DefaultMessage();
        message.setBody(localizer.get(
            LocalizationKeys.BOOTSTRAP_ENTITY_DELETED,
            configs.getLogLocale(),
            task.getClass().getSimpleName(), task.getId()
        ));
        message.setSubject("deleted task");
        Set<MessageParticipant> recipients = new HashSet<>();
        recipients.add(userService.findByName("mario.abraham"));
        message.setRecipients(recipients);

        return message;
    }

    @PUT
    public Response updateTask(Task task) {

        taskService.update(task);

        return Response.ok().status(Response.Status.NO_CONTENT).build();
    }

    @Path("/filter/{text}")
    @GET
    public Response filterTasks(@PathParam("text") String text) {

        FindParams findParams = FindParams.create();
        findParams.addFilter("description", text);

        List<Task> tasks = taskService.findAll(findParams).getItems();

        return Response.ok(tasks).build();
    }
}
