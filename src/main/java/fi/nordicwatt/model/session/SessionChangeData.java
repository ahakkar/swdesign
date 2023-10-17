package fi.nordicwatt.model.session;

import java.util.List;

import fi.nordicwatt.model.data.ChartRequest;
import fi.nordicwatt.model.data.DataResult;
import fi.nordicwatt.types.SessionChangeType;

/**
 * Contains data about session change. What changed, what are the new values.
 * For eample UI needs them to update the view correctly.
 * 
 * @author Antti Hakkarainen
 */
public class SessionChangeData {
    private final SessionChangeType type;
    private String tabId;
    private String chartId;
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

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public String getTabId() {
        return tabId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    public String getChartId() {
        return chartId;
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
