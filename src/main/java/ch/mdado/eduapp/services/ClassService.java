package ch.mdado.eduapp.services;

import ch.mdado.eduapp.models.Class;
import ch.mdado.eduapp.repositories.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    public List<Class> findAll() {
        return classRepository.findAll();
    }

    public Class findById(Integer id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + id));
    }

    public List<Class> findByTeacherId(Integer teacherId) {
        return classRepository.findByTeacherId(teacherId);
    }

    public List<Class> findByStudentId(Integer studentId) {
        return classRepository.findClassesByStudentId(studentId);
    }

    public List<Class> findBySubjectName(String subjectName) {
        return classRepository.findBySubjectName(subjectName);
    }

    public List<Class> findActiveClasses() {
        return classRepository.findByIsActiveTrue();
    }

    public List<Class> findByDayOfWeek(Integer dayOfWeek) {
        return classRepository.findByDayOfWeek(dayOfWeek);
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