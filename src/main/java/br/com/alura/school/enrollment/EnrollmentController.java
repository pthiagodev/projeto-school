package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;

@RestController
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public EnrollmentController(EnrollmentRepository enrollmentRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping(value = "courses/enroll/report")
    ResponseEntity<List<EnrollmentResponse>> enrollmentReport() {

        List<User> enrolledUsers = userRepository.findAll()
                .stream().filter(user -> !user.getEnrollments().isEmpty())
                .sorted(Comparator.comparing( obj -> obj.getEnrollments().size(), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        if (enrolledUsers.isEmpty())
            throw new ResponseStatusException((NO_CONTENT), format("No enrolled users found"));

        List<EnrollmentResponse> enrolledUsersReport = enrolledUsers.stream().map(EnrollmentResponse::new).collect(Collectors.toList());

        return ResponseEntity.ok(enrolledUsersReport);
    }

    @PostMapping(value = "/courses/{courseCode}/enroll")
    ResponseEntity<Void> newEnrollment(@RequestBody @Valid NewEnrollmentRequest newEnrollmentRequest, @PathVariable("courseCode") String courseCode) {

        User user = userRepository.findByUsername(newEnrollmentRequest.getUsername()).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User %s not found", newEnrollmentRequest.getUsername())));
        Course course = courseRepository.findByCode(courseCode).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", courseCode)));

        if (enrollmentRepository.findEnrollmentByUserAndCourse(user, course).isPresent()){
            return ResponseEntity.status(BAD_REQUEST).build();
        }

        enrollmentRepository.save(newEnrollmentRequest.toEntity(user, course));
        return ResponseEntity.status(CREATED).build();
    }

}
