package controllers;

import org.apache.commons.mail.EmailException;
import play.*;
import play.mvc.*;
import java.util.*;
import org.apache.commons.mail.SimpleEmail;
import play.data.validation.Required;
import play.libs.Mail;
import play.templates.BaseTemplate;
import play.templates.TemplateLoader;

/**
 * Registration module for Play Framework
 *
 * @author Steven Osborn <osborn.steven@gmail.com>
 */
public class Registration extends Controller {
  
    public static void register() {
        render();
    }

    private static SimpleEmail genRegEmail(String recipient) throws EmailException {

        SimpleEmail email = new SimpleEmail();

        Map<String, Object> values = new HashMap<String, Object>();
        values.put("recipient", recipient);
        String message = TemplateLoader
                .load("Registration/registration_email.txt")
                .render(values);

        String subject = TemplateLoader
                    .load("Registration/registration_email_subject.txt")
                    .render(values);

        email.setFrom("sender@zenexity.fr");
        email.setSubject(subject);
        email.setMsg(message);
        email.addTo(recipient);
        return email;
    }

    public static void registerFinish() throws EmailException {

        Mail.send(genRegEmail("osborn.steven@gmail.com"));

        System.out.println();
        register();
    }

    public static void confirm() {
        render();
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
        System.out.println("auth");
        if(validation.hasErrors()) {
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
