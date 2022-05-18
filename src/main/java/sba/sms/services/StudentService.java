package sba.sms.services;

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
        Student student = new Student();

        try {
            t = s.beginTransaction();
            NativeQuery <Student>  q = s.createNativeQuery("Select * from Student where email = :email", Student.class);
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
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = null;
        CourseService service = new CourseService();
        try {
            t = s.beginTransaction();
            Course c = service.getCourseById(courseId);
            c.addStudent(getStudentByEmail(email));
            t.commit();
        } catch(HibernateException ex) {
            if (t!=null) t.rollback();
            ex.printStackTrace();

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
            NativeQuery q = s.createNativeQuery("select co.id, co.name, co.instructor from course as co join student_courses as sco on co.id = sco.courses_id join student as st on st.email = sco.student_email where st.email = :email", Course.class);
            q.setParameter("email", email);
            courses = q.getResultList();

        } catch (HibernateException ex) {
            if (t!=null) t.rollback();
            ex.printStackTrace();
        } finally {
            s.close();
        }
        return courses;
    }
}
