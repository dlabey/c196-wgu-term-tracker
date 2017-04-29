package org.wgu.termtracker.data;

import android.database.SQLException;

import java.util.Date;

public interface TermContract extends DBContract {
    public long createTerm(String title, Date startDate, Date endDate);

    public boolean updateTerm(long termId, String title, Date startDate, Date endDate);

    public boolean deleteTerm(long termId);
}
