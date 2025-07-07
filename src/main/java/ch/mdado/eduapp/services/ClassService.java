package ch.mdado.eduapp.services;

import ch.mdado.eduapp.models.Class;
import ch.mdado.eduapp.repositories.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Transactional(readOnly = true)
    public List<Class> findAll() {
        List<Class> classes = classRepository.findAll();
        // Force loading of students
        classes.forEach(c -> c.getStudents().size());
        return classes;
    }

    @Transactional(readOnly = true)
    public Class findById(Integer id) {
        Class classEntity = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + id));

        // Force loading of students and teacher
        classEntity.getStudents().size();
        classEntity.getTeacher().getName();

        return classEntity;
    }

    @Transactional(readOnly = true)
    public List<Class> findByTeacherId(Integer teacherId) {
        List<Class> classes = classRepository.findByTeacherId(teacherId);
        classes.forEach(c -> c.getStudents().size());
        return classes;
    }

    @Transactional(readOnly = true)
    public List<Class> findByStudentId(Integer studentId) {
        List<Class> classes = classRepository.findClassesByStudentId(studentId);
        classes.forEach(c -> c.getStudents().size());
        return classes;
    }

    @Transactional(readOnly = true)
    public List<Class> findBySubjectName(String subjectName) {
        List<Class> classes = classRepository.findBySubjectName(subjectName);
        classes.forEach(c -> c.getStudents().size());
        return classes;
    }

    @Transactional(readOnly = true)
    public List<Class> findActiveClasses() {
        List<Class> classes = classRepository.findByIsActiveTrue();
        classes.forEach(c -> c.getStudents().size());
        return classes;
    }

    @Transactional(readOnly = true)
    public List<Class> findByDayOfWeek(Integer dayOfWeek) {
        List<Class> classes = classRepository.findByDayOfWeek(dayOfWeek);
        classes.forEach(c -> c.getStudents().size());
        return classes;
    }

    public Class save(Class classEntity) {
        return classRepository.save(classEntity);
    }

    public Class update(Class classEntity) {
        Class existingClass = findById(classEntity.getId());
        updateData(existingClass, classEntity);
        return classRepository.save(existingClass);
    }

    private void updateData(Class existingClass, Class newClass) {
        if (newClass.getClassName() != null) {
            existingClass.setClassName(newClass.getClassName());
        }
        if (newClass.getSubjectName() != null) {
            existingClass.setSubjectName(newClass.getSubjectName());
        }
        if (newClass.getTeacher() != null) {
            existingClass.setTeacher(newClass.getTeacher());
        }
        if (newClass.getStartTime() != null) {
            existingClass.setStartTime(newClass.getStartTime());
        }
        if (newClass.getEndTime() != null) {
            existingClass.setEndTime(newClass.getEndTime());
        }
        if (newClass.getDayOfWeek() != null) {
            existingClass.setDayOfWeek(newClass.getDayOfWeek());
        }
        if (newClass.getRoomNumber() != null) {
            existingClass.setRoomNumber(newClass.getRoomNumber());
        }
        if (newClass.getMaxStudents() != null) {
            existingClass.setMaxStudents(newClass.getMaxStudents());
        }
        if (newClass.getIsActive() != null) {
            existingClass.setIsActive(newClass.getIsActive());
        }
    }

    public void delete(Integer id) {
        if (!classRepository.existsById(id)) {
            throw new RuntimeException("Class not found with ID: " + id);
        }
        classRepository.deleteById(id);
    }
}