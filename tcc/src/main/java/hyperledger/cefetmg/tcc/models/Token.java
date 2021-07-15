package hyperledger.cefetmg.tcc.models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Token {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String token;
	@Column(name="creation_date")
	private LocalDateTime creationDate = LocalDateTime.now();
	@Column(name="expiration_date")
	private LocalDateTime expirationDate;
	private Long duration;
		
	public Token() {
		super();
	}

	public Token(String token, Long duration) {
		Date today = new Date();
		Date expirationDate = new Date(today.getTime() + duration);
		this.token = token;
		this.duration = duration;
		this.expirationDate = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;	
	}
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "Token [id=" + id + ", token=" + token + ", creationDate=" + creationDate + ", expirationDate="
				+ expirationDate + ", duration=" + duration + "]";
	}
}
