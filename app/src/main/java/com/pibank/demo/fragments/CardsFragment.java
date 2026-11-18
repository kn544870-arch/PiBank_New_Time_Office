package com.pibank.demo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pibank.demo.R;
import com.pibank.demo.adapters.CardsAdapter;
import com.pibank.demo.models.Card;
import com.pibank.demo.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CardsFragment extends Fragment {

    private RecyclerView cardsRecyclerView;
    private CardsAdapter cardsAdapter;
    private Button addCardButton;
    private ProgressBar progressBar;
    private FirebaseHelper firebaseHelper;
    private List<Card> cardsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, container, false);

        firebaseHelper = FirebaseHelper.getInstance();
        cardsList = new ArrayList<>();

        initViews(view);
        setupRecyclerView();
        setupListeners();
        loadCards();

        return view;
    }

    private void initViews(View view) {
        cardsRecyclerView = view.findViewById(R.id.cardsRecyclerView);
        addCardButton = view.findViewById(R.id.addCardButton);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        cardsAdapter = new CardsAdapter(cardsList);
        cardsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cardsRecyclerView.setAdapter(cardsAdapter);
    }

    private void setupListeners() {
        addCardButton.setOnClickListener(v -> showAddCardDialog());
    }

    private void loadCards() {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) return;

        progressBar.setVisibility(View.VISIBLE);

        firebaseHelper.getCardsRef()
                .orderByChild("userId")
                .equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.GONE);
                        cardsList.clear();
                        for (DataSnapshot cardSnapshot : snapshot.getChildren()) {
                            Card card = cardSnapshot.getValue(Card.class);
                            if (card != null) {
                                cardsList.add(card);
                            }
                        }
                        cardsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), R.string.error_loading, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAddCardDialog() {
        if (cardsList.size() >= 3) {
            Toast.makeText(getContext(), R.string.max_cards_reached, Toast.LENGTH_SHORT).show();
            return;
        }

        String[] cardTypes = {
                getString(R.string.debit_card),
                getString(R.string.credit_card),
                getString(R.string.kids_card)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.select_card_type);
        builder.setItems(cardTypes, (dialog, which) -> {
            String cardType = "";
            switch (which) {
                case 0:
                    cardType = Card.TYPE_DEBIT;
                    break;
                case 1:
                    cardType = Card.TYPE_CREDIT;
                    break;
                case 2:
                    cardType = Card.TYPE_KIDS;
                    break;
            }
            createCard(cardType);
        });
        builder.show();
    }

    private void createCard(String cardType) {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) return;

        progressBar.setVisibility(View.VISIBLE);

        firebaseHelper.getUsersRef().child(userId).child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String cardHolder = snapshot.getValue(String.class);
                        if (cardHolder == null) cardHolder = "Card Holder";

                        String cardId = firebaseHelper.getCardsRef().push().getKey();
                        Card card = new Card(cardId, userId, cardType, cardHolder);

                        if (cardId != null) {
                            firebaseHelper.getCardsRef().child(cardId).setValue(card)
                                    .addOnCompleteListener(task -> {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), R.string.card_created, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), R.string.error_network, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
