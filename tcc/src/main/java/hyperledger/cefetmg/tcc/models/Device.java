package hyperledger.cefetmg.tcc.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User user;

	@OneToOne(fetch = FetchType.EAGER)
	private Token token;

	private String address;

	@Column(name = "creation_date")
	private LocalDateTime creationDate = LocalDateTime.now();

	private Long type;

	private String name;
	
	private String value;
	
	public Device() {
		super();
	}

	
	public Device(Token token, User user, Long type, String name) {
		super();
		this.token = token;
		this.user = user;
		this.type = type;
		this.name = name;
	}
	
	
	public Device(User user, String address, Long type, String name) {
		super();
		this.user = user;
		this.address = address;
		this.type = type;
		this.name = name;
	}


	public Device(User user, Long type, String name) {
		super();
		this.user = user;
		this.type = type;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Device [id=" + id + ", token=" + token + ", user=" + user + ", creationDate=" + creationDate + ", type="
				+ type + ", name=" + name + "]";
	}

}
