package controllers;

import play.data.validation.Validation;
import play.mvc.*;
import java.util.*;
import modules.registration.RegistrationEmail;
import modules.registration.User;
import org.apache.commons.codec.binary.Base64;
import play.data.validation.Email;
import play.data.validation.Required;

/**
 * Simple, extensible, registration module for Play Framework
 *
 * @author Steven Osborn <osborn.steven@gmail.com>
 */
public class Registration extends Controller {

    private static byte[] asByteArray(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] buffer = new byte[16];

        for (int i = 0; i < 8; i++) {
            buffer[i] = (byte) (msb >>> 8 * (7 - i));
        }
        for (int i = 8; i < 16; i++) {
            buffer[i] = (byte) (lsb >>> 8 * (7 - i));
        }
        return buffer;
    }

    public static String shortUUID() {
        UUID uuid = UUID.randomUUID();
        return Base64.encodeBase64URLSafeString(Registration.asByteArray(uuid));
    }

    private static boolean authenticate() {
        return true;
    }

    public static void register() {
        render();
    }

    /**
     *
     * @param email
     * @param password
     * @param confirm
     */
    public static void registerFinish(@Required @Email String email, 
            @Required String password, @Required String confirm) {

        if(Validation.hasErrors()) {
            flash.error("registration.error");
            Validation.keep();
            params.flash("email");
            register();
        } else {
            // Valid Registration
            User user = new User(email, password);
            try {
                user.save();
                RegistrationEmail.confirmation(user);
                pending();
            } catch (Exception e) {
                // User already exists
                flash.error("registration.user_exists");
                register();
            }
        }
    }

    public static void pending() {
        render();
    }

    public static void confirm(String code) {
        System.out.println(code);
        login();
    }

    public static void password() {
        render();
    }

    public static void recover() {
        render();
    }

    public static void logout() {
        session.clear();
        flash.success("secure.logout");
        login();
    }

    public static void login() {
        render();
    }

    public static void auth(@Required String username, String password) {
        if(Validation.hasErrors()) {
            flash.keep("url");
            flash.error("registration.auth_error");
            params.flash();
            login();
        }
        // Mark user as connected
        session.put("username", username);
        login();
    }
}
