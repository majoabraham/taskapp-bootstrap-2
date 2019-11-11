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
package sk.fourq.mario.taskappbootstrap.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import sk.fourq.bootstrap.domain.Acl;
import sk.fourq.bootstrap.domain.AclPermission;
import sk.fourq.bootstrap.domain.Domain;
import sk.fourq.bootstrap.domain.User;
import sk.fourq.bootstrap.security.RequestContext;
import sk.fourq.bootstrap.service.AbstractAclAwareService;
import sk.fourq.mario.taskappbootstrap.dao.AclDao;
import sk.fourq.mario.taskappbootstrap.dao.TaskDao;
import sk.fourq.mario.taskappbootstrap.domain.Task;

@Stateless
@LocalBean
public class TaskService extends AbstractAclAwareService<Task, Integer> {

    public TaskService() {
        super(Task.class);
    }

    @Inject
    private RequestContext context;

    @Inject
    private TaskDao taskDao;

    @Inject
    private AclDao aclDao;

    @Override
    protected TaskDao getDao() {
        return this.taskDao;
    }

//    @Override
//    protected void authorize(final Task entity, final AclPermission permission) {
//
//    }

    @Override
    protected void createAcls(Task entity, Task persistedEntity) {

        final User activeUser = context.getUser();
        final Domain domain = activeUser.getDomain();

        Acl aclUserRead = new Acl();
        aclUserRead.setAclPermission(AclPermission.READ);
        aclUserRead.setAclPrincipal(activeUser);
        entity.getAcl().add(aclUserRead);
        aclDao.create(aclUserRead);

        Acl aclUserWrite = new Acl();
        aclUserWrite.setAclPermission(AclPermission.WRITE);
        aclUserWrite.setAclPrincipal(activeUser);
        entity.getAcl().add(aclUserWrite);
        aclDao.create(aclUserWrite);

        Acl aclDomainRead = new Acl();
        aclDomainRead.setAclPermission(AclPermission.READ);
        aclDomainRead.setAclPrincipal(domain);
        entity.getAcl().add(aclDomainRead);
        aclDao.create(aclDomainRead);
    }
}
