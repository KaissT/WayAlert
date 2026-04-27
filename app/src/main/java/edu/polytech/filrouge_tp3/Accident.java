package edu.polytech.filrouge_tp3;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Accident implements Parcelable {
    protected boolean humain;
    protected String description;
    protected String date;
    protected Gravite gravite;
    protected Blessure blessure; // null si humain = false

    public Accident() {}

    protected Accident(Parcel in) {
        humain = in.readByte() != 0;
        description = in.readString();
        date = in.readString();
        String graviteStr = in.readString();
        gravite = graviteStr != null ? Gravite.valueOf(graviteStr) : null;
        String blessureStr = in.readString();
        blessure = blessureStr != null ? Blessure.valueOf(blessureStr) : null;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (humain ? 1 : 0));
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(gravite != null ? gravite.name() : null);
        dest.writeString(blessure != null ? blessure.name() : null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters
    public boolean isHumain()        { return humain; }
    public String getDescription()   { return description; }
    public String getDate()          { return date; }
    public Gravite getGravite()      { return gravite; }
    public Blessure getBlessure()    { return blessure; } // null si pas de blessé

    // Setters
    public void setHumain(boolean h)       { this.humain = h; }
    public void setDescription(String d)   { this.description = d; }
    public void setDate(String d)          { this.date = d; }
    public void setGravite(Gravite g)      { this.gravite = g; }

    // Setter blessure — seulement si humain = true
    public void setBlessure(Blessure b) throws Throwable {
        if (!this.humain) {
            throw new Throwable("Impossible : aucun humain impliqué");
        }
        this.blessure = b;
    }
}