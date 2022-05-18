package sba.sms.services;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class CourseService implements CourseI {

    @Override
    public void createCourse(Course course) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = null;
        try {
            t = s.beginTransaction();
            s.persist(course);
            t.commit();
        } catch (HibernateException ex) {
            if (t!=null)
                t.rollback();
                ex.printStackTrace();
        } finally {
            s.close();
        }
    }

    @Override
    public Course getCourseById(int courseId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = null;
        Course c = new Course();
        String hql = "from Course where id = :id";

        try {
            t = s.beginTransaction();
            Query q = s.createQuery(hql, Course.class);
            q.setParameter("id", courseId);
            c = (Course) q.getSingleResult();
          } catch (HibernateException ex) {
            ex.printStackTrace();
          } finally {
            s.close();
          }
          return c;
    }

    @Override
    public List<Course> getAllCourses() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = null;
        List<Course> list = new ArrayList<>();
        String hql = "from Course";
        try {
            t = s.beginTransaction();
            Query <Course> q = s.createQuery(hql, Course.class);
            list = q.getResultList();
            t.commit();
        } catch (HibernateException ex) {
            if(t!=null) {
                t.rollback();
                ex.printStackTrace();
            }
        } finally {
            s.close();
        }
        return list;
    }
}
