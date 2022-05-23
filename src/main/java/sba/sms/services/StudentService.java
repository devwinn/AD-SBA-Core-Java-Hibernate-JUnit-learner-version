package sba.sms.services;

import jakarta.persistence.TypedQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;
import java.util.ArrayList;
import java.util.List;

public class StudentService implements StudentI {

    @Override
    public List<Student> getAllStudents() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = null;
        List<Student> list = new ArrayList<>();
        try {
            t = s.beginTransaction();
            Query<Student> q = s.createQuery("from Student",Student.class);
            list = q.getResultList();
            t.commit();
        } catch (HibernateException ex) {
            if(t!=null) t.rollback();
            ex.printStackTrace();
        } finally {
            s.close();
        }
        return list;
    }

    @Override
    public void createStudent(Student student) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = null;

        try {
            t = s.beginTransaction();
            s.persist(student);
            t.commit();
        } catch (HibernateException ex) {
            if(t!=null) t.rollback();
            ex.printStackTrace();
        } finally {
            s.close();
        }

    }

    @Override
    public Student getStudentByEmail(String email) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = null;
        Student student = null;

        try {
            t = s.beginTransaction();
            TypedQuery <Student>  q = s.createQuery("from Student where email = :email", Student.class);
            q.setParameter("email", email);
            student = q.getSingleResult();
            t.commit();
        } catch (HibernateException ex) {
            if(t!=null) t.rollback();
            ex.printStackTrace();
        } finally {
            s.close();
        }
        return student;
    }

    @Override
    public boolean validateStudent(String email, String password) {

        if (getStudentByEmail(email).getPassword().equals(password))
            return true;
        else
            return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Student student = getStudentByEmail(email);
        CourseService service = new CourseService();
        Course course = service.getCourseById(courseId);

            Session s = HibernateUtil.getSessionFactory().openSession();
            Transaction t = s.beginTransaction();
            try {
                student.addCourse(course);
                s.merge(student);
                t.commit();
            } catch(HibernateException ex) {
                if(t != null) {
                    t.rollback();
                    ex.printStackTrace();
                }
            } finally {
                s.close();
            }
    }

    @Override
    public List<Course> getStudentCourses(String email) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = null;
        List<Course> courses = new ArrayList<>();

        try {
            t = s.beginTransaction();
            NativeQuery<Course> query = s.createNativeQuery("select c.id, c.name, c.instructor from student as s " +
                    "join student_courses as sc on s.email = sc.student_email " +
                    "join course as c ON sc.courses_id = c.id where s.email = :email", Course.class);
            query.setParameter("email", email);
            courses = query.getResultList();
            t.commit();
        } catch(HibernateException ex) {
            if(t != null) {
                t.rollback();
                ex.printStackTrace();
            }
        } finally {
            s.close();
        }
        return courses;
    }
}
