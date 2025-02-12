package com.vinsoftsolutions.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vinsoftsolutions.Models.LedgerModel;
import com.vinsoftsolutions.R;

import java.util.List;

public class LedgerAdapter extends RecyclerView.Adapter<LedgerAdapter.ImageView> {
    private Context context;
    private List<LedgerModel> ledgerModels;

    public LedgerAdapter(Context context, List<LedgerModel> ledgerModels) {
        this.context = context;
        this.ledgerModels = ledgerModels;
    }

    @NonNull
    @Override
    public LedgerAdapter.ImageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ledger,parent,false);
        return new LedgerAdapter.ImageView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LedgerAdapter.ImageView holder, int position) {
        final LedgerModel ledgerModel = ledgerModels.get(position);

        holder.FIRM_NAME.setText(ledgerModel.getFirm_name());
        holder.VOU_DATE.setText(ledgerModel.getVou_date());
        holder.NARATION.setText(ledgerModel.getNarration());
        holder.CREDIT.setText(""+ledgerModel.getCredit());
        holder.DEBIT.setText(""+ledgerModel.getDebit());
        holder.CLOSING.setText(ledgerModel.getClosing());
    }

    @Override
    public int getItemCount() {
        return ledgerModels.size();
    }

    public class ImageView extends RecyclerView.ViewHolder {
        TextView FIRM_NAME,VOU_DATE,NARATION,CREDIT,DEBIT,CLOSING;
        public ImageView(@NonNull View itemView) {
            super(itemView);

            FIRM_NAME = itemView.findViewById(R.id.ledger_firm_name);
            VOU_DATE = itemView.findViewById(R.id.ledger_date);
            NARATION = itemView.findViewById(R.id.ledger_naration);
            CREDIT = itemView.findViewById(R.id.ledger_credit);
            DEBIT = itemView.findViewById(R.id.ledger_debit);
            CLOSING = itemView.findViewById(R.id.ledger_closing);
        }
    }
}
