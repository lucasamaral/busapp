package com.urban.busapp.app.models;


import android.os.Parcel;
import android.os.Parcelable;

public class SegmentTime implements Parcelable {

    private Integer timeEstimated;

    public SegmentTime(Integer timeEstimated) {
        this.timeEstimated = timeEstimated;
    }

    public Integer getTimeEstimated() {
        return timeEstimated;
    }

    protected SegmentTime(Parcel in) {
        timeEstimated = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public String toString() {
        return "SegmentTime{" +
                "timeEstimated=" + timeEstimated +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (timeEstimated == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(timeEstimated);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SegmentTime> CREATOR = new Parcelable.Creator<SegmentTime>() {
        @Override
        public SegmentTime createFromParcel(Parcel in) {
            return new SegmentTime(in);
        }

        @Override
        public SegmentTime[] newArray(int size) {
            return new SegmentTime[size];
        }
    };
}
