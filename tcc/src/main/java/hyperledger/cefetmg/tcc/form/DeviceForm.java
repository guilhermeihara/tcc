package hyperledger.cefetmg.tcc.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DeviceForm {

	@NotNull @NotEmpty
	private String name;
	
	@NotNull
	private Long tokenDuration;
	
	@NotNull
	private Long type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTokenDuration() {
		return tokenDuration;
	}

	public void setTokenDuration(Long tokenDuration) {
		this.tokenDuration = tokenDuration;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}
}
