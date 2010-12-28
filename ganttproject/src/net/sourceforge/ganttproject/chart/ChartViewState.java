package net.sourceforge.ganttproject.chart;

import java.util.Date;

import net.sourceforge.ganttproject.Mediator;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.scrolling.ScrollingListener;
import net.sourceforge.ganttproject.gui.zoom.ZoomEvent;
import net.sourceforge.ganttproject.gui.zoom.ZoomListener;
import net.sourceforge.ganttproject.gui.zoom.ZoomManager;
import net.sourceforge.ganttproject.gui.zoom.ZoomManager.ZoomState;
import net.sourceforge.ganttproject.task.TaskLength;
import net.sourceforge.ganttproject.time.TimeUnit;

/**
 * @author bard
 */
public class ChartViewState implements ScrollingListener, ZoomListener {
    private ZoomState myCurrentZoomState;
    private UIFacade myUIFacade;

    private final TimelineChart myChart;

    public ChartViewState(TimelineChart chart, UIFacade uiFacade) {
        myChart = chart;
        myUIFacade = uiFacade;
        uiFacade.getZoomManager().addZoomListener(this);
    }

    // private void setDefaultBottomUnitWidth() {
    // myBottomUnitWidth = 20;
    // }

    public void scrollBy(TaskLength duration) {
        myChart.scrollBy(duration);
    }

    public void scrollTo(Date date) {
        myChart.setStartDate(date);
    }

    public void zoomChanged(ZoomEvent e) {
        myCurrentZoomState = e.getNewZoomState();
        Date date;
        if (myUIFacade.getViewIndex() == UIFacade.GANTT_INDEX) {
            Date d = Mediator.getTaskSelectionManager().getEarliestStart();
            // boolean zoomin = e.getZoomValue() < e.getOldValue();
            // if (zoomin && myZoomStateIndex > 0) {
            //     myZoomStateIndex--;
            // } else if (!zoomin && myZoomStateIndex < myZoomStates.length - 1) {
            //     myZoomStateIndex++;
            // }
            //
            // myCurrentTimeFrame = scrollTimeFrame(d == null ? getStartDate() : d);
            date = d == null ? myChart.getStartDate() : d;
        } else {
            date = myChart.getStartDate();
        }

        myChart.setTopUnit(getTopTimeUnit());
        myChart.setBottomUnit(getBottomTimeUnit());
        myChart.setBottomUnitWidth(getBottomUnitWidth());
        myChart.setStartDate(date == null ? new Date() : date);
    }

    public int getBottomUnitWidth() {
        return getCurrentZoomState().getBottomUnitWidth();
    }

    public TimeUnit getTopTimeUnit() {
        return getCurrentZoomState().getTimeUnitPair().getTopTimeUnit();
    }

    public TimeUnit getBottomTimeUnit() {
        return getCurrentZoomState().getTimeUnitPair().getBottomTimeUnit();
    }

    public ZoomManager.ZoomState getCurrentZoomState() {
        return myCurrentZoomState;
    }
}