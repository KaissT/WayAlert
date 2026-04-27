package edu.polytech.filrouge_tp3;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class MenuFragment extends Fragment{
    private final String TAG = "frallo "+getClass().getSimpleName();
    private Menuable menuable;
    private int currentActivatedIndex = 0;
    private  View layout;


    public MenuFragment() {
    }

    public void setCurrentActivatedIndex(int index){
        Log.d(TAG,"setCurrentActivatedIndex updated to " + index +" (currentActivatedIndex = "+currentActivatedIndex+")");
        List<ImageView> imageViews = findPicturesMenuFromId( layout.findViewById(R.id.itemsMenu));
        
        // Protection contre l'IndexOutOfBoundsException
        if (currentActivatedIndex < imageViews.size()) {
            imageViews.get(currentActivatedIndex).setImageResource(  layout.getResources().getIdentifier("menu"+(currentActivatedIndex), "mipmap", layout.getContext().getPackageName()) );
        }
        
        if (index < imageViews.size()) {
            imageViews.get(index).setImageResource(  layout.getResources().getIdentifier("menu"+(index)+"_s", "mipmap", layout.getContext().getPackageName()) );
        }
        
        currentActivatedIndex = index;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_menu, container, false);

        List<ImageView> imageViews = findPicturesMenuFromId( layout.findViewById(R.id.itemsMenu));

        if (getArguments() != null) {
            currentActivatedIndex = getArguments().getInt(getString(R.string.index), 0);
        }
        
        // Protection à l'initialisation
        if (currentActivatedIndex < imageViews.size()) {
            imageViews.get(currentActivatedIndex).setImageResource(  layout.getResources().getIdentifier("menu"+(currentActivatedIndex)+"_s", "mipmap", layout.getContext().getPackageName()) );
        }


        for(ImageView imageView : imageViews) {
            imageView.setOnClickListener( menu -> {
                int oldIndex = currentActivatedIndex;
                currentActivatedIndex = Integer.parseInt(imageView.getTag().toString());

                menuable.onMenuChange(currentActivatedIndex);

                if (oldIndex < imageViews.size()) {
                    imageViews.get(oldIndex).setImageResource(  layout.getResources().getIdentifier("menu"+(oldIndex), "mipmap", layout.getContext().getPackageName()) );
                }

                if (currentActivatedIndex < imageViews.size()) {
                    ((ImageView)menu).setImageResource(  layout.getResources().getIdentifier("menu"+(currentActivatedIndex)+"_s", "mipmap", layout.getContext().getPackageName()) );
                }
            });
        }
        return layout;
    }

    private List<ImageView> findPicturesMenuFromId(View view) {
        List<ImageView> pictures = new ArrayList<>();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof ImageView) {
                    // On accepte tous les IDs commençant par menu
                    pictures.add((ImageView) child);
                }
            }
        }
        return pictures;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof Menuable) {
            menuable = (Menuable) requireActivity();
        } else {
            throw new AssertionError("Classe " + requireActivity().getClass().getName() + " ne met pas en œuvre Notifiable.");
        }
    }
}
