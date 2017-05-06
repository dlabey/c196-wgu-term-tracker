package org.wgu.termtracker.data;

import org.wgu.termtracker.models.CourseMentorModel;

import java.util.List;

interface CourseMentorContract {
    public long createCourseMentor(long courseId, String name, String phoneNumber, String email);

    public boolean updateCourseMentor(long courseMentorId, String name, String phoneNumber,
                                      String email);

    public boolean deleteCourseMentor(long courseMentorId);

    public List<CourseMentorModel> listCourseMentors(long courseId);
}
