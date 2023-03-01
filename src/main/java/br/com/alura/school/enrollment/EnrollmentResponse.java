package br.com.alura.school.enrollment;

import br.com.alura.school.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnrollmentResponse {

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "quantidade_matriculas")
    private int enrollments;

    EnrollmentResponse(User user) {
        this.email = user.getEmail();
        this.enrollments = user.getEnrollments().size();
    }

    public int getEnrollments() {
        return enrollments;
    }
}
