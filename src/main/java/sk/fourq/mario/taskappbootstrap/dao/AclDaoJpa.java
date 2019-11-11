package sk.fourq.mario.taskappbootstrap.dao;

import sk.fourq.bootstrap.dao.jpa.AbstractDaoJpa;
import sk.fourq.bootstrap.domain.Acl;

public class AclDaoJpa extends AbstractDaoJpa<Acl, Integer> implements AclDao {

    public AclDaoJpa() {
        super(Acl.class);
    }
}
