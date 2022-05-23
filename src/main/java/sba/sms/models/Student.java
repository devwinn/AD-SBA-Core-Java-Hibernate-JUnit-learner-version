package sba.sms.models;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Log
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "student")
@Entity
public class Student {


    @Id
    @NonNull
    @Column(length = 50, nullable = false, name = "email")
    String email;

    @NonNull
    @Column(length = 50, nullable = false,name = "name")
    String name;

    @NonNull
    @Column(length = 50, nullable = false, name = "password")
    String password;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(name = "student_courses", joinColumns = @JoinColumn(name = "student_email"), inverseJoinColumns = @JoinColumn(name = "courses_id"))
    List<Course> courses = new ArrayList<>();

    public void addCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return email.equals(student.email) && name.equals(student.name) && password.equals(student.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password);
    }
}
