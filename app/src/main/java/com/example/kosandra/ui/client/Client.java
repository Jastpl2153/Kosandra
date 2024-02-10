package com.example.kosandra.ui.client;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDate;

@Entity(tableName = "client")
@TypeConverters(Converters.class)
public class Client {
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
}
