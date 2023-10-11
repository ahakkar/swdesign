package org.example.model.session;

import java.util.List;

import org.example.model.data.ChartRequest;
import org.example.model.data.DataResult;
import org.example.types.SessionChangeType;

/**
 * Contains data about session change. What changed, what are the new values.
 * For eample UI needs them to update the view correctly.
 * 
 * @author Antti Hakkarainen
 */
public class SessionChangeData {
    private final SessionChangeType type;
    private String tabId;
    private String title;
    private List<DataResult> data;
    private ChartRequest chartRequest;

    public SessionChangeData(
        SessionChangeType type
    ){
        this.type = type;
    }

    public SessionChangeData(
        SessionChangeType type, 
        String tabId,
        String title)
    {
        this.type = type;
        this.tabId = tabId;
        this.title = title;
    }

    public SessionChangeType getType() {
        return type;
    }

    public void setId(String tabId) {
        this.tabId = tabId;
    }

    public String getId() {
        return tabId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<DataResult> getData() {
        return data;
    }

    public void setData(List<DataResult> data) {
        this.data = data;
    }

    public ChartRequest getChartRequest() {
        return chartRequest;
    }

    public void setChartRequest(ChartRequest chartRequest) {
        this.chartRequest = chartRequest;
    }
}
