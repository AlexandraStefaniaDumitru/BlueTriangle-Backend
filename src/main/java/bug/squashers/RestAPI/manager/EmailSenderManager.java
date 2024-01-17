package bug.squashers.RestAPI.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderManager {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendEmail(String to, String subject, String body) {
        //just for test purposes
        subject = "Salut, sunt eu, Lorena";
        to = "lrngrigorescu@gmail.com";
        body = "heeeeeeeey";
        SimpleMailMessage messager = new SimpleMailMessage();
        messager.setFrom("blue.triangle0605@gmail.com");
        messager.setSubject(subject);
        messager.setText(body);
        messager.setTo(to);
        javaMailSender.send(messager);
    }


}
