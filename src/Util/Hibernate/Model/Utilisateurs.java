package Util.Hibernate.Model;

public class Utilisateurs {
	int id;
	String login;
	String mdp;
	String prenom;
	String nom;
	String mail;
	
	
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
	
	
}
