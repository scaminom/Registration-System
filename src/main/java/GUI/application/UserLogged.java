/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.application;

/**
 *
 * @author patri
 */
public class UserLogged {

    private static UserLogged userLogged;
    private static String username;
    private static String role;

    private UserLogged() {
    }

    public static UserLogged getInstance() {
        if (userLogged == null) {
            userLogged = new UserLogged();
        }
        return userLogged;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserLogged.username = username;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        UserLogged.role = role;
    }
    
}
