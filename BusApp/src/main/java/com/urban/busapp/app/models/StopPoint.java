package com.urban.busapp.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;


public class StopPoint implements Parcelable {
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

    protected StopPoint(Parcel in) {
        point = (LatLng) in.readValue(LatLng.class.getClassLoader());
        segId = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(point);
        if (segId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(segId);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StopPoint> CREATOR = new Parcelable.Creator<StopPoint>() {
        @Override
        public StopPoint createFromParcel(Parcel in) {
            return new StopPoint(in);
        }

        @Override
        public StopPoint[] newArray(int size) {
            return new StopPoint[size];
        }
    };
}
