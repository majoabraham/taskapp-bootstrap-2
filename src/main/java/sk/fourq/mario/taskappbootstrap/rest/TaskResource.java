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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sk.fourq.bootstrap.messaging.EuroSmsService;
import sk.fourq.bootstrap.messaging.MailService;
import sk.fourq.bootstrap.messaging.Message;
import sk.fourq.bootstrap.messaging.NotificationException;
import sk.fourq.bootstrap.search.FindParams;
import sk.fourq.bootstrap.security.RequestContext;
import sk.fourq.bootstrap.security.exception.ClientException;
import sk.fourq.bootstrap.util.ErrorCode;
import sk.fourq.mario.taskappbootstrap.domain.Task;
import sk.fourq.mario.taskappbootstrap.l10n.TaskLocalizationKeys;
import sk.fourq.mario.taskappbootstrap.search.TaskFindParams;
import sk.fourq.mario.taskappbootstrap.service.TaskService;
import sk.fourq.mario.taskappbootstrap.util.TaskUtils;

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
    private EuroSmsService euroSmsService;
    @Inject
    private TaskUtils taskUtils;
    @Inject
    private RequestContext context;

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
            throw new ClientException(
                ErrorCode.ENTITY_NOT_FOUND,
                TaskLocalizationKeys.TASK_ID_DOESNT_EXIST,
                id.toString()
            );
        }
    }

    @POST
    public Response createTask(Task task) {

        if (task == null) {
            throw new ClientException(
                ErrorCode.MISSING_TYPE_AND_ENTITY_ID,
                TaskLocalizationKeys.TASK_NO_TASK_INCLUDED
            );
        }

        task.setOwner(context.getUser());

        taskService.create(task);

        return Response.ok().status(Response.Status.CREATED).build();
    }

    @Path("/{id}")
    @DELETE
    public Response deleteTask(@PathParam("id") Integer id) throws NotificationException {

        Task task = taskService.delete(id);

        if (task != null) {
            Message<Long> message = taskUtils.createMessage(task, id);
//            mailService.send(message);
//            euroSmsService.send(message);
            return Response.ok().status(Response.Status.NO_CONTENT).build();
        } else {
            throw new ClientException(
                ErrorCode.ENTITY_NOT_FOUND,
                TaskLocalizationKeys.TASK_ID_DOESNT_EXIST,
                id.toString()
            );
        }
    }

    @PUT
    public Response updateTask(Task task) {

        if (task == null) {
            throw new ClientException(
                ErrorCode.MISSING_TYPE_AND_ENTITY_ID,
                TaskLocalizationKeys.TASK_NO_TASK_INCLUDED
            );
        }

        Integer taskID = task.getId();

        if (taskID == null) {
            throw new ClientException(
                ErrorCode.MISSING_TYPE_AND_ENTITY_ID,
                TaskLocalizationKeys.TASK_ID_MISSING
            );
        }

        if (taskService.find(taskID) == null) {
            throw new ClientException(
                ErrorCode.ENTITY_NOT_FOUND,
                TaskLocalizationKeys.TASK_ID_DOESNT_EXIST,
                task.getId().toString()
            );
        }

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

    @Path("/filter-color/")
    @GET
    public Response filterTasks(@QueryParam("color") final Set<String> colors) {

        TaskFindParams taskFindParams = TaskFindParams.create();
        taskFindParams.setColors(colors);

        List<Task> tasks = taskService.findAll(taskFindParams).getItems();

        return Response.ok(tasks).build();
    }
}
