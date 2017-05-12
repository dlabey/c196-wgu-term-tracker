package org.wgu.termtracker.data;

import org.wgu.termtracker.models.NoteModel;

import java.util.List;

interface NoteContract {
    public long createCourseNote(long courseId, String text, String photoUri);

    public long createAssessmentNote(long assessmentId, String text, String photoUri);

    public boolean updateNote(long noteId, String text, String photoUri);

    public boolean deleteCourseNote(long noteId);

    public boolean deleteAssessmentNote(long noteId);

    public List<NoteModel> listCourseNotes(long courseId);

    public List<NoteModel> listAssessmentNotes(long assessmentId);
}
