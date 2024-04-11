package com.example.kosandra.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

/**
 * The Client class represents a client with various attributes such as photo, name, date of birth, phone number,
 * <p>
 * number of visits, hair length, hair color, hair density, and conversation details.
 * <p>
 * This class implements the Parcelable interface for serialization and deserialization.
 */
@Entity(tableName = "client")
public class Client implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private byte[] photo;
    private String name;
    private LocalDate dateOfBirth;
    private String numberPhone;
    private int numberOfVisits;
    private int hairLength;
    @Nullable
    private String hairColor;
    @Nullable
    private String hairDensity;
    @Nullable
    private String conversationDetails;

    public Client(byte[] photo, String name, LocalDate dateOfBirth, String numberPhone,
                  int numberOfVisits, int hairLength, @Nullable String hairColor,
                  @Nullable String hairDensity, @Nullable String conversationDetails) {
        this.photo = photo;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.numberPhone = numberPhone;
        this.numberOfVisits = numberOfVisits;
        this.hairLength = hairLength;
        this.hairColor = hairColor;
        this.hairDensity = hairDensity;
        this.conversationDetails = conversationDetails;
    }

    public Client(Parcel source) {
        this.id = source.readInt();
        this.photo = source.createByteArray();
        this.name = source.readString();
        this.dateOfBirth = LocalDate.ofEpochDay(source.readLong());
        this.numberPhone = source.readString();
        this.numberOfVisits = source.readInt();
        this.hairLength = source.readInt();
        this.hairColor = source.readString();
        this.hairDensity = source.readString();
        this.conversationDetails = source.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public int getNumberOfVisits() {
        return numberOfVisits;
    }

    public void setNumberOfVisits(int numberOfVisits) {
        this.numberOfVisits = numberOfVisits;
    }

    public int getHairLength() {
        return hairLength;
    }

    public void setHairLength(int hairLength) {
        this.hairLength = hairLength;
    }

    @Nullable
    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(@Nullable String hairColor) {
        this.hairColor = hairColor;
    }

    @Nullable
    public String getHairDensity() {
        return hairDensity;
    }

    public void setHairDensity(@Nullable String hairDensity) {
        this.hairDensity = hairDensity;
    }

    @Nullable
    public String getConversationDetails() {
        return conversationDetails;
    }

    public void setConversationDetails(@Nullable String conversationDetails) {
        this.conversationDetails = conversationDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByteArray(photo);
        dest.writeString(name);
        dest.writeLong(dateOfBirth.toEpochDay());
        dest.writeString(numberPhone);
        dest.writeInt(numberOfVisits);
        dest.writeInt(hairLength);
        dest.writeString(hairColor);
        dest.writeString(hairDensity);
        dest.writeString(conversationDetails);
    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel source) {
            return new Client(source);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[0];
        }
    };

    @Override
    public String toString() {
        return name;
    }
}
