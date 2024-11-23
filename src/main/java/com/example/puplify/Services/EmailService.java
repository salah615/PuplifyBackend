package com.example.puplify.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.example.puplify.Entities.Course;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService implements EmailServiceInterface {

    @Autowired
    private JavaMailSender mailSender;



    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // true for HTML content

        mailSender.send(message);
    }



    @Override
    public void sendPasswordResetEmail(String email, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset");
        message.setText("Use the following link to reset your password: http://localhost:4200/reset-password?token=" + resetToken);
        mailSender.send(message);
    }

    @Override
    public void sendVerificationEmail(String email, String verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Verification");
        message.setText("Use the following link to verify your email: http://localhost:8888/verify-email?email=" + email + "&token=" + verificationToken);
        mailSender.send(message);
    }

    public void sendCourseCreationEmail(String to, Course course) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("New Course Announcement: " + course.getName());

        String htmlContent = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>New Course Announcement</title>
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                .header { background-color: #4a69bd; color: #ffffff; padding: 20px; text-align: center; }
                .content { background-color: #f9f9f9; padding: 20px; }
                .course-title { color: #4a69bd; }
                .button { display: inline-block; padding: 10px 20px; background-color: #4a69bd; color: #ffffff; text-decoration: none; border-radius: 5px; }
                .footer { text-align: center; margin-top: 20px; font-size: 0.8em; color: #666; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>New Course Announcement</h1>
                </div>
                <div class="content">
                    <p>Dear Student,</p>
                    <p>We are excited to announce a new course that has been added to our curriculum:</p>
                    <h2 class="course-title">%s</h2>
                    <p><strong>Course Description:</strong></p>
                    <p>%s</p>
                    <p>This course offers an excellent opportunity to expand your knowledge and skills. We encourage you to explore the course details and consider enrolling.</p>
                    <p><a href="#" class="button">View Course Details</a></p>
                    <p>If you have any questions about this course or need additional information, please don't hesitate to contact our academic support team.</p>
                    <p>Best regards,<br>The Academic Team</p>
                </div>
                <div class="footer">
                    <p>© 2024 Future University. All rights reserved.</p>
                </div>
            </div>
        </body>
        </html>
    """.formatted(course.getName(), course.getDescription());

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
