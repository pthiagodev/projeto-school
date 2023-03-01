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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void should_retrieve_enrolled_users_report() throws Exception {
        User user = new User("ana", "ana@email.com");
        Course course = new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.");
        userRepository.save(user);
        courseRepository.save(course);
        enrollmentRepository.save(new Enrollment(user, course));

        mockMvc.perform(get("/courses/enroll/report")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].email", is("ana@email.com")))
                .andExpect(jsonPath("$.[0].quantidade_matriculas", is(1)));
    }

    @Test
    void should_order_by_enrollment_number_desc() throws Exception {
        User userAna = new User("ana", "ana@email.com");
        userRepository.save(userAna);
        User userAlex = new User("alex", "alex@email.com");
        userRepository.save(userAlex);

        Course course1 = new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.");
        courseRepository.save(course1);
        Course course2 = new Course("spring-1", "Spring Basics", "Spring Core and Spring MVC.");
        courseRepository.save(course2);

        enrollmentRepository.save(new Enrollment(userAna, course1));
        enrollmentRepository.save(new Enrollment(userAlex, course1));
        enrollmentRepository.save(new Enrollment(userAlex, course2));

        mockMvc.perform(get("/courses/enroll/report")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].quantidade_matriculas", is(2)))
                .andExpect(jsonPath("$.[1].quantidade_matriculas", is(1)));
    }

    @Test
    void no_content_when_no_enrolled_user_found() throws Exception {
        mockMvc.perform(get("/courses/enroll/report")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
