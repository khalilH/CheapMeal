package Util.Hibernate.Model;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "SESSIONS")
public class Sessions {
	@Id
	int idSession;
	
	@Column(name = "cleSession")
	String cleSession;
	
	@Column(name = "dateExpiration")
	Timestamp dateExpiration;

	@MapsId
    @OneToOne
    @JoinColumn(name="idSession")
	@OnDelete(action = OnDeleteAction.CASCADE) 
	Utilisateurs u;
	
	public Sessions() {
	}

	public Sessions(int idSession, String cleSession, Timestamp dateExpiration) {
		super();
		this.idSession = idSession;
		this.cleSession = cleSession;
		this.dateExpiration = dateExpiration;
	}

	public int getIdSession() {
		return idSession;
	}

	public void setIdSession(int idSession) {
		this.idSession = idSession;
	}

	public String getCleSession() {
		return cleSession;
	}

	public void setCleSession(String cleSession) {
		this.cleSession = cleSession;
	}

	public Timestamp getDateExpiration() {
		return dateExpiration;
	}

	public void setDateExpiration(Timestamp dateExpiration) {
		this.dateExpiration = dateExpiration;
	}

	public Utilisateurs getU() {
		return u;
	}

	public void setU(Utilisateurs u) {
		this.u = u;
	}

}
