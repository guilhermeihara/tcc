package hyperledger.cefetmg.tcc.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hyperledger.cefetmg.tcc.dto.DtoUser;
import hyperledger.cefetmg.tcc.form.UserDeleteForm;
import hyperledger.cefetmg.tcc.form.UserForm;
import hyperledger.cefetmg.tcc.form.UserUpdateForm;
import hyperledger.cefetmg.tcc.models.User;
import hyperledger.cefetmg.tcc.repository.UserRepository;
import hyperledger.cefetmg.tcc.services.HyperledgerUserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	HyperledgerUserService _hyperledgerService;

	@Autowired
	UserRepository _userRepository;

	@PostMapping
	public ResponseEntity<DtoUser> createUser(HttpServletRequest request, @RequestBody @Valid UserForm userForm) {

		Optional<User> hasUser = _userRepository.findByEmail(userForm.getEmail());

		if (hasUser.isEmpty()) {
			String encryptedPassword = new BCryptPasswordEncoder().encode(userForm.getPassword());
			User user = new User(userForm.getName(), userForm.getEmail(), encryptedPassword);
			DtoUser dtoUserHyperledger = new DtoUser(user.getName(), user.getEmail(), encryptedPassword);
			DtoUser dtoUser = new DtoUser(user.getName(), user.getEmail(), userForm.getPassword());
			if(_hyperledgerService.createUser(dtoUserHyperledger)) {				
				_userRepository.save(user);
				return ResponseEntity.ok(dtoUser);				
			}
		}
		return ResponseEntity.badRequest().build();
	}

	@DeleteMapping
	public ResponseEntity<DtoUser> deleteUser(HttpServletRequest request, @RequestBody @Valid UserDeleteForm userForm) {

		Optional<User> hasUser = _userRepository.findByEmail(userForm.getEmail());

		if (hasUser.isPresent()) {
			User userToDelete = hasUser.get();
			_userRepository.delete(userToDelete);
			DtoUser userDeleted = new DtoUser(userToDelete.getName(), userToDelete.getEmail(), true);
			if(_hyperledgerService.deleteUser(userToDelete.getEmail())){				
				return ResponseEntity.ok(userDeleted);
			}
		}
		return ResponseEntity.badRequest().build();
	}


	@PatchMapping
	public ResponseEntity<DtoUser> updateUser(HttpServletRequest request, @RequestBody @Valid UserUpdateForm userForm) {

		Optional<User> hasUser = _userRepository.findByEmail(userForm.getEmail());

		if (hasUser.isPresent()) {
			User user = hasUser.get();
			DtoUser responseUser = new DtoUser();
			DtoUser hyperDto = new DtoUser(user.getName(),user.getEmail(),user.getPassword());
			if(userForm.getName() != null && !userForm.getName().isEmpty()) {
				user.setName(userForm.getName());
				responseUser.setName(userForm.getName());
				hyperDto.setName(userForm.getName());
			}
			if(userForm.getPassword() != null && !userForm.getPassword().isEmpty()) {
				String encryptedPassword = new BCryptPasswordEncoder().encode(userForm.getPassword());
				user.setPassword(encryptedPassword);
				responseUser.setPassword(userForm.getPassword());
				hyperDto.setPassword(encryptedPassword);
			}
			if(_hyperledgerService.updateUser(hyperDto)) {				
				_userRepository.save(user);
				return ResponseEntity.ok(responseUser);
			}
		}
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/all")
	public ResponseEntity<String> getAll() {
		String users = _hyperledgerService.getAllUsers();
		
		return ResponseEntity.ok(users);
	}
}