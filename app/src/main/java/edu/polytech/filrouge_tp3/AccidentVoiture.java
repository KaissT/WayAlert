package edu.polytech.filrouge_tp3;

import android.os.Parcel;
import android.os.Parcelable;

public class AccidentVoiture extends Accident {

    private int nombreVehicules;

    public AccidentVoiture() {
        super();
        this.humain = false;
        this.gravite = Gravite.FAIBLE;
    }

    protected AccidentVoiture(Parcel in) {
        super(in);
        nombreVehicules = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(nombreVehicules);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<AccidentVoiture> CREATOR = new Parcelable.Creator<AccidentVoiture>() {
        @Override
        public AccidentVoiture createFromParcel(Parcel in) {
            return new AccidentVoiture(in);
        }

        @Override
        public AccidentVoiture[] newArray(int size) {
            return new AccidentVoiture[size];
        }
    };

    public int getNombreVehicules()       { return nombreVehicules; }
    public void setNombreVehicules(int n) { this.nombreVehicules = n; }
}