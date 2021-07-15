package hyperledger.cefetmg.tcc.dto;

import java.time.LocalDateTime;

import hyperledger.cefetmg.tcc.models.Device;

public class DtoDevice {

	private Long id;

	private String name;
	private LocalDateTime creationDate;
	private Long type;

	private Long userId;

	private String token;
	private LocalDateTime tokenCreationDate;
	private LocalDateTime tokenExpirationDate;

	
	public DtoDevice(Long id, String name, LocalDateTime creationDate, Long type, Long userId, String token,
			LocalDateTime tokenCreationDate, LocalDateTime tokenExpirationDate) {
		this.id = id;
		this.name = name;
		this.creationDate = creationDate;
		this.type = type;
		this.userId = userId;
		this.token = token;
		this.tokenCreationDate = tokenCreationDate;
		this.tokenExpirationDate = tokenExpirationDate;
	}
	
	public DtoDevice(Device device) {
		this.id = device.getId();
		this.name = device.getName();
		this.creationDate = device.getCreationDate();
		this.type = device.getType();
		this.userId = device.getUser().getId();
		this.token = device.getToken().getToken();
		this.tokenCreationDate = device.getToken().getCreationDate();
		this.tokenExpirationDate = device.getToken().getExpirationDate();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getTokenCreationDate() {
		return tokenCreationDate;
	}

	public void setTokenCreationDate(LocalDateTime tokenCreationDate) {
		this.tokenCreationDate = tokenCreationDate;
	}

	public LocalDateTime getTokenExpirationDate() {
		return tokenExpirationDate;
	}

	public void setTokenExpirationDate(LocalDateTime tokenExpirationDate) {
		this.tokenExpirationDate = tokenExpirationDate;
	}

}
