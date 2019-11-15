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
package sk.fourq.mario.taskappbootstrap.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import sk.fourq.bootstrap.domain.Acl;
import sk.fourq.bootstrap.domain.AclPrincipal;
import sk.fourq.bootstrap.domain.User;
import sk.fourq.bootstrap.domain.interfaces.AclAware;
import sk.fourq.bootstrap.domain.interfaces.IdAware;
import sk.fourq.bootstrap.domain.interfaces.OwnerAware;
import sk.fourq.bootstrap.rest.jackson.ExcludeFieldsPropertyFilter;
import sk.fourq.mario.taskappbootstrap.domain.json.OwnerSerializer;

@Entity
@Table(name = "TASK")
@JsonFilter(ExcludeFieldsPropertyFilter.NAME)
public class Task implements IdAware<Integer>, AclAware, OwnerAware {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DESCRIPTION")
    @NotNull
    private String description;

    @Column(name = "DONE")
    @NotNull
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID", nullable = false)
    @JsonSerialize(using = OwnerSerializer.class)
    private User owner;

    @Column(name = "COLOR")
    private String color;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Acl> acl;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public Set<Acl> getAcl() {
        return this.acl;
    }

    @Override
    public void setAcl(Set<Acl> acl) {
        this.acl = acl;
    }

    @Override
    public AclPrincipal getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(AclPrincipal owner) {
        this.owner = (User) owner;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return done == task.done &&
            Objects.equals(id, task.id) &&
            Objects.equals(description, task.description) &&
            Objects.equals(owner, task.owner) &&
            Objects.equals(color, task.color) &&
            Objects.equals(acl, task.acl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, done, owner, color, acl);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Task{");
        sb.append("id=").append(id);
        sb.append(", description='").append(description).append('\'');
        sb.append(", done=").append(done);
        sb.append(", owner=").append(owner);
        sb.append(", color='").append(color).append('\'');
        sb.append(", acl=").append(acl);
        sb.append('}');
        return sb.toString();
    }
}
