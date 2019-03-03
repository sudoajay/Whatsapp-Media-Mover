package com.sudoajay.whatapp_media_mover_to_sdcard.Toast;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sudoajay.whatapp_media_mover_to_sdcard.R;


public class CustomToast {

    public static void ToastIt(final Context mContext , final String mes){
        Toast toast = Toast.makeText(mContext, mes, Toast.LENGTH_LONG);
        View view = toast.getView();
//Gets the actual oval background of the Toast then sets the colour filter
        view.getBackground().setColorFilter(mContext.getResources().getColor(R.color.toastBackgroundColor)
                , PorterDuff.Mode.SRC_IN);
//Gets the TextView from the Toast so it can be editted
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(mContext.getResources().getColor(R.color.toastTextColor));

        toast.show();
    }


}
