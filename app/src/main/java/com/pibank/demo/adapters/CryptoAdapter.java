package com.pibank.demo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pibank.demo.R;
import com.pibank.demo.models.Crypto;
import com.pibank.demo.utils.Utils;

import java.util.List;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder> {

    private final List<Crypto> cryptoList;

    public CryptoAdapter(List<Crypto> cryptoList) {
        this.cryptoList = cryptoList;
    }

    @NonNull
    @Override
    public CryptoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_crypto, parent, false);
        return new CryptoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CryptoViewHolder holder, int position) {
        Crypto crypto = cryptoList.get(position);
        holder.bind(crypto);
    }

    @Override
    public int getItemCount() {
        return cryptoList.size();
    }

    static class CryptoViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameText;
        private final TextView symbolText;
        private final TextView amountText;
        private final TextView priceText;
        private final TextView totalValueText;

        public CryptoViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            symbolText = itemView.findViewById(R.id.symbolText);
            amountText = itemView.findViewById(R.id.amountText);
            priceText = itemView.findViewById(R.id.priceText);
            totalValueText = itemView.findViewById(R.id.totalValueText);
        }

        public void bind(Crypto crypto) {
            nameText.setText(crypto.getCryptoName());
            symbolText.setText(crypto.getSymbol());
            amountText.setText(String.format("%.8f", crypto.getAmount()));
            priceText.setText(Utils.formatCurrency(crypto.getPriceUSD()));
            totalValueText.setText(Utils.formatCurrency(crypto.getTotalValueUSD()));
        }
    }
}
