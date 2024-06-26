package com.example.kosandra.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entity class representing a hairstyle visit.
 * <p>
 * The entity is used to store information about a client's visit for a hairstyle service.
 * <p>
 * The class implements Parcelable for object serialization.
 * <p>
 * The entity is associated with the Client class through a foreign key constraint.
 */
@Entity(tableName = "hairstyleVisit",
        foreignKeys = @ForeignKey(entity = Client.class,
                parentColumns = "id",
                childColumns = "visitId",
                onDelete = ForeignKey.CASCADE))
public class HairstyleVisit implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private LocalDate visitDate;
    private byte[] photoHairstyle;
    private String haircutName;
    private int visitId;
    private int haircutCost;
    private int materialCost;
    private LocalTime timeSpent;
    private int materialWeight;
    private String[] codeMaterial;
    private int[] countMaterial;

    public HairstyleVisit(LocalDate visitDate, byte[] photoHairstyle, String haircutName, int visitId,
                          int haircutCost, int materialCost, LocalTime timeSpent,
                          int materialWeight, String[] codeMaterial, int[] countMaterial) {
        this.visitDate = visitDate;
        this.photoHairstyle = photoHairstyle;
        this.haircutName = haircutName;
        this.visitId = visitId;
        this.haircutCost = haircutCost;
        this.materialCost = materialCost;
        this.timeSpent = timeSpent;
        this.materialWeight = materialWeight;
        this.codeMaterial = codeMaterial;
        this.countMaterial = countMaterial;
    }

    public HairstyleVisit(Parcel parcel) {
        this.id = parcel.readInt();
        this.visitDate = LocalDate.ofEpochDay(parcel.readLong());
        this.photoHairstyle = parcel.createByteArray();
        this.haircutName = parcel.readString();
        this.visitId = parcel.readInt();
        this.haircutCost = parcel.readInt();
        this.materialCost = parcel.readInt();
        this.timeSpent = LocalTime.ofSecondOfDay(parcel.readLong());
        this.materialWeight = parcel.readInt();
        this.codeMaterial = parcel.createStringArray();
        this.countMaterial = parcel.createIntArray();
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPhotoHairstyle() {
        return photoHairstyle;
    }

    public void setPhotoHairstyle(byte[] photoHairstyle) {
        this.photoHairstyle = photoHairstyle;
    }

    public String getHaircutName() {
        return haircutName;
    }

    public void setHaircutName(String haircutName) {
        this.haircutName = haircutName;
    }

    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    public int getHaircutCost() {
        return haircutCost;
    }

    public void setHaircutCost(int haircutCost) {
        this.haircutCost = haircutCost;
    }

    public int getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(int materialCost) {
        this.materialCost = materialCost;
    }

    public LocalTime getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(LocalTime timeSpent) {
        this.timeSpent = timeSpent;
    }

    public int getMaterialWeight() {
        return materialWeight;
    }

    public void setMaterialWeight(int materialWeight) {
        this.materialWeight = materialWeight;
    }

    public String[] getCodeMaterial() {
        return codeMaterial;
    }

    public void setCodeMaterial(String[] codeMaterial) {
        this.codeMaterial = codeMaterial;
    }

    public int[] getCountMaterial() {
        return countMaterial;
    }

    public void setCountMaterial(int[] countMaterial) {
        this.countMaterial = countMaterial;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(visitDate.toString());
        dest.writeByteArray(photoHairstyle);
        dest.writeString(haircutName);
        dest.writeInt(visitId);
        dest.writeInt(haircutCost);
        dest.writeInt(materialCost);
        dest.writeString(timeSpent.toString());
        dest.writeInt(materialWeight);
        dest.writeStringArray(codeMaterial);
        dest.writeIntArray(countMaterial);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HairstyleVisit> CREATOR = new Creator<HairstyleVisit>() {
        @Override
        public HairstyleVisit createFromParcel(Parcel in) {
            return new HairstyleVisit(in);
        }

        @Override
        public HairstyleVisit[] newArray(int size) {
            return new HairstyleVisit[size];
        }
    };
}
