package edu.polytech.filrouge_tp3;

public interface Notifiable {
    void onClick(int fragmentId);
    void onFragmentDisplayed(int fragmentId);
    void onAccidentCreated(Accident accident); // Nouvelle méthode

    void onDataChange(int numFragment, Object data);
}
