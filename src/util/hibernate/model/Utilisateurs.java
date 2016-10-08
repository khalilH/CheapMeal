package util.hibernate.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="UTILISATEURS")
public class Utilisateurs {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	int id;
	
	@Column(name="login", unique = true)
	String login;
	
	@Column(name="mdp")
	String mdp;
	
	@Column(name="prenom")
	String prenom;
	
	@Column(name="nom")
	String nom;
	
	@Column(name="mail", unique = true)
	String mail;
	
	@OneToOne(cascade=CascadeType.REMOVE, mappedBy = "utilisateur")
	Sessions sessions;
	
	@OneToOne(cascade=CascadeType.REMOVE, mappedBy = "utilisateur")
	Profils profils;
	
	public Utilisateurs() {}
	
	public Utilisateurs(String login, String mdp, String prenom, String nom, String mail) {
		super();
		this.login = login;
		this.mdp = mdp;
		this.prenom = prenom;
		this.nom = nom;
		this.mail = mail;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getMdp() {
		return mdp;
	}
	
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getMail() {
		return mail;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
		
	public Sessions getSessions() {
		return sessions;
	}
	public void setSessions(Sessions sessions) {
		this.sessions = sessions;
	}

	public Profils getProfil() {
		return profils;
	}
	public void setProfil(Profils profils) {
		this.profils = profils;
	}
	

	
	
	
}
