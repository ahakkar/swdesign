package fi.nordicwatt.model.session;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * ID and name of a tab, possibly something else too later?
 * 
 * @author Antti Hakkarainen
 */
public class TabInfo {
    private final String uuid;
    private String title;
    private int storedChartCount = 0;
    private Set<String> storedCharts;

    public TabInfo(String title) {
        this.uuid = UUID.randomUUID().toString();
        this.title = title;
        storedCharts = new HashSet<>();
    }
    // getters and setters


    public String getId() {
        return uuid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getStoredChartCount() {
        return storedChartCount;
    }

    public Set<String> getStoredCharts() {
        return storedCharts;
    }

    public void incrementStoredChartCount() {
        storedChartCount++;
    }

    public void addChart(String chartId) {
        storedCharts.add(chartId);
    }

    public Boolean removeChart(String chartId) {
        return storedCharts.remove(chartId);
    }
}
