package cz.larpy.xcom.fieldscanner_v3.ScannerResearch;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cz.larpy.xcom.fieldscanner_v3.R;

public class ResearchAdapter extends ArrayAdapter<ResearchRecord> {

  private final Context context;
  private final int rowResourceId;
  private final ResearchRecord[] records;

  public ResearchAdapter(Context context, int resource, ResearchRecord[] objects) {
    super(context, resource, objects);
    this.context = context;
    this.records = objects;
    this.rowResourceId = resource;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View rowView = inflater.inflate(rowResourceId, parent, false);
    ImageView iw = (ImageView) rowView.findViewById(R.id.researchImageView);
    TextView tw = (TextView) rowView.findViewById(R.id.researchLabelView);

    ResearchRecord record = records[position];

    tw.setText(record.getSummary());

    String iconName = record.getTrack();

    int iconId;
    if (record.isLocked()) {
      iconId = context.getResources().getIdentifier(iconName + "_red", "drawable", context.getPackageName());

    } else {
      iconId = context.getResources().getIdentifier(iconName + "_green", "drawable", context.getPackageName());
    }
    Drawable icon = context.getResources().getDrawable(iconId);
    iw.setImageDrawable(icon);
    return rowView;
  }
}
