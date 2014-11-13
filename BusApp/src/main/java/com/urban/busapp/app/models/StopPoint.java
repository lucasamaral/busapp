package com.urban.busapp.app.models;

import com.google.android.gms.maps.model.LatLng;


public class StopPoint {
    private LatLng point;
    private Integer segId;

    public StopPoint(LatLng point, Integer segId) {
        this.point = point;
        this.segId = segId;
    }

    public LatLng getPoint() {
        return point;
    }

    public Integer getSegId() {
        return segId;
    }
}
