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


    @Column(length = 50, name = "email")
    @NonNull
    @Id
    String email;

    @Column(length = 50, name = "name")
    @NonNull
    String name;

    @Column(length = 50, name = "password")
    @NonNull
    String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "student_courses", joinColumns = @JoinColumn(name = "student_email"), inverseJoinColumns = @JoinColumn(name = "courses_id"))
    List<Course> courses = new ArrayList<>();



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
