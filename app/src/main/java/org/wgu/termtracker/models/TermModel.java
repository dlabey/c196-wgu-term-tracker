package org.wgu.termtracker.models;

import android.icu.text.SimpleDateFormat;

import org.wgu.termtracker.Constants;

import java.io.Serializable;
import java.util.Date;

public class TermModel implements Serializable {
    private long termId;

    private String title;

    private Date startDate;

    private Date endDate;

    public long getTermId() {
        return termId;
    }

    public void setTermId(long termId) {
        this.termId = termId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        String startDate = simpleDateFormat.format(this.startDate);
        String endDate = simpleDateFormat.format(this.endDate);

        return String.format("%s%n%s - %s", title, startDate, endDate);
    }
}
