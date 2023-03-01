package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.support.validation.Unique;
import br.com.alura.school.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class NewEnrollmentRequest {

    @Size(max=20)
    @NotBlank
    @JsonProperty("username")
    private String username;

    NewEnrollmentRequest(){}
    NewEnrollmentRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    Enrollment toEntity(User user, Course course) {
        return new Enrollment(user, course);
    }
}
