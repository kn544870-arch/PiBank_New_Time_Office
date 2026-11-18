package com.pibank.demo.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pibank.demo.R;
import com.pibank.demo.models.Card;
import com.pibank.demo.utils.Utils;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardViewHolder> {

    private final List<Card> cards;

    public CardsAdapter(List<Card> cards) {
        this.cards = cards;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.bind(card);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardFront;
        private final CardView cardBack;
        private final TextView cardNumberText;
        private final TextView cardHolderText;
        private final TextView cardExpiryText;
        private final TextView cardBalanceText;
        private final TextView cardTypeText;
        private final TextView cardCvvText;
        private boolean isCardFront = true;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardFront = itemView.findViewById(R.id.cardFront);
            cardBack = itemView.findViewById(R.id.cardBack);
            cardNumberText = itemView.findViewById(R.id.cardNumberText);
            cardHolderText = itemView.findViewById(R.id.cardHolderText);
            cardExpiryText = itemView.findViewById(R.id.cardExpiryText);
            cardBalanceText = itemView.findViewById(R.id.cardBalanceText);
            cardTypeText = itemView.findViewById(R.id.cardTypeText);
            cardCvvText = itemView.findViewById(R.id.cardCvvText);
        }

        public void bind(Card card) {
            cardNumberText.setText(card.getCardNumber());
            cardHolderText.setText(card.getCardHolder());
            cardExpiryText.setText(card.getExpiryDate());
            cardBalanceText.setText(Utils.formatCurrency(card.getBalance()));
            cardTypeText.setText(Utils.getCardTypeDisplayName(card.getCardType()));
            cardCvvText.setText(card.getCvv());

            itemView.setOnClickListener(v -> flipCard());
        }

        private void flipCard() {
            AnimatorSet flipOut;
            AnimatorSet flipIn;

            if (isCardFront) {
                flipOut = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(), R.anim.card_flip_out);
                flipIn = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(), R.anim.card_flip_in);
                flipOut.setTarget(cardFront);
                flipIn.setTarget(cardBack);

                flipOut.start();
                flipIn.start();

                cardFront.setVisibility(View.GONE);
                cardBack.setVisibility(View.VISIBLE);
            } else {
                flipOut = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(), R.anim.card_flip_out);
                flipIn = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(), R.anim.card_flip_in);
                flipOut.setTarget(cardBack);
                flipIn.setTarget(cardFront);

                flipOut.start();
                flipIn.start();

                cardBack.setVisibility(View.GONE);
                cardFront.setVisibility(View.VISIBLE);
            }

            isCardFront = !isCardFront;
        }
    }
}
