package com.example.kosandra.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(tableName = "records",
   foreignKeys = @ForeignKey(entity = Client.class,
        parentColumns = "id",
        childColumns = "clientId",
        onDelete = ForeignKey.CASCADE))
public class Record implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int clientId;
    private LocalDate visitDate;
    private LocalTime timeSpent;
    private String haircutName;
    private int cost;

    public Record(int clientId, LocalDate visitDate, LocalTime timeSpent, String haircutName, int cost) {
        this.clientId = clientId;
        this.visitDate = visitDate;
        this.timeSpent = timeSpent;
        this.haircutName = haircutName;
        this.cost = cost;
    }

    public Record(Parcel source){
        this.id = source.readInt();
        this.clientId = source.readInt();
        this.visitDate = LocalDate.ofEpochDay(source.readLong());
        this.timeSpent = LocalTime.ofSecondOfDay(source.readLong());
        this.haircutName = source.readString();
        this.cost = source.readInt();
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public LocalTime getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(LocalTime timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getHaircutName() {
        return haircutName;
    }

    public void setHaircutName(String haircutName) {
        this.haircutName = haircutName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(clientId);
        dest.writeLong(visitDate.toEpochDay());
        dest.writeString(timeSpent.toString());
        dest.writeString(haircutName);
        dest.writeInt(cost);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };
}
