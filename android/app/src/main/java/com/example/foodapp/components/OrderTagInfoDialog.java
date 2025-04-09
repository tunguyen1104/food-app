package com.example.foodapp.components;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.foodapp.R;
import com.example.foodapp.enums.OrderStatus;

public class OrderTagInfoDialog {

    public static void show(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        for (OrderStatus tag : OrderStatus.values()) {
            LinearLayout rowLayout = new LinearLayout(context);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setPadding(20, 20, 20, 20);
            rowLayout.setGravity(Gravity.CENTER_VERTICAL);

            CardView circleView = new CardView(context);
            LinearLayout.LayoutParams circleParams = new LinearLayout.LayoutParams(50, 50);
            circleParams.setMargins(0, 0, 20, 0);
            circleView.setLayoutParams(circleParams);
            circleView.setRadius(15);
            circleView.setCardElevation(0);
            circleView.setCardBackgroundColor(ResourcesCompat.getColor(
                    context.getResources(), tag.getColorResId(), null));

            TextView textView = new TextView(context);
            textView.setText(tag.getLabel());
            textView.setTextAppearance(R.style.body_m);
            textView.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.dark_darkest, null));

            rowLayout.addView(circleView);
            rowLayout.addView(textView);

            layout.addView(rowLayout);
        }

        new AlertDialog.Builder(context)
                .setTitle("Tag color descriptions:")
                .setView(layout)
                .setPositiveButton("OK", null)
                .show();
    }
}
