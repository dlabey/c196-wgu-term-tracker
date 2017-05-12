package org.wgu.termtracker.data;

import org.wgu.termtracker.enums.AssessmentTypeEnum;
import org.wgu.termtracker.enums.NoteTypeEnum;
import org.wgu.termtracker.models.CourseMentorModel;
import org.wgu.termtracker.models.NoteModel;

import java.util.Date;
import java.util.List;

interface NoteContract {
    public long createCourseNote(long courseId, NoteTypeEnum type, String text, String photoUri);

    public long createAssessmentNote(long assessmentId, NoteTypeEnum type, String text,
                                     String photoUri);

    public boolean updateNote(long noteId, NoteTypeEnum type, String text, String photoUri);

    public boolean deleteCourseNote(long noteId);

    public boolean deleteAssessmentNote(long noteId);

    public List<NoteModel> listCourseNotes(long courseId);

    public List<NoteModel> listAssessmentNotes(long assessmentId);
}
