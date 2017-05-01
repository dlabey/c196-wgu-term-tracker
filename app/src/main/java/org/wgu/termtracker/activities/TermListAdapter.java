package org.wgu.termtracker.activities;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class TermListAdapter extends ArrayAdapter {
    public TermListAdapter(Context context, int resource, List list) {
        super(context, resource, list);
    }
}
