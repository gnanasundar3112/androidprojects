package com.vinsoftsolutions.Service;

import android.content.Context;
import android.content.Intent;

public class ActivityMoving {
    public static void ActivityToActivity(Context context, Class<?> toActivityClass){
        Intent intent = new Intent(context, toActivityClass);
        context.startActivity(intent);
    }
    public static void ActivityMoving(Context context, Class<?> toActivityClass){
        Intent intent = new Intent(context, toActivityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
