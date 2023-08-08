package sample.model;

import java.util.Date;

public class MediaInformation {

    private String name;
    private String extension;
    private String location;
    private String description;
    private String latitude;
    private String longitude;
    private String googleMapUrl;
    private String modificationTime;
    private String takenTime;
    private double originalFileSize;
    private double currentFileSize;
    private String modifier;
    private String checkSum;
    private int qualityRating;

    public MediaInformation() {
    }

    public MediaInformation(String name, String extension, String location, String description, String latitude, String longitude, String googleMapUrl, String modificationTime, String takenTime, double originalFileSize, double currentFileSize, String modifier, String checkSum, int qualityRating) {
        this.name = name;
        this.extension = extension;
        this.location = location;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.googleMapUrl = googleMapUrl;
        this.modificationTime = modificationTime;
        this.takenTime = takenTime;
        this.originalFileSize = originalFileSize;
        this.currentFileSize = currentFileSize;
        this.modifier = modifier;
        this.checkSum = checkSum;
        this.qualityRating = qualityRating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGoogleMapUrl() {
        return googleMapUrl;
    }

    public void setGoogleMapUrl(String googleMapUrl) {
        this.googleMapUrl = googleMapUrl;
    }

    public String getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(String modificationTime) {
        this.modificationTime = modificationTime;
    }

    public String getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(String takenTime) {
        this.takenTime = takenTime;
    }

    public double getOriginalFileSize() {
        return originalFileSize;
    }

    public void setOriginalFileSize(double originalFileSize) {
        this.originalFileSize = originalFileSize;
    }

    public double getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(double currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public int getQualityRating() {
        return qualityRating;
    }

    public void setQualityRating(int qualityRating) {
        this.qualityRating = qualityRating;
    }
}
