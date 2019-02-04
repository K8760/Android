package fi.jamk.k8760.showplaces;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public InfoWindow(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.info_window, null);

        TextView name_tv = view.findViewById(R.id.name);
        TextView address_tv = view.findViewById(R.id.address);
        TextView phone_tv = view.findViewById(R.id.phone);
        TextView email_tv = view.findViewById(R.id.email);
        TextView url_tv = view.findViewById(R.id.url);

        name_tv.setText(marker.getTitle());
        address_tv.setText(marker.getSnippet());

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        phone_tv.setText(infoWindowData.getPhone());
        email_tv.setText(infoWindowData.getEmail());
        url_tv.setText(infoWindowData.getUrl());

        return view;
    }
}