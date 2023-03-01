package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class EnrollmentControllerTest {


    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void should_enroll_a_user() throws Exception {
        User ana = new User("ana", "ana@email.com");
        userRepository.save(ana);

        Course cursoJava = new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.");
        courseRepository.save(cursoJava);

        mockMvc.perform(post("/courses/java-1/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString("ana")))
                .andExpect(status().isCreated());
    }

    @Test
    void should_not_allow_duplication_of_enrollment() throws Exception {
        User user = new User("ana", "ana@email.com");
        Course course = new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.");
        userRepository.save(user);
        courseRepository.save(course);
        enrollmentRepository.save(new Enrollment(user, course));

        mockMvc.perform(post("/courses/java-1/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString("ana")))
                .andExpect(status().isBadRequest());

    }

}
