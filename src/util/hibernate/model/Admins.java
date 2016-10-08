package util.hibernate.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ADMINS")
public class Admins {

	@Id
	Integer id;

	public Admins() {}
	
	public Admins(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setLogin(Integer id) {
		this.id = id;
	}
	
}
