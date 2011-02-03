package modules.registration;

import java.util.HashMap;
import java.util.Map;
import play.Play;
import play.mvc.Mailer;
import play.mvc.Router;

/**
 *
 * @author steven
 */
public class RegistrationEmail extends Mailer {

    public static void confirmation(User user) {
        String sender =
            Play.configuration.getProperty("registration.from_email",
                "noreply@example.com");
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("code", user.confirmationCode());
        String confirmation_link = Router.getFullUrl("Registration.confirm", args);
        setSubject("Registration Confirmation");
        addRecipient(user.email);
        setFrom(sender);
        send("Registration/confirmation", confirmation_link);
   }

}
