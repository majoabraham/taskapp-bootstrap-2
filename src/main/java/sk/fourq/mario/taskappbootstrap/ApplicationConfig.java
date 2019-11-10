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

import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.plugins.providers.multipart.MimeMultipartProvider;
import org.jboss.resteasy.plugins.validation.ValidatorContextResolver;
import sk.fourq.bootstrap.rest.ErrorExceptionMapper;
import sk.fourq.bootstrap.rest.OptionsHandler;
import sk.fourq.bootstrap.rest.filter.BootstrapAuthFilter;
import sk.fourq.bootstrap.rest.filter.BootstrapInitializationFilter;
import sk.fourq.bootstrap.rest.filter.CudLimitFilter;
import sk.fourq.bootstrap.rest.filter.LicenseConsentFilter;
import sk.fourq.bootstrap.rest.filter.OriginAndRefererFilter;
import sk.fourq.bootstrap.rest.filter.XsrfProtectionFilter;
import sk.fourq.bootstrap.rest.jackson.JsonVulnerabilityWriter;
import sk.fourq.bootstrap.rest.jackson.ObjectMapperProvider;
import sk.fourq.bootstrap.rest.resource.defaults.DefaultErrorResource;
import sk.fourq.bootstrap.rest.resource.defaults.DefaultEventResource;
import sk.fourq.bootstrap.rest.resource.defaults.DefaultGroupResource;
import sk.fourq.bootstrap.rest.resource.defaults.DefaultMaintenanceResource;
import sk.fourq.bootstrap.rest.resource.defaults.DefaultUserResource;
import sk.fourq.mario.taskappbootstrap.rest.TaskResource;

@ApplicationPath("rest")
public class ApplicationConfig extends Application {

    final private Set<Class<?>> resources = new HashSet<>();

    public ApplicationConfig() {
        //BOOTSTRAP
        this.resources.add(BootstrapAuthFilter.class);
        this.resources.add(OriginAndRefererFilter.class);
        this.resources.add(XsrfProtectionFilter.class);
        this.resources.add(CudLimitFilter.class);
        this.resources.add(BootstrapInitializationFilter.class);
        this.resources.add(OptionsHandler.class);
        this.resources.add(ErrorExceptionMapper.class);
        this.resources.add(LicenseConsentFilter.class);
        this.resources.add(JsonVulnerabilityWriter.class);
        this.resources.add(ObjectMapperProvider.class);
        //RESTEASY
        this.resources.add(ResteasyJackson2Provider.class);
        this.resources.add(ValidatorContextResolver.class);
        this.resources.add(MimeMultipartProvider.class);
        //JACKSON
        this.resources.add(JacksonFeatures.class);
        //BOOTSTRAP RESOURCES
        this.resources.add(DefaultErrorResource.class);
        this.resources.add(DefaultEventResource.class);
        this.resources.add(DefaultUserResource.class);
        this.resources.add(DefaultGroupResource.class);
        this.resources.add(DefaultMaintenanceResource.class);
        //CUSTOM RESOURCES
        this.resources.add(TaskResource.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return Collections.unmodifiableSet(this.resources);
    }
}
