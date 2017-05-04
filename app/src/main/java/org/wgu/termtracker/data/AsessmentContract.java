package org.wgu.termtracker.data;

import org.wgu.termtracker.enums.AssessmentTypeEnum;
import org.wgu.termtracker.models.AssessmentModel;

import java.util.Date;
import java.util.List;

public interface AsessmentContract {
    public long createAssessment(long courseId, String title, Date dueDate,
                                 AssessmentTypeEnum type);

    public boolean updateAssessment(long assessmentId, String title, Date dueDate,
                                    AssessmentTypeEnum type);

    public boolean deleteAssessment(long assessmentId);

    public List<AssessmentModel> listAssessments(long courseId);
}
