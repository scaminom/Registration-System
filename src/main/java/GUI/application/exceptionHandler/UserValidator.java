package GUI.application.exceptionHandler;

import com.scrum.registrationsystem.entities.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserValidator {

	private User user;

	public UserValidator(User user) {
		this.user = user;
	}

	public Map<String, List<String>> validate() {
		Map<String, List<String>> errors = new HashMap<>();

		errors.put("firstName", new ArrayList<>());
		errors.put("lastName", new ArrayList<>());
		errors.put("username", new ArrayList<>());
		errors.put("password", new ArrayList<>());
		errors.put("email", new ArrayList<>());
		errors.put("gender", new ArrayList<>());
		errors.put("role", new ArrayList<>());

		if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
			errors.get("firstName").add("El nombre es requerido.");
		}
		if (user.getLastName() == null || user.getLastName().isEmpty()) {
			errors.get("lastName").add("El apellido es requerido.");
		}

		if (user.getUsername() == null || user.getUsername().isEmpty()) {
			errors.get("username").add("El nombre de usuario es requerido.");
		}

		if (containsSQLInjectionRisk(user.getUsername())) {
			errors.get("username").add("El nombre de usuario contiene caracteres inválidos.");
		}

		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			errors.get("password").add("La contraseña es requerida.");
		}

		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			errors.get("email").add("El email es requerido.");
		}

		if (!isValidEmail(user.getEmail())) {
			errors.get("email").add("El formato del email no es válido.");
		}

		if (user.getGender() == null) {
			errors.get("gender").add("El género es requerido.");
		}

		if (user.getRole() == null) {
			errors.get("role").add("El rol es requerido.");
		}

		errors.entrySet().removeIf(entry -> entry.getValue().isEmpty());

		return errors;
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return email.matches(emailRegex);
	}

	private boolean isStrongPassword(String password) {
		String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
		return password.matches(passwordRegex);
	}

	private boolean containsSQLInjectionRisk(String input) {
		String[] riskyCharacters = { ";", "'", "--", "/*", "*/", "@@", "@", "char", "nchar", "varchar", "nvarchar",
				"alter", "begin", "cast", "create", "cursor", "declare", "delete", "drop", "end", "exec", "execute",
				"fetch", "insert", "kill", "select", "sys", "sysobjects", "syscolumns", "table", "update" };
		return Arrays.stream(riskyCharacters).anyMatch(input::contains);
	}
}
