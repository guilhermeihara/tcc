package hyperledger.cefetmg.tcc.dto;

public class DtoUser {

	String name;
	String email;
	String password;
	Boolean deleted;

	public DtoUser() {
	}

	public DtoUser(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public DtoUser(String name, String email, Boolean deleted) {
		super();
		this.name = name;
		this.email = email;
		this.deleted = deleted;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
