package org.wgu.termtracker.data;

import org.wgu.termtracker.enums.AssessmentTypeEnum;
import org.wgu.termtracker.enums.NoteTypeEnum;

import java.util.Date;

interface NoteContract {
    public long createCourseNote(long courseId, NoteTypeEnum type, String text, String photoUri);

    public long createAssessmentNote(long assessmentId, NoteTypeEnum type, String text,
                                     String photoUri);

    public boolean updateNote(long noteId, NoteTypeEnum type, String text, String photoUri);

    public boolean deleteCourseNote(long noteId);

    public boolean deleteAssessmentNote(long noteId);
}
