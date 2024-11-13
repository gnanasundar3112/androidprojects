package com.example.sriganesh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sriganesh.Model.LedgerModel;
import com.example.sriganesh.R;

import java.util.ArrayList;
import java.util.List;

public class LedgerAdapter extends RecyclerView.Adapter<LedgerAdapter.ViewHolder>{

    private Context mContext;
    private List<LedgerModel> ledgerModel = new ArrayList<>();

    public LedgerAdapter(Context mContext, List<LedgerModel> ledgerModel) {
        this.mContext = mContext;
        this.ledgerModel = ledgerModel;
    }

    @NonNull
    @Override
    public LedgerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ledger,parent,false);
        return new LedgerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LedgerAdapter.ViewHolder holder, int position) {

        final LedgerModel ledgerModels = ledgerModel.get(position);

        holder.LEDGER_FIRM.setText(ledgerModels.getFirmName());
        holder.LEDGER_DATE.setText(ledgerModels.getVouDate());
        holder.LEDGER_NARATION.setText(ledgerModels.getNaration());
        holder.LEDGER_CREDIT.setText(""+ledgerModels.getCredit());
        holder.LEDGER_DEBIT.setText(""+ledgerModels.getDebit());
        holder.LEDGER_CLOSING.setText(""+ledgerModels.getClosing());

    }

    @Override
    public int getItemCount() {
        return ledgerModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView LEDGER_FIRM,LEDGER_DATE,LEDGER_NARATION,LEDGER_CREDIT,LEDGER_DEBIT,LEDGER_CLOSING;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            LEDGER_FIRM = itemView.findViewById(R.id.ledger_firm_name);
            LEDGER_DATE = itemView.findViewById(R.id.ledger_date);
            LEDGER_NARATION = itemView.findViewById(R.id.ledger_naration);
            LEDGER_CREDIT = itemView.findViewById(R.id.ledger_credit);
            LEDGER_DEBIT = itemView.findViewById(R.id.ledger_debit);
            LEDGER_CLOSING = itemView.findViewById(R.id.ledger_closing);
        }
    }
}
