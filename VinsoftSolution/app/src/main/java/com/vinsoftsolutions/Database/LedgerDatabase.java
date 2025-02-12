package com.vinsoftsolutions.Database;

import android.content.Context;


import com.vinsoftsolutions.Adapter.LedgerAdapter;
import com.vinsoftsolutions.Models.LedgerModel;

import java.util.ArrayList;
import java.util.List;

public class LedgerDatabase {
    private Context context;
    private List<LedgerModel> LEDGER =  new ArrayList<>();
    private LedgerAdapter LEDGER_ADAPTER;
    public LedgerDatabase(Context context){
        this.context = context;
    }
}
