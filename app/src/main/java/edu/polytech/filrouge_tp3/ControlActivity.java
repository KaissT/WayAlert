package edu.polytech.filrouge_tp3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class ControlActivity extends AppCompatActivity implements Menuable, Notifiable {
    private static final String DATA_IS_STARTING = "sauvegarde";
    private static final String DATA_MENU_NUMBER = "num";
    private final String TAG = "frallo "+getClass().getSimpleName();
    private Fragment mainFragment;
    private MenuFragment menu;
    private boolean isStarting = true;
    
    private List<Accident> listeAccidents = new ArrayList<>();

    private Fragment[] tabFragments = { new Screen1Fragment(), new Screen2Fragment(),
                                        new InformationsFragment(), new Screen3Fragment(),
                                        new Screen4Fragment(), new Screen5Fragment(),
                                        new Screen6Fragment(), new Screen7Fragment() };
    private int menuNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        if(savedInstanceState == null) {
             menuNumber = 0;
        }

        Intent intent = getIntent();
        if(intent!=null){
            menuNumber = intent.getIntExtra(getString(R.string.index),0);
        }

        Bundle args = new Bundle();
        args.putInt(getString(R.string.index), menuNumber);

        if (savedInstanceState == null) {
            menu = new MenuFragment();
            menu.setArguments(args);
            mainFragment = tabFragments[menuNumber];

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_menu, menu);
            transaction.replace(R.id.fragment_main, mainFragment);
            transaction.commit();
        }
    }

    @Override
    public void onAccidentCreated(Accident accident) {
        listeAccidents.add(accident);
    }

    public List<Accident> getListeAccidents() {
        return listeAccidents;
    }

    @Override
    public void onMenuChange(int index) {
        if (index >= 0 && index < tabFragments.length) {
            menuNumber = index;
            mainFragment = tabFragments[index];
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_main, mainFragment);

            if (!isStarting) {
                transaction.addToBackStack(null);
            } else {
                isStarting = false;
            }
            transaction.commit();
            
            // On notifie le menu visuel si l'index est valide pour lui
            if (menu != null) {
                menu.setCurrentActivatedIndex(menuNumber);
            }
        }
    }

    @Override
    public void onFragmentDisplayed(int fragmentId) {
        if(menu != null && menuNumber != fragmentId){
            menuNumber = fragmentId;
            menu.setCurrentActivatedIndex(menuNumber);
        }
    }

    @Override
    public void onClick(int numFragment) {
        // La navigation se fait via onMenuChange
        onMenuChange(numFragment);
    }

    @Override
    public void onDataChange(int numFragment, Object data) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DATA_IS_STARTING, isStarting);
        outState.putInt(DATA_MENU_NUMBER, menuNumber);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isStarting = savedInstanceState.getBoolean(DATA_IS_STARTING);
        menuNumber = savedInstanceState.getInt(DATA_MENU_NUMBER);
    }
}
