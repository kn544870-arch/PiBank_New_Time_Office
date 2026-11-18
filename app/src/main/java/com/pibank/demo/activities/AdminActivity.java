package com.pibank.demo.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pibank.demo.R;
import com.pibank.demo.models.Card;
import com.pibank.demo.models.News;
import com.pibank.demo.utils.FirebaseHelper;

public class AdminActivity extends AppCompatActivity {

    private EditText searchCardInput;
    private Button searchButton;
    private Button addNewsButton;
    private ProgressBar progressBar;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        firebaseHelper = FirebaseHelper.getInstance();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.admin_panel);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        setupListeners();
        checkAdminAccess();
    }

    private void initViews() {
        searchCardInput = findViewById(R.id.searchCardInput);
        searchButton = findViewById(R.id.searchButton);
        addNewsButton = findViewById(R.id.addNewsButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        searchButton.setOnClickListener(v -> searchCard());
        addNewsButton.setOnClickListener(v -> showAddNewsDialog());
    }

    private void checkAdminAccess() {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) {
            finish();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseHelper.getUsersRef().child(userId).child("isAdmin")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.GONE);
                        Boolean isAdmin = snapshot.getValue(Boolean.class);
                        if (isAdmin == null || !isAdmin) {
                            Toast.makeText(AdminActivity.this, "Доступ запрещен", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        finish();
                    }
                });
    }

    private void searchCard() {
        String cardNumber = searchCardInput.getText().toString().trim().replace(" ", "");

        if (TextUtils.isEmpty(cardNumber)) {
            searchCardInput.setError("Введите номер карты");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseHelper.getCardsRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.GONE);
                        Card foundCard = null;

                        for (DataSnapshot cardSnapshot : snapshot.getChildren()) {
                            Card card = cardSnapshot.getValue(Card.class);
                            if (card != null && card.getCardNumber().replace(" ", "").equals(cardNumber)) {
                                foundCard = card;
                                break;
                            }
                        }

                        if (foundCard != null) {
                            showCardManagementDialog(foundCard);
                        } else {
                            Toast.makeText(AdminActivity.this, "Карта не найдена", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AdminActivity.this, R.string.error_loading, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showCardManagementDialog(Card card) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Управление картой");

        String message = "Номер: " + card.getCardNumber() + "\n" +
                "Владелец: " + card.getCardHolder() + "\n" +
                "Баланс: " + card.getBalance() + "\n" +
                "Статус: " + (card.isBlocked() ? "Заблокирована" : "Активна");

        builder.setMessage(message);

        builder.setPositiveButton("Изменить баланс", (dialog, which) -> showChangeBalanceDialog(card));

        builder.setNeutralButton(card.isBlocked() ? "Разблокировать" : "Заблокировать", (dialog, which) -> {
            toggleCardBlock(card);
        });

        builder.setNegativeButton("Закрыть", null);
        builder.show();
    }

    private void showChangeBalanceDialog(Card card) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Изменить баланс");

        final EditText input = new EditText(this);
        input.setHint("Новый баланс");
        input.setText(String.valueOf(card.getBalance()));
        builder.setView(input);

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String balanceStr = input.getText().toString();
            if (!TextUtils.isEmpty(balanceStr)) {
                try {
                    double newBalance = Double.parseDouble(balanceStr);
                    updateCardBalance(card, newBalance);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Неверный формат", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void updateCardBalance(Card card, double newBalance) {
        progressBar.setVisibility(View.VISIBLE);

        firebaseHelper.getCardsRef().child(card.getCardId()).child("balance").setValue(newBalance)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Баланс обновлен", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.error_network, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void toggleCardBlock(Card card) {
        progressBar.setVisibility(View.VISIBLE);

        boolean newBlockStatus = !card.isBlocked();
        firebaseHelper.getCardsRef().child(card.getCardId()).child("blocked").setValue(newBlockStatus)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        String message = newBlockStatus ? "Карта заблокирована" : "Карта разблокирована";
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.error_network, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAddNewsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавить новость");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_news, null);
        EditText titleInput = dialogView.findViewById(R.id.titleInput);
        EditText descriptionInput = dialogView.findViewById(R.id.descriptionInput);
        EditText linkInput = dialogView.findViewById(R.id.linkInput);

        builder.setView(dialogView);

        builder.setPositiveButton("Добавить", (dialog, which) -> {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String link = linkInput.getText().toString().trim();

            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {
                addNews(title, description, link);
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void addNews(String title, String description, String link) {
        progressBar.setVisibility(View.VISIBLE);

        String newsId = firebaseHelper.getNewsRef().push().getKey();
        if (newsId != null) {
            News news = new News(newsId, title, description, link);

            firebaseHelper.getNewsRef().child(newsId).setValue(news)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Новость добавлена", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, R.string.error_network, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
