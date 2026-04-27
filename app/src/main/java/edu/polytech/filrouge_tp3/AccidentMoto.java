package edu.polytech.filrouge_tp3;

import android.os.Parcel;
import android.os.Parcelable;

public class AccidentMoto extends Accident {

    private boolean casquePorte;

    public AccidentMoto() {
        super();
        this.humain = false;
        this.gravite = Gravite.FAIBLE;
        this.casquePorte = true;
    }

    protected AccidentMoto(Parcel in) {
        super(in);
        casquePorte = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (casquePorte ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<AccidentMoto> CREATOR = new Parcelable.Creator<AccidentMoto>() {
        @Override
        public AccidentMoto createFromParcel(Parcel in) {
            return new AccidentMoto(in);
        }

        @Override
        public AccidentMoto[] newArray(int size) {
            return new AccidentMoto[size];
        }
    };

    public boolean isCasquePorte() {
        return casquePorte;
    }

    public void setCasquePorte(boolean casquePorte) {
        this.casquePorte = casquePorte;
    }
}
