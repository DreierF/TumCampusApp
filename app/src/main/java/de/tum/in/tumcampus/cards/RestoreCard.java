package de.tum.in.tumcampus.cards;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.tum.in.tumcampus.R;
import de.tum.in.tumcampus.models.managers.CardManager;

/**
 * Card that allows the user to reset the dismiss state of all cards
 */
public class RestoreCard extends Card {

    public RestoreCard(Context context) {
        super(context);
    }

    public static RecyclerView.ViewHolder inflateViewHolder(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_restore, parent, false);
        return new RecyclerView.ViewHolder(view) {};
    }

    @Override
    public int getTyp() {
        return CardManager.CARD_RESTORE;
    }

    @Override
    public boolean isDismissable() {
        return false;
    }
}
