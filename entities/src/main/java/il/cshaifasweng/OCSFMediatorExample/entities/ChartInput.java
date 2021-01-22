package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class ChartInput implements Serializable {

    String seriesName;
    int[] data;


    public ChartInput(String seriesName, int[] data) {
        this.seriesName = seriesName;
        this.data = data;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

}
