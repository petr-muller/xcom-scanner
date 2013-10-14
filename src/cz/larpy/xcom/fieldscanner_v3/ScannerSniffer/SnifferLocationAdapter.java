package cz.larpy.xcom.fieldscanner_v3.ScannerSniffer;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import cz.larpy.xcom.fieldscanner_v3.R;

public class SnifferLocationAdapter extends ArrayAdapter<SnifferLocation> {
  private final Context context;
  private final int rowResourceId;
  private final List<SnifferLocation> locations;

  public SnifferLocationAdapter(Context pContext, int pResource, List<SnifferLocation> pObjects) {
    super(pContext, pResource, pObjects);
    context = pContext;
    rowResourceId = pResource;
    locations = pObjects;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(rowResourceId, parent, false);

    SnifferLocation location = locations.get(position);
    TextView snifferSummaryTw = (TextView) rowView.findViewById(R.id.snifferItemSummary);
    TextView snifferSignalTw = (TextView) rowView.findViewById(R.id.snifferItemSignalStrength);
    TextView snifferRemainsTw = (TextView) rowView.findViewById(R.id.snifferItemRemains);
    TextView snifferDistanceTw = (TextView) rowView.findViewById(R.id.snifferDistance);
    TextView snifferTimeRatioTw = (TextView) rowView.findViewById(R.id.snifferTimeModifier);
    ProgressBar snifferProgress = (ProgressBar) rowView.findViewById(R.id.snifferItemProgress);

    snifferSummaryTw.setText(location.getSummary());
    snifferProgress.setMax(location.getNeeded());
    snifferProgress.setProgress(location.getSniffed());

    Double signalStrength = location.getSignalStrength();
    if (signalStrength == null) {
      snifferSignalTw.setText("0");
      snifferRemainsTw.setText("N/A");
      snifferDistanceTw.setText("N/A");
      snifferTimeRatioTw.setText("N/A");
    } else {
      snifferSignalTw.setText(signalStrength.toString());
      int remains = (location.getNeeded() - location.getSniffed()) / signalStrength.intValue();
      snifferRemainsTw.setText(String.valueOf(remains));
      snifferDistanceTw.setText(String.valueOf(location.getDistance()) + "/" + String.valueOf(location.getDistanceR()));
      snifferTimeRatioTw.setText(String.valueOf(location.getTimeR()));
    }
    return rowView;
  }
}
