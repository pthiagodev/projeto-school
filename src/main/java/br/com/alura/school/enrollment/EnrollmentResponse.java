package br.com.alura.school.enrollment;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class EnrollmentResponse {

    @NotBlank
    @Email
    private final String email;

    @NotNull
    @Positive
    private final Long enrollments;

    EnrollmentResponse(String email, Long enrollments) {
        this.email = email;
        this.enrollments = enrollments;
    }
}
