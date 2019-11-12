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
package sk.fourq.mario.taskappbootstrap.servlet;

import java.time.OffsetDateTime;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import sk.fourq.bootstrap.dao.EventDao;
import sk.fourq.bootstrap.dao.UserDao;
import sk.fourq.bootstrap.domain.UserEvent;
import sk.fourq.bootstrap.domain.enums.EventSeverity;
import sk.fourq.bootstrap.domain.interfaces.UserAware;
import sk.fourq.bootstrap.l10n.LocalizableText;
import sk.fourq.bootstrap.security.RequestContext;
import sk.fourq.bootstrap.servlet.BootstrapAuthEndpoint;

@WebServlet(urlPatterns = "/auth/*")
public class AuthEndpoint extends BootstrapAuthEndpoint {

    @Inject
    private UserDao userDao;


    @Inject
    private EventDao eventDao;

    @Inject
    private RequestContext requestContext;

    @Override
    protected UserAware findUserAware(String login) {
        return userDao.findByName(login);
    }

    @Override
    protected void executeAfterLoginSucceeded(HttpServletRequest httpServletRequest, UserAware userAware) {
        UserEvent userEvent = new UserEvent(
            true,
            EventSeverity.INFO,
            userAware.getUser(),
            new LocalizableText("Prihlásenie", false),
            new LocalizableText("Používateľ sa prihlásil", false)
        );

        //FIXME: Ešte raz sa zamyslieť, či chceme naozaj obchádzať servisu a používať na systémové eventy dao
        //pretože takto strácame autosetovanie týchto atribútov
        userEvent.setCreationDate(OffsetDateTime.now());
        userEvent.setDomain(requestContext.getDomain());
        userEvent.setCreator(requestContext.getUser());

        eventDao.create(userEvent);
    }

    @Override
    protected void executeAfterLoginFailed(HttpServletRequest httpServletRequest, UserAware userAware) {
        //DO NOTHING
    }
}
