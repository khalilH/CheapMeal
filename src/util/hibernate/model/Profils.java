package util.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "PROFILS")
public class Profils {
	
	@Id
	int idProfil;
	
	@Column(name = "bio")
	String bio;
	
	@MapsId
	@OneToOne
	@JoinColumn(name = "idProfil")
	@OnDelete(action = OnDeleteAction.CASCADE)
	Utilisateurs utilisateur;
	
	public Profils() {}
	
	public Profils(int idProfil, String bio) {
		super();
		this.idProfil = idProfil;
		this.bio = bio;
	}

	public int getIdProfil() {
		return idProfil;
	}
	
	public void setIdProfil(int idProfil) {
		this.idProfil = idProfil;
	}
	
	public String getBio() {
		return bio;
	}
	
	public void setBio(String bio) {
		this.bio = bio;
	}

	public Utilisateurs getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateurs utilisateur) {
		this.utilisateur = utilisateur;
	}
}
