package ro.trenulmeu.mobile.fragments.details;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;
import ro.trenulmeu.mobile.fragments.ScrollEnabledMapFragment;
import ro.trenulmeu.mobile.models.Train;
import ro.trenulmeu.mobile.models.TrainPath;

/**
 * Mini-Fragment displaying Train Map.
 */
public class TrainMapFragment extends Fragment {

    public static final String visibleArgKey = "visibleArgKey";

    private View content;
    private Train train;
    private View mapView;
    private ScrollEnabledMapFragment mapFragment;
    private GoogleMap gMap;
    private LatLngBounds.Builder boundsBuilder;
    private ViewParent viewParent = null;

    public static TrainMapFragment newInstance(boolean visible) {
        TrainMapFragment fragment = new TrainMapFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(visibleArgKey, visible);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        boolean visible = savedInstanceState == null
                || savedInstanceState.getBoolean(visibleArgKey, true);
        train = AppContext.selectedTrain;

        final View view = inflater.inflate(R.layout.fragment_train_map, container, false);
        mapView = view.findViewById(R.id.map);
        mapFragment = ((ScrollEnabledMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

        WindowManager wm = (WindowManager) AppContext.activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = Math.max(size.y / 2, 300);
        mapView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        final ImageView toggle = (ImageView) view.findViewById(R.id.toggle);
        content = view.findViewById(R.id.content);
        view.findViewById(R.id.header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean visible = content.getVisibility() == View.VISIBLE;

                content.setVisibility(visible ? View.GONE : View.VISIBLE);
                toggle.setImageResource(visible ? R.drawable.expand : R.drawable.fold);

                bound();
            }
        });

        content.setVisibility(visible ? View.VISIBLE : View.GONE);
        toggle.setImageResource(visible ? R.drawable.fold : R.drawable.expand);

        setScrollParent();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                draw();
            }
        });

        return view;
    }

    public void setScrollParent(final ViewParent mScrollView) {
        viewParent = mScrollView;
        setScrollParent();
    }

    private void setScrollParent() {
        if (mapFragment != null && viewParent != null) {
            mapFragment.setListener(new ScrollEnabledMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    viewParent.requestDisallowInterceptTouchEvent(true);
                }
            });
        }
    }

    private void draw() {
        boundsBuilder = new LatLngBounds.Builder();
        PolylineOptions line = new PolylineOptions ();
        List<TrainPath> items = train.getStops();
        int limit = items.size();
        for (int i = 0; i < limit; i++) {
            TrainPath p = items.get(i);
            LatLng ll = new LatLng(p.getStation().getLat(), p.getStation().getLon());
            line.add(ll);
            boundsBuilder.include(ll);

            MarkerOptions m = new MarkerOptions ();
            m.anchor(0.5f, 0.5f);
            m.title(p.getStation().getName());
            m.snippet(p.getArrive() + " -> " + p.getDepart());
            m.position(ll);
            m.icon(BitmapDescriptorFactory.fromResource(i == 0
                    ? R.drawable.point_start : i == limit - 1
                    ? R.drawable.point_stop
                    : R.drawable.point_int));
            gMap.addMarker(m);
        }
        line.width(10);
        line.color(ContextCompat.getColor(AppContext.activity, R.color.map_line));
        gMap.addPolyline(line);
        bound();

/*
        foreach (MapPoint p in DbData.GetStopPoints(Train.Id, true)) {
            MarkerOptions m = new MarkerOptions ();
            m.Anchor (0.5f, 0.5f);
            m.SetTitle (p.StationName);
            m.SetSnippet (p.InTime.ToString (@"hh\:mm") + " -> " + p.OutTime.ToString (@"hh\:mm"));
            m.SetPosition (ConvertLatLon(p.StationPoint));
            m.InvokeIcon (BitmapDescriptorFactory.FromResource(Resource.Drawable.PointInterm));

            StopsMarkerOptions.Add (m);
            StopsMarkers.Add(gMap.AddMarker (m));
        }

        MarkerOptions fromM = new MarkerOptions ();
        fromM.Anchor (0.5f, 0.5f);
        fromM.SetTitle (pathPoints.First ().StationName);
        fromM.SetSnippet (pathPoints.First ().InTime.ToString (@"hh\:mm"));
        fromM.SetPosition (ConvertLatLon(pathPoints.First ().StationPoint));
        fromM.InvokeIcon (BitmapDescriptorFactory.FromResource(Resource.Drawable.PointStart));
        gMap.AddMarker (fromM);

        MarkerOptions toM = new MarkerOptions ();
        toM.Anchor (0.5f, 0.5f);
        toM.SetTitle (pathPoints.Last ().StationName);
        toM.SetSnippet (pathPoints.Last ().InTime.ToString (@"hh\:mm"));
        toM.SetPosition (ConvertLatLon(pathPoints.Last ().StationPoint));
        toM.InvokeIcon (BitmapDescriptorFactory.FromResource(Resource.Drawable.PointStop));
        gMap.AddMarker (toM);

        MarkerOptions crtM = new MarkerOptions ();
        crtM.SetPosition(new LatLng (0, 0));
        posM = gMap.AddMarker (crtM);

        gMap.InfoWindowClick += (sender, e) => {
            Toast.MakeText(context, e.Marker.Title, ToastLength.Long);
        };
        */
    }

    private void bound() {
        if (content == null || boundsBuilder == null) {
            return;
        }

        if (content.getVisibility() == View.VISIBLE) {
            final LatLngBounds bounds = boundsBuilder.build();
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            AppContext.activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40));
                                }
                            });
                        }
                    },
                    500
            );
        }
    }

    public boolean isContentVisible() {
        return content.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(visibleArgKey, isContentVisible());
        super.onSaveInstanceState(outState);
    }

}
