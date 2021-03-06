package de.tum.in.tumcampusapp.widgets;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.tumcampusapp.R;
import de.tum.in.tumcampusapp.auxiliary.MVVSymbolView;
import de.tum.in.tumcampusapp.managers.TransportManager;
import de.tum.in.tumcampusapp.models.efa.Departure;
import de.tum.in.tumcampusapp.models.efa.WidgetDepartures;

@SuppressLint("Registered")
public class MVVWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MVVRemoteViewFactory(this.getApplicationContext(), intent);
    }

    private class MVVRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

        private final Context applicationContext;
        private List<Departure> departures = new ArrayList<>();
        private int appWidgetID;
        private boolean forceLoadDepartures = false;

        MVVRemoteViewFactory(Context applicationContext, Intent intent) {
            this.applicationContext = applicationContext.getApplicationContext();
            this.appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            this.forceLoadDepartures = intent.getBooleanExtra(MVVWidget.MVV_WIDGET_FORCE_RELOAD, true);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            // load the departures for the widget
            TransportManager transportManager = new TransportManager(applicationContext);
            WidgetDepartures wd = transportManager.getWidget(this.appWidgetID);

            this.departures = wd.getDepartures(applicationContext, this.forceLoadDepartures);
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            if (this.departures == null) {
                return 0;
            }
            return this.departures.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(applicationContext.getPackageName(), R.layout.departure_line_widget);
            if (this.departures == null) {
                return rv;
            }

            // get the departure for this view
            Departure currentItem = this.departures.get(position);
            if (currentItem == null) {
                return null;
            }

            // Setup the line symbol
            rv.setTextViewText(R.id.line_symbol, currentItem.symbol);
            MVVSymbolView d = new MVVSymbolView(currentItem.symbol);
            rv.setTextColor(R.id.line_symbol, d.getTextColor());
            rv.setInt(R.id.line_symbol, "setBackgroundColor", d.getBackgroundColor());

            // Setup the line name and the departure time
            rv.setTextViewText(R.id.line_name, currentItem.direction);
            rv.setTextViewText(R.id.departure_time, currentItem.getCalculatedCountDown() + " min");

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}