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
package sk.fourq.mario.taskappbootstrap;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import sk.fourq.bootstrap.domain.User;
import sk.fourq.bootstrap.domain.UserEvent;
import sk.fourq.bootstrap.domain.User_;
import sk.fourq.bootstrap.rest.dto.UserEventDto;
import sk.fourq.bootstrap.util.EventDescriptor;
import sk.fourq.bootstrap.util.EventDescriptors;
import sk.fourq.mario.taskappbootstrap.domain.Task;
import sk.fourq.mario.taskappbootstrap.domain.TaskEvent;
import sk.fourq.mario.taskappbootstrap.domain.Task_;
import sk.fourq.mario.taskappbootstrap.dto.TaskEventDto;

/**
 * Startup task, ktorý registruje EventDescriptory
 */
@Singleton
@Startup
public class StartupTask {
    @Inject
    private EventDescriptors eventDescriptors;

    @PostConstruct
    public void onStart() {
        eventDescriptors.add(UserEvent.class.getSimpleName(),
            new EventDescriptor(
                UserEvent.class,
                User_.class,
                User.class,
                "user",
                UserEventDto.class
            ));
        eventDescriptors.add(TaskEvent.class.getSimpleName(),
            new EventDescriptor(
                TaskEvent.class,
                Task_.class,
                Task.class,
                "task",
                TaskEventDto.class
            ));
    }
}
