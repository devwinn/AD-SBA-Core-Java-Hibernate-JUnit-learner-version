package sba.sms.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Log
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "course")
@Entity
public class Course {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(length = 50)
    @NonNull
    String name;

    @Column(length = 50)
    @NonNull
    String instructor;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "courses", cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}, fetch = FetchType.EAGER)
    List<Student> students = new ArrayList<>();


    public void addStudent(Student s) {
        students.add(s);
        s.getCourses().add(this);

    }


}
