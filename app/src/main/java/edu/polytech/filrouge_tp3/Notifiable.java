package edu.polytech.filrouge_tp3;

import android.content.Context;

public interface Notifiable {
    void onClick(int numFragment);
    void onDataChange(int numFragment, Object object);
    void onFragmentDisplayed(int fragmentId);
}
