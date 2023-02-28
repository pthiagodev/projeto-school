package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Enrollment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Course course;

    @Column(name = "date", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime dateofEnrollment;

    @Deprecated
    protected Enrollment() {}

    Enrollment(User user, Course course) {
        this.user = user;
        this.course = course;
        this.dateofEnrollment = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDateTime getDateofEnrollment() {
        return dateofEnrollment;
    }

}

