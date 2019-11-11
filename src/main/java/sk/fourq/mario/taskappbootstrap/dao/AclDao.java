package sk.fourq.mario.taskappbootstrap.dao;

import javax.ejb.Local;
import sk.fourq.bootstrap.dao.Dao;
import sk.fourq.bootstrap.domain.Acl;

@Local
public interface AclDao extends Dao<Acl, Integer> {
}
