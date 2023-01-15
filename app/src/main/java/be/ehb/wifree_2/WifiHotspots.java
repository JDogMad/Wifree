package be.ehb.wifree_2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WifiHotspots {
    private int nhits;
    private Parameters parameters;
    private List<WifiHotspot> records;

    public WifiHotspots() {}

    public int getNhits() {
        return nhits;
    }

    public void setNhits(int nhits) {
        this.nhits = nhits;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public List<WifiHotspot> getRecords() {
        return records;
    }

    public void setRecords(List<WifiHotspot> records) {
        this.records = records;
    }

    public static class Parameters {
        private String dataset;
        private int rows;
        private int start;
        private String[] facet;
        private String format;
        private String timezone;

        public Parameters() {}

        public String getDataset() {
            return dataset;
        }

        public void setDataset(String dataset) {
            this.dataset = dataset;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public String[] getFacet() {
            return facet;
        }

        public void setFacet(String[] facet) {
            this.facet = facet;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }
    }
}

