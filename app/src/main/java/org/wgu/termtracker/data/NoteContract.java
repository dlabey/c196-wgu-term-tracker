package org.wgu.termtracker.data;

import org.wgu.termtracker.enums.AssessmentTypeEnum;
import org.wgu.termtracker.enums.NoteTypeEnum;

import java.util.Date;

public interface NoteContract {
    public long createCourseNote(long courseId, NoteTypeEnum type, String text, String photoUri);

    public long createAssessmentNote(long assessmentId, NoteTypeEnum type, String text,
                                     String photoUri);

    public boolean updateCourseNote(long noteId, NoteTypeEnum type, String text, String photoUri);

    public boolean updateAssessmentNote(long assessmentId, NoteTypeEnum type, String text,
                                        String photoUri);

    public boolean deleteCourseNote(long noteId, long courseId);

    public boolean deleteAssessmentNote(long noteId, long assessmentId);
}
