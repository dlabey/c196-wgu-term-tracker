package org.wgu.termtracker.data;

import android.database.SQLException;

import org.wgu.termtracker.models.TermModel;

import java.util.Date;
import java.util.List;

public interface TermContract extends DBContract {
    public long createTerm(String title, Date startDate, Date endDate);

    public boolean updateTerm(long termId, String title, Date startDate, Date endDate);

    public boolean deleteTerm(long termId);

    public List<TermModel> listTerms();
}
