package org.wgu.termtracker.data;

import org.wgu.termtracker.enums.CourseStatusEnum;
import org.wgu.termtracker.models.CourseModel;

import java.util.Date;
import java.util.List;

public interface CourseContract {
    public long createCourse(long termId, String title, Date startDate, Date anticipatedEndDate,
                             Date dueDate, CourseStatusEnum status);

    public boolean updateCourse(long courseId, String title, Date startDate,
                                Date anticipatedEndDate, Date dueDate, CourseStatusEnum status);

    public boolean deleteCourse(long courseId);

    public List<CourseModel> listCourses(long termId);
}
