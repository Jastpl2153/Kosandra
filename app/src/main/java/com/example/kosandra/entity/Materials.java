package com.example.kosandra.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "materials")
public class Materials implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String typeMaterials;
    private String colorMaterial;
    private String codeMaterial;
    private byte[] photo;
    private int count;
    private int cost;
    @Nullable
    private String typeKanekalon;
    @Nullable
    private String manufacturer;
    @Nullable
    private String typeCurls;
    @Nullable
    private Integer length;

    private int rating;

    public Materials(String typeMaterials, String colorMaterial, String codeMaterial, byte[] photo, int count, int cost, @Nullable String typeKanekalon, @Nullable String manufacturer, @Nullable String typeCurls, Integer length, int rating) {
        this.typeMaterials = typeMaterials;
        this.colorMaterial = colorMaterial;
        this.codeMaterial = codeMaterial;
        this.photo = photo;
        this.count = count;
        this.cost = cost;
        this.typeKanekalon = typeKanekalon;
        this.manufacturer = manufacturer;
        this.typeCurls = typeCurls;
        this.length = length;
        this.rating = rating;
    }

    public Materials(Parcel source) {
        this.id = source.readInt();
        this.typeMaterials = source.readString();
        this.colorMaterial = source.readString();
        this.codeMaterial = source.readString();
        this.photo = source.createByteArray();
        this.count = source.readInt();
        this.cost = source.readInt();
        this.typeKanekalon = source.readString();
        this.manufacturer = source.readString();
        this.typeCurls = source.readString();
        this.length = source.readInt();
        this.rating = source.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeMaterials() {
        return typeMaterials;
    }

    public void setTypeMaterials(String typeMaterials) {
        this.typeMaterials = typeMaterials;
    }

    public String getColorMaterial() {
        return colorMaterial;
    }

    public void setColorMaterial(String colorMaterial) {
        this.colorMaterial = colorMaterial;
    }

    public String getCodeMaterial() {
        return codeMaterial;
    }

    public void setCodeMaterial(String codeMaterial) {
        this.codeMaterial = codeMaterial;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Nullable
    public String getTypeKanekalon() {
        return typeKanekalon;
    }

    public void setTypeKanekalon(@Nullable String typeKanekalon) {
        this.typeKanekalon = typeKanekalon;
    }

    @Nullable
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(@Nullable String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Nullable
    public String getTypeCurls() {
        return typeCurls;
    }

    public void setTypeCurls(@Nullable String typeCurls) {
        this.typeCurls = typeCurls;
    }

    @Nullable
    public Integer getLength() {
        return length;
    }

    public void setLength(@Nullable Integer length) {
        this.length = length;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(typeMaterials);
        dest.writeString(colorMaterial);
        dest.writeString(codeMaterial);
        dest.writeByteArray(photo);
        dest.writeInt(count);
        dest.writeInt(cost);
        dest.writeString(typeKanekalon);
        dest.writeString(manufacturer);
        dest.writeString(typeCurls);
        dest.writeInt(length);
        dest.writeInt(rating);
    }

    public static final Creator<Materials> CREATOR = new Creator<Materials>() {
        @Override
        public Materials createFromParcel(Parcel source) {
            return new Materials(source);
        }

        @Override
        public Materials[] newArray(int size) {
            return new Materials[0];
        }
    };
}
