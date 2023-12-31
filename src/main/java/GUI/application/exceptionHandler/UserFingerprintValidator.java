/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.application.exceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scrum.registrationsystem.entities.User;

/**
 *
 * @author patri
 */
public class UserFingerprintValidator {
    private byte[] fingerprintPattern;
    private User user;

    public UserFingerprintValidator(byte[] fingerprintPattern, User user) {
        this.fingerprintPattern = fingerprintPattern;
        this.user = user;
    }

    public Map<String, List<String>> validate() {
        Map<String, List<String>> errors = new HashMap<>();

        errors.put("fingerprintPattern", new ArrayList<>());
        errors.put("user", new ArrayList<>());

        if (fingerprintPatternIsEmtpy()) {
            errors.get("fingerprintPattern").add("El patron de la huella es requerido.");
        }

        if (user == null) {
            errors.get("user").add("El usuario es requerido.");
        }

        errors.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        return errors;
    }

    public boolean fingerprintPatternIsEmtpy() {
        boolean vacio = true; // una variable para indicar si el array está vacío
        for (byte b : fingerprintPattern) {
            if (b != 0) {
                vacio = false; // si hay algún elemento distinto de cero, el array no está vacío
                break; // salimos del bucle
            }
        }
        return vacio;
    }

}
