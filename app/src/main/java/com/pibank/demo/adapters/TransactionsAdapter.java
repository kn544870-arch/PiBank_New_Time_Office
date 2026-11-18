package com.pibank.demo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pibank.demo.R;
import com.pibank.demo.models.Transaction;
import com.pibank.demo.utils.Utils;

import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

    private final List<Transaction> transactions;

    public TransactionsAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {

        private final TextView typeText;
        private final TextView descriptionText;
        private final TextView amountText;
        private final TextView dateText;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            typeText = itemView.findViewById(R.id.typeText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            amountText = itemView.findViewById(R.id.amountText);
            dateText = itemView.findViewById(R.id.dateText);
        }

        public void bind(Transaction transaction) {
            typeText.setText(Utils.getTransactionTypeDisplayName(transaction.getType()));
            descriptionText.setText(transaction.getDescription());
            amountText.setText(Utils.formatCurrency(transaction.getAmount()));
            dateText.setText(Utils.formatDate(transaction.getTimestamp()));

            int amountColor = transaction.getAmount() >= 0
                    ? itemView.getContext().getColor(R.color.colorSuccess)
                    : itemView.getContext().getColor(R.color.colorError);
            amountText.setTextColor(amountColor);
        }
    }
}
