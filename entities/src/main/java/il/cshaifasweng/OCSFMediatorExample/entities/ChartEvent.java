package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class ChartEvent implements Serializable {



    private List<ChartInput> chartInputs;

    public ChartEvent(List<ChartInput> chartInputs) {
        this.chartInputs = chartInputs;
    }

    public List<ChartInput> getChartInputs() {
        return chartInputs;
    }

    public void setChartInputs(List<ChartInput> chartInputs) {
        this.chartInputs = chartInputs;
    }
}
