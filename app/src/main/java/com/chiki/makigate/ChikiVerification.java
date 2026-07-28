// Created by ridhoae303
// https://github.com/ridhoae303
// No lambda expression

package com.chiki.makigate;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Process;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Locale;

public class ChikiVerification {

    static {
        System.loadLibrary("makigate");
    }

    public static native String nRkVxQpLm();

    public static native boolean zMpwQbLrS(int year, int month, int day);

    public static native boolean qTrLxVmNp(int year);

    public static native String pXvNqRtKm(String plain);

    public static native String vLmQrXpNz(String encrypted);

    private static final String PREFS_NAME = "maki_gate_enc";
    private static final String KEY_VERIFIED = "vrf";
    private static final String VERIFICATION_VALUE = "verified";
    private static final int MIN_BIRTH_YEAR = 1980;
    private static final long BACK_EXIT_DELAY_MS = 1800L;

    private static long lastBackPressedAt = 0L;

    public static void verify(final Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (isAlreadyVerified(activity)) {
            return;
        }

        clearVerification(activity);

        final Dialog dialog =
                new Dialog(
                        activity,
                        android.R.style.Theme_Black_NoTitleBar_Fullscreen
                );

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnKeyListener(
                new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(
                            DialogInterface dialogInterface,
                            int keyCode,
                            KeyEvent event
                    ) {
                        if (keyCode == KeyEvent.KEYCODE_BACK
                                && event.getAction() == KeyEvent.ACTION_UP) {
                            handleMainBack(activity, dialog);
                            return true;
                        }

                        return false;
                    }
                }
        );

        final View mainContent =
                buildDialogView(
                        activity,
                        dialog
                );

        mainContent.setAlpha(0.0f);
        mainContent.setScaleX(0.950f);
        mainContent.setScaleY(0.950f);

        dialog.setContentView(mainContent);

        Window window = dialog.getWindow();

        if (window != null) {
            window.setBackgroundDrawable(
                    new ColorDrawable(Color.BLACK)
            );
        }

        dialog.show();
        animateViewIn(mainContent, 360);
    }

    private static boolean isAlreadyVerified(Activity activity) {
        SharedPreferences prefs =
                activity.getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );

        String enc =
                prefs.getString(
                        KEY_VERIFIED,
                        null
                );

        if (enc == null || enc.length() == 0) {
            return false;
        }

        try {
            String dec = vLmQrXpNz(enc);

            return VERIFICATION_VALUE.equals(dec);
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static View buildDialogView(
            final Activity activity,
            final Dialog dialog
    ) {
        final DisplayMetrics dm =
                activity.getResources().getDisplayMetrics();

        int rootPadding = dp(dm, 22);

        ScrollView scrollView = new ScrollView(activity);
        scrollView.setFillViewport(true);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        scrollView.setClipChildren(false);
        scrollView.setClipToPadding(false);

        GradientDrawable rootBg =
                new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{
                                Color.parseColor("#050208"),
                                Color.parseColor("#0B0612"),
                                Color.parseColor("#030105")
                        }
                );

        scrollView.setBackgroundDrawable(rootBg);

        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setClipChildren(false);
        root.setClipToPadding(false);
        root.setPadding(
                rootPadding,
                rootPadding,
                rootPadding,
                rootPadding
        );

        scrollView.addView(
                root,
                new ScrollView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        LinearLayout card = new LinearLayout(activity);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setClipChildren(false);
        card.setClipToPadding(false);
        card.setPadding(
                dp(dm, 22),
                dp(dm, 30),
                dp(dm, 22),
                dp(dm, 24)
        );

        GradientDrawable cardBg =
                new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{
                                Color.parseColor("#1A1028"),
                                Color.parseColor("#100A19"),
                                Color.parseColor("#08050D")
                        }
                );

        cardBg.setCornerRadius(dp(dm, 30));
        cardBg.setStroke(
                dp(dm, 1),
                Color.parseColor("#6F3DFF")
        );

        card.setBackgroundDrawable(cardBg);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        root.addView(
                card,
                cardParams
        );

        final FrameLayout logoFrame = new FrameLayout(activity);

        int logoFrameSize = dp(dm, 174);
        int logoSize = dp(dm, 142);

        logoFrame.setClipChildren(false);
        logoFrame.setClipToPadding(false);

        GradientDrawable logoFrameBg =
                new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{
                                Color.parseColor("#28F4FF"),
                                Color.parseColor("#7B3DFF")
                        }
                );

        logoFrameBg.setShape(GradientDrawable.OVAL);
        logoFrameBg.setStroke(
                dp(dm, 4),
                Color.parseColor("#FFFFFF")
        );

        logoFrame.setBackgroundDrawable(logoFrameBg);
        logoFrame.setPadding(
                dp(dm, 8),
                dp(dm, 8),
                dp(dm, 8),
                dp(dm, 8)
        );

        logoFrame.setClickable(true);
        logoFrame.setFocusable(true);

        LinearLayout.LayoutParams logoFrameParams =
                new LinearLayout.LayoutParams(
                        logoFrameSize,
                        logoFrameSize
                );

        logoFrameParams.gravity = Gravity.CENTER;

        card.addView(
                logoFrame,
                logoFrameParams
        );

        ImageView logo = new ImageView(activity);

        Bitmap original =
                loadBitmapFromAssets(
                        activity,
                        "chiki/age_verify/logo.png"
                );

        if (original != null) {
            Bitmap circularLogo =
                    createCircularBitmap(
                            original,
                            logoSize
                    );

            logo.setImageBitmap(circularLogo);
            original.recycle();
        } else {
            GradientDrawable fallbackLogo = new GradientDrawable();
            fallbackLogo.setShape(GradientDrawable.OVAL);
            fallbackLogo.setColor(Color.parseColor("#7B2CFF"));
            fallbackLogo.setStroke(
                    dp(dm, 3),
                    Color.parseColor("#E8D8FF")
            );

            logo.setBackgroundDrawable(fallbackLogo);
        }

        logo.setAdjustViewBounds(false);
        logo.setScaleType(ImageView.ScaleType.CENTER_CROP);

        FrameLayout.LayoutParams logoParams =
                new FrameLayout.LayoutParams(
                        logoSize,
                        logoSize
                );

        logoParams.gravity = Gravity.CENTER;

        logoFrame.addView(
                logo,
                logoParams
        );

        logoFrame.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playLogoAnimationThenOpenLink(
                                activity,
                                logoFrame
                        );
                    }
                }
        );

        TextView modsText = new TextView(activity);

        modsText.setText("ridhoae303 Mods");
        modsText.setTextColor(Color.WHITE);
        modsText.setTextSize(26);
        modsText.setGravity(Gravity.CENTER);
        modsText.setIncludeFontPadding(true);
        modsText.setShadowLayer(
                10.0f,
                0.0f,
                0.0f,
                Color.parseColor("#8A5CFF")
        );

        try {
            Typeface font =
                    Typeface.createFromAsset(
                            activity.getAssets(),
                            "chiki/age_verify/font.ttf"
                    );

            modsText.setTypeface(font);
        } catch (Exception e) {
            modsText.setTypeface(Typeface.DEFAULT_BOLD);
        }

        LinearLayout.LayoutParams modsTextParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        modsTextParams.setMargins(
                0,
                dp(dm, 18),
                0,
                dp(dm, 2)
        );

        card.addView(
                modsText,
                modsTextParams
        );

        TextView smallHint = new TextView(activity);

        smallHint.setText("Secure age verification");
        smallHint.setTextColor(Color.parseColor("#6E35FE"));
        smallHint.setTextSize(12);
        smallHint.setGravity(Gravity.CENTER);
        smallHint.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams smallHintParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        smallHintParams.setMargins(
                0,
                0,
                0,
                dp(dm, 22)
        );

        card.addView(
                smallHint,
                smallHintParams
        );

        TextView prompt = new TextView(activity);

        prompt.setText(safePrompt());
        prompt.setTextColor(Color.WHITE);
        prompt.setTextSize(18);
        prompt.setTypeface(Typeface.DEFAULT_BOLD);
        prompt.setGravity(Gravity.CENTER);
        prompt.setLineSpacing(
                dp(dm, 2),
                1.0f
        );
        prompt.setPadding(
                dp(dm, 16),
                dp(dm, 16),
                dp(dm, 16),
                dp(dm, 16)
        );

        GradientDrawable promptBg =
                new GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT,
                        new int[]{
                                Color.parseColor("#25193A"),
                                Color.parseColor("#152336")
                        }
                );

        promptBg.setCornerRadius(dp(dm, 18));
        promptBg.setStroke(
                dp(dm, 1),
                Color.parseColor("#35E8FF")
        );

        prompt.setBackgroundDrawable(promptBg);

        LinearLayout.LayoutParams promptParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        promptParams.setMargins(
                0,
                0,
                0,
                dp(dm, 24)
        );

        card.addView(
                prompt,
                promptParams
        );

        LinearLayout btnLayout = new LinearLayout(activity);
        btnLayout.setOrientation(LinearLayout.HORIZONTAL);
        btnLayout.setGravity(Gravity.CENTER);
        btnLayout.setBackgroundColor(Color.TRANSPARENT);
        btnLayout.setPadding(
                0,
                0,
                0,
                0
        );

        LinearLayout.LayoutParams btnLayoutParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        card.addView(
                btnLayout,
                btnLayoutParams
        );

        TextView btnUnderage = new TextView(activity);

        btnUnderage.setText("Nah, I'm a minor");
        btnUnderage.setTextColor(Color.WHITE);
        btnUnderage.setTextSize(13);
        btnUnderage.setTypeface(Typeface.DEFAULT_BOLD);
        btnUnderage.setGravity(Gravity.CENTER);
        btnUnderage.setClickable(true);
        btnUnderage.setFocusable(true);
        btnUnderage.setPadding(
                dp(dm, 8),
                dp(dm, 15),
                dp(dm, 8),
                dp(dm, 15)
        );

        btnUnderage.setBackgroundDrawable(
                makeRoundedBackground(
                        Color.parseColor("#D9163A"),
                        Color.parseColor("#FF6B86"),
                        dp(dm, 18),
                        dp(dm, 1)
                )
        );

        LinearLayout.LayoutParams leftParams =
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                );

        leftParams.setMargins(
                0,
                0,
                dp(dm, 10),
                0
        );

        btnLayout.addView(
                btnUnderage,
                leftParams
        );

        TextView btnAdult = new TextView(activity);

        btnAdult.setText("Yeah, I'm 18+");
        btnAdult.setTextColor(Color.WHITE);
        btnAdult.setTextSize(13);
        btnAdult.setTypeface(Typeface.DEFAULT_BOLD);
        btnAdult.setGravity(Gravity.CENTER);
        btnAdult.setClickable(true);
        btnAdult.setFocusable(true);
        btnAdult.setPadding(
                dp(dm, 8),
                dp(dm, 15),
                dp(dm, 8),
                dp(dm, 15)
        );

        btnAdult.setBackgroundDrawable(
                makeRoundedBackground(
                        Color.parseColor("#26A85A"),
                        Color.parseColor("#6DFF9B"),
                        dp(dm, 18),
                        dp(dm, 1)
                )
        );

        LinearLayout.LayoutParams rightParams =
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                );

        rightParams.setMargins(
                dp(dm, 10),
                0,
                0,
                0
        );

        btnLayout.addView(
                btnAdult,
                rightParams
        );

        final View mainContent = scrollView;

        btnUnderage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastBackPressedAt = 0L;
                        clearVerification(activity);
                        closeApp(activity, dialog);
                    }
                }
        );

        btnAdult.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastBackPressedAt = 0L;
                        showDatePicker(
                                activity,
                                dialog,
                                mainContent
                        );
                    }
                }
        );

        return scrollView;
    }

    private static String safePrompt() {
        try {
            String prompt = nRkVxQpLm();

            if (prompt != null && prompt.trim().length() > 0) {
                return prompt;
            }
        } catch (Throwable ignored) {
        }

        return "Please verify your age before continuing.";
    }

    private static GradientDrawable makeRoundedBackground(
            int fillColor,
            int strokeColor,
            int radius,
            int strokeWidth
    ) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(fillColor);
        drawable.setCornerRadius(radius);
        drawable.setStroke(
                strokeWidth,
                strokeColor
        );

        return drawable;
    }

    private static void playLogoAnimationThenOpenLink(
            final Activity activity,
            final View logoView
    ) {
        if (activity == null || logoView == null) {
            return;
        }

        logoView.setEnabled(false);
        logoView.clearAnimation();

        logoView.animate().cancel();

        logoView.setPivotX(logoView.getWidth() / 2.0f);
        logoView.setPivotY(logoView.getHeight() / 2.0f);

        logoView.animate()
                .scaleX(0.98f)
                .scaleY(0.98f)
                .rotation(-1.5f)
                .alpha(0.94f)
                .setDuration(110)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(
                        new Runnable() {
                            @Override
                            public void run() {
                                logoView.animate()
                                        .scaleX(1.035f)
                                        .scaleY(1.035f)
                                        .rotation(1.5f)
                                        .alpha(1.0f)
                                        .setDuration(170)
                                        .setInterpolator(new DecelerateInterpolator())
                                        .withEndAction(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        logoView.animate()
                                                                .scaleX(1.0f)
                                                                .scaleY(1.0f)
                                                                .rotation(0.0f)
                                                                .alpha(1.0f)
                                                                .setDuration(210)
                                                                .setInterpolator(new DecelerateInterpolator())
                                                                .withEndAction(
                                                                        new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                logoView.setEnabled(true);
                                                                                openLogoLink(activity);
                                                                            }
                                                                        }
                                                                )
                                                                .start();
                                                    }
                                                }
                                        )
                                        .start();
                            }
                        }
                )
                .start();
    }

    private static void openLogoLink(Activity activity) {
        try {
            Intent intent =
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://chat.whatsapp.com/DcA3oplpxcbDr5vVqIfvE6")
                    );

            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(
                    activity,
                    "Unable to open link.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private static Bitmap loadBitmapFromAssets(
            Activity activity,
            String path
    ) {
        try {
            AssetManager am = activity.getAssets();

            InputStream is = am.open(path);

            Bitmap bmp = BitmapFactory.decodeStream(is);

            is.close();

            return bmp;
        } catch (Exception e) {
            return null;
        }
    }

    private static Bitmap createCircularBitmap(
            Bitmap source,
            int size
    ) {
        Bitmap output =
                Bitmap.createBitmap(
                        size,
                        size,
                        Bitmap.Config.ARGB_8888
                );

        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        Bitmap squareBitmap =
                centerCropBitmap(
                        source,
                        size,
                        size
                );

        BitmapShader shader =
                new BitmapShader(
                        squareBitmap,
                        Shader.TileMode.CLAMP,
                        Shader.TileMode.CLAMP
                );

        paint.setShader(shader);

        float radius = size / 2.0f;

        canvas.drawCircle(
                radius,
                radius,
                radius,
                paint
        );

        if (squareBitmap != source) {
            squareBitmap.recycle();
        }

        return output;
    }

    private static Bitmap centerCropBitmap(
            Bitmap source,
            int targetWidth,
            int targetHeight
    ) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        float scale =
                Math.max(
                        (float) targetWidth / sourceWidth,
                        (float) targetHeight / sourceHeight
                );

        int scaledWidth =
                Math.round(sourceWidth * scale);

        int scaledHeight =
                Math.round(sourceHeight * scale);

        Bitmap scaledBitmap =
                Bitmap.createScaledBitmap(
                        source,
                        scaledWidth,
                        scaledHeight,
                        true
                );

        int x =
                Math.max(
                        0,
                        (scaledWidth - targetWidth) / 2
                );

        int y =
                Math.max(
                        0,
                        (scaledHeight - targetHeight) / 2
                );

        Bitmap croppedBitmap =
                Bitmap.createBitmap(
                        scaledBitmap,
                        x,
                        y,
                        targetWidth,
                        targetHeight
                );

        if (scaledBitmap != source) {
            scaledBitmap.recycle();
        }

        return croppedBitmap;
    }

    private static void showDatePicker(
            final Activity activity,
            final Dialog mainDialog,
            final View mainContent
    ) {
        hideMainThenShowPicker(
                activity,
                mainDialog,
                mainContent,
                null,
                false
        );
    }

    private static void showSecondDatePicker(
            final Activity activity,
            final Dialog mainDialog,
            final View mainContent,
            final String firstDate
    ) {
        showCustomDatePicker(
                activity,
                mainDialog,
                mainContent,
                firstDate,
                true
        );
    }

    private static void showCustomDatePicker(
            final Activity activity,
            final Dialog mainDialog,
            final View mainContent,
            final String firstDate,
            final boolean secondStep
    ) {
        final DisplayMetrics dm =
                activity.getResources().getDisplayMetrics();

        final Dialog pickerDialog =
                new Dialog(
                        activity,
                        android.R.style.Theme_Translucent_NoTitleBar
                );

        pickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pickerDialog.setCancelable(false);
        pickerDialog.setCanceledOnTouchOutside(false);

        pickerDialog.setOnKeyListener(
                new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(
                            DialogInterface dialogInterface,
                            int keyCode,
                            KeyEvent event
                    ) {
                        if (keyCode == KeyEvent.KEYCODE_BACK
                                && event.getAction() == KeyEvent.ACTION_UP) {
                            returnToMainDialog(
                                    activity,
                                    pickerDialog,
                                    mainContent
                            );
                            return true;
                        }

                        return false;
                    }
                }
        );

        FrameLayout overlay = new FrameLayout(activity);
        overlay.setAlpha(0.0f);
        overlay.setBackgroundColor(Color.parseColor("#DD000000"));
        overlay.setClipChildren(false);
        overlay.setClipToPadding(false);
        overlay.setPadding(
                dp(dm, 22),
                dp(dm, 22),
                dp(dm, 22),
                dp(dm, 22)
        );

        final LinearLayout panel = new LinearLayout(activity);
        panel.setAlpha(0.0f);
        panel.setScaleX(0.930f);
        panel.setScaleY(0.930f);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setGravity(Gravity.CENTER);
        panel.setClipChildren(false);
        panel.setClipToPadding(false);
        panel.setPadding(
                dp(dm, 20),
                dp(dm, 22),
                dp(dm, 20),
                dp(dm, 18)
        );

        GradientDrawable panelBg =
                new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{
                                Color.parseColor("#18121F"),
                                Color.parseColor("#0E0A14"),
                                Color.parseColor("#08060C")
                        }
                );

        panelBg.setCornerRadius(dp(dm, 24));
        panelBg.setStroke(
                dp(dm, 1),
                Color.parseColor("#7B3DFF")
        );

        panel.setBackgroundDrawable(panelBg);

        FrameLayout.LayoutParams panelParams =
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        panelParams.gravity = Gravity.CENTER;

        overlay.addView(
                panel,
                panelParams
        );

        TextView title = new TextView(activity);

        if (secondStep) {
            title.setText("Verify your birth date again");
        } else {
            title.setText("Select your birth date");
        }

        title.setTextColor(Color.WHITE);
        title.setTextSize(21);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        titleParams.setMargins(
                0,
                0,
                0,
                dp(dm, 8)
        );

        panel.addView(
                title,
                titleParams
        );

        TextView subtitle = new TextView(activity);

        if (secondStep) {
            subtitle.setText("Enter the same date to complete verification.");
        } else {
            subtitle.setText("Your selected date will be checked securely.");
        }

        subtitle.setTextColor(Color.parseColor("#6E35FE"));
        subtitle.setTextSize(13);
        subtitle.setTypeface(Typeface.DEFAULT_BOLD);
        subtitle.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams subtitleParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        subtitleParams.setMargins(
                0,
                0,
                0,
                dp(dm, 16)
        );

        panel.addView(
                subtitle,
                subtitleParams
        );

        LinearLayout pickerBox = new LinearLayout(activity);
        pickerBox.setOrientation(LinearLayout.VERTICAL);
        pickerBox.setGravity(Gravity.CENTER);
        pickerBox.setBackgroundColor(Color.TRANSPARENT);
        pickerBox.setPadding(
                dp(dm, 14),
                dp(dm, 14),
                dp(dm, 14),
                dp(dm, 14)
        );

        GradientDrawable pickerBoxBg =
                new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{
                                Color.parseColor("#21182E"),
                                Color.parseColor("#15101F")
                        }
                );

        pickerBoxBg.setCornerRadius(dp(dm, 18));
        pickerBoxBg.setStroke(
                dp(dm, 1),
                Color.parseColor("#4DEEFF")
        );

        pickerBox.setBackgroundDrawable(pickerBoxBg);

        LinearLayout.LayoutParams pickerBoxParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        pickerBoxParams.setMargins(
                0,
                0,
                0,
                dp(dm, 18)
        );

        panel.addView(
                pickerBox,
                pickerBoxParams
        );

        TextView pickerHint = new TextView(activity);

        pickerHint.setText("Year    Month    Day");
        pickerHint.setTextColor(Color.parseColor("#8B8199"));
        pickerHint.setTextSize(13);
        pickerHint.setTypeface(Typeface.DEFAULT_BOLD);
        pickerHint.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams pickerHintParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        pickerHintParams.setMargins(
                0,
                0,
                0,
                dp(dm, 10)
        );

        pickerBox.addView(
                pickerHint,
                pickerHintParams
        );

        LinearLayout pickerLayout = new LinearLayout(activity);
        pickerLayout.setOrientation(LinearLayout.HORIZONTAL);
        pickerLayout.setGravity(Gravity.CENTER);
        pickerLayout.setBackgroundColor(Color.TRANSPARENT);

        pickerBox.addView(
                pickerLayout,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        final Calendar now = Calendar.getInstance();

        final int currentYear = now.get(Calendar.YEAR);
        final int currentMonth = now.get(Calendar.MONTH);
        final int currentDay = now.get(Calendar.DAY_OF_MONTH);

        final NumberPicker yearPicker = new StableNumberPicker(activity);
        final NumberPicker monthPicker = new StableNumberPicker(activity);
        final NumberPicker dayPicker = new StableNumberPicker(activity);

        int defaultYear = currentYear;

        yearPicker.setMinValue(MIN_BIRTH_YEAR);
        yearPicker.setMaxValue(currentYear);
        yearPicker.setValue(defaultYear);

        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(11);
        monthPicker.setDisplayedValues(
                new String[]{
                        "Jan",
                        "Feb",
                        "Mar",
                        "Apr",
                        "May",
                        "Jun",
                        "Jul",
                        "Aug",
                        "Sep",
                        "Oct",
                        "Nov",
                        "Dec"
                }
        );
        monthPicker.setValue(currentMonth);

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        dayPicker.setValue(currentDay);

        styleNumberPicker(
                yearPicker,
                Color.WHITE
        );

        styleNumberPicker(
                monthPicker,
                Color.WHITE
        );

        styleNumberPicker(
                dayPicker,
                Color.WHITE
        );

        LinearLayout.LayoutParams yearParams =
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                );

        yearParams.setMargins(
                dp(dm, 4),
                0,
                dp(dm, 4),
                0
        );

        pickerLayout.addView(
                yearPicker,
                yearParams
        );

        LinearLayout.LayoutParams monthParams =
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                );

        monthParams.setMargins(
                dp(dm, 4),
                0,
                dp(dm, 4),
                0
        );

        pickerLayout.addView(
                monthPicker,
                monthParams
        );

        LinearLayout.LayoutParams dayParams =
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                );

        dayParams.setMargins(
                dp(dm, 4),
                0,
                dp(dm, 4),
                0
        );

        pickerLayout.addView(
                dayPicker,
                dayParams
        );

        NumberPicker.OnValueChangeListener dateLimiter =
                new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(
                            NumberPicker picker,
                            int oldVal,
                            int newVal
                    ) {
                        int y = yearPicker.getValue();
                        int m = monthPicker.getValue();

                        if (y == currentYear && m > currentMonth) {
                            monthPicker.setValue(currentMonth);
                            m = currentMonth;
                        }

                        Calendar temp = Calendar.getInstance();
                        temp.set(Calendar.YEAR, y);
                        temp.set(Calendar.MONTH, m);
                        temp.set(Calendar.DAY_OF_MONTH, 1);

                        int maxDay =
                                temp.getActualMaximum(Calendar.DAY_OF_MONTH);

                        if (y == currentYear && m == currentMonth) {
                            if (maxDay > currentDay) {
                                maxDay = currentDay;
                            }
                        }

                        dayPicker.setMaxValue(maxDay);

                        if (dayPicker.getValue() > maxDay) {
                            dayPicker.setValue(maxDay);
                        }

                        // Hapus ketiga panggilan refreshNumberPickerStyle berikut
                        // untuk menghilangkan lag saat scroll
                        // refreshNumberPickerStyle(yearPicker, Color.WHITE);
                        // refreshNumberPickerStyle(monthPicker, Color.WHITE);
                        // refreshNumberPickerStyle(dayPicker, Color.WHITE);
                    }
                };

        yearPicker.setOnValueChangedListener(dateLimiter);
        monthPicker.setOnValueChangedListener(dateLimiter);
        dayPicker.setOnValueChangedListener(dateLimiter);

        dateLimiter.onValueChange(
                yearPicker,
                yearPicker.getValue(),
                yearPicker.getValue()
        );

        LinearLayout actionLayout = new LinearLayout(activity);
        actionLayout.setOrientation(LinearLayout.HORIZONTAL);
        actionLayout.setGravity(Gravity.CENTER);
        actionLayout.setBackgroundColor(Color.TRANSPARENT);
        actionLayout.setPadding(
                0,
                0,
                0,
                0
        );

        panel.addView(
                actionLayout,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        TextView cancelButton = new TextView(activity);

        cancelButton.setText("Cancel");
        cancelButton.setTextColor(Color.WHITE);
        cancelButton.setTextSize(14);
        cancelButton.setTypeface(Typeface.DEFAULT_BOLD);
        cancelButton.setGravity(Gravity.CENTER);
        cancelButton.setClickable(true);
        cancelButton.setFocusable(true);
        cancelButton.setPadding(
                dp(dm, 8),
                dp(dm, 14),
                dp(dm, 8),
                dp(dm, 14)
        );

        cancelButton.setBackgroundDrawable(
                makeRoundedBackground(
                        Color.parseColor("#3A3345"),
                        Color.parseColor("#7B7088"),
                        dp(dm, 16),
                        dp(dm, 1)
                )
        );

        LinearLayout.LayoutParams cancelParams =
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                );

        cancelParams.setMargins(
                0,
                0,
                dp(dm, 8),
                0
        );

        actionLayout.addView(
                cancelButton,
                cancelParams
        );

        TextView verifyButton = new TextView(activity);

        if (secondStep) {
            verifyButton.setText("Confirm");
        } else {
            verifyButton.setText("Verify");
        }

        verifyButton.setTextColor(Color.WHITE);
        verifyButton.setTextSize(14);
        verifyButton.setTypeface(Typeface.DEFAULT_BOLD);
        verifyButton.setGravity(Gravity.CENTER);
        verifyButton.setClickable(true);
        verifyButton.setFocusable(true);
        verifyButton.setPadding(
                dp(dm, 8),
                dp(dm, 14),
                dp(dm, 8),
                dp(dm, 14)
        );

        verifyButton.setBackgroundDrawable(
                makeRoundedBackground(
                        Color.parseColor("#6E35FF"),
                        Color.parseColor("#B99BFF"),
                        dp(dm, 16),
                        dp(dm, 1)
                )
        );

        LinearLayout.LayoutParams verifyParams =
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                );

        verifyParams.setMargins(
                dp(dm, 8),
                0,
                0,
                0
        );

        actionLayout.addView(
                verifyButton,
                verifyParams
        );

        cancelButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearVerification(activity);
                        returnToMainDialog(
                                activity,
                                pickerDialog,
                                mainContent
                        );
                    }
                }
        );

        verifyButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int year = yearPicker.getValue();
                        final int month = monthPicker.getValue();
                        final int dayOfMonth = dayPicker.getValue();

                        if (secondStep) {
                            onSecondDateSelected(
                                    activity,
                                    mainDialog,
                                    pickerDialog,
                                    mainContent,
                                    panel,
                                    firstDate,
                                    year,
                                    month,
                                    dayOfMonth
                            );
                        } else {
                            onFirstDateSelected(
                                    activity,
                                    mainDialog,
                                    pickerDialog,
                                    mainContent,
                                    panel,
                                    year,
                                    month,
                                    dayOfMonth
                            );
                        }
                    }
                }
        );

        pickerDialog.setContentView(overlay);

        Window window = pickerDialog.getWindow();

        if (window != null) {
            window.setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT)
            );
        }

        pickerDialog.show();
        animatePickerIn(
                overlay,
                panel
        );
    }

    private static void onFirstDateSelected(
            final Activity activity,
            final Dialog mainDialog,
            final Dialog pickerDialog,
            final View mainContent,
            final View panel,
            int year,
            int month,
            int dayOfMonth
    ) {
        boolean validAge = isBirthdayAllowed(year, month, dayOfMonth);

        if (!validAge) {
            clearVerification(activity);
            showToast(
                    activity,
                    "That birthday does not pass yet. Pick an 18+ date from 1980 onward."
            );
            playInvalidChoiceFeedback(panel);
            return;
        }

        boolean secondVerification = false;

        try {
            secondVerification = qTrLxVmNp(year);
        } catch (Throwable ignored) {
            secondVerification = false;
        }

        if (secondVerification) {
            final String confirmedDate = makeDateKey(
                    year,
                    month,
                    dayOfMonth
            );

            dismissDialogAnimated(
                    pickerDialog,
                    new Runnable() {
                        @Override
                        public void run() {
                            showSecondDatePicker(
                                    activity,
                                    mainDialog,
                                    mainContent,
                                    confirmedDate
                            );
                        }
                    }
            );

            return;
        }

        finishVerification(
                activity,
                mainDialog,
                pickerDialog,
                mainContent
        );
    }

    private static void onSecondDateSelected(
            final Activity activity,
            final Dialog mainDialog,
            final Dialog pickerDialog,
            final View mainContent,
            final View panel,
            String firstDate,
            int year,
            int month,
            int dayOfMonth
    ) {
        if (!isBirthdayAllowed(year, month, dayOfMonth)) {
            clearVerification(activity);
            showToast(
                    activity,
                    "That birthday does not pass yet. Pick an 18+ date from 1980 onward."
            );
            playInvalidChoiceFeedback(panel);
            return;
        }

        String secondDate = makeDateKey(
                year,
                month,
                dayOfMonth
        );

        if (firstDate == null || !firstDate.equals(secondDate)) {
            clearVerification(activity);
            showToast(
                    activity,
                    "Those dates do not match. Try the same birthday again."
            );
            playInvalidChoiceFeedback(panel);
            return;
        }

        finishVerification(
                activity,
                mainDialog,
                pickerDialog,
                mainContent
        );
    }

    private static boolean isBirthdayAllowed(
            int year,
            int month,
            int dayOfMonth
    ) {
        if (year < MIN_BIRTH_YEAR) {
            return false;
        }

        try {
            return zMpwQbLrS(
                    year,
                    month,
                    dayOfMonth
            );
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static String makeDateKey(
            int year,
            int month,
            int dayOfMonth
    ) {
        return String.format(
                Locale.US,
                "%04d-%02d-%02d",
                year,
                month + 1,
                dayOfMonth
        );
    }

    private static void finishVerification(
            final Activity activity,
            final Dialog mainDialog,
            final Dialog pickerDialog,
            final View mainContent
    ) {
        if (!saveVerification(activity)) {
            clearVerification(activity);
            showToast(
                    activity,
                    "Could not save verification. Please try again."
            );
            returnToMainDialog(
                    activity,
                    pickerDialog,
                    mainContent
            );
            return;
        }

        dismissDialogAnimated(
                pickerDialog,
                new Runnable() {
                    @Override
                    public void run() {
                        dismissDialogAnimated(mainDialog, null);
                    }
                }
        );
    }

    private static boolean saveVerification(
            Activity activity
    ) {
        try {
            SharedPreferences prefs =
                    activity.getSharedPreferences(
                            PREFS_NAME,
                            Context.MODE_PRIVATE
                    );

            String enc =
                    pXvNqRtKm(
                            VERIFICATION_VALUE
                    );

            if (enc == null || enc.length() == 0) {
                return false;
            }

            prefs.edit()
                    .putString(
                            KEY_VERIFIED,
                            enc
                    )
                    .commit();

            return isAlreadyVerified(activity);
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static void clearVerification(
            Activity activity
    ) {
        try {
            SharedPreferences prefs =
                    activity.getSharedPreferences(
                            PREFS_NAME,
                            Context.MODE_PRIVATE
                    );

            prefs.edit()
                    .remove(KEY_VERIFIED)
                    .commit();
        } catch (Exception ignored) {
        }
    }

    private static void hideMainThenShowPicker(
            final Activity activity,
            final Dialog mainDialog,
            final View mainContent,
            final String firstDate,
            final boolean secondStep
    ) {
        if (mainContent == null) {
            showCustomDatePicker(
                    activity,
                    mainDialog,
                    null,
                    firstDate,
                    secondStep
            );
            return;
        }

        mainContent.animate().cancel();

        mainContent.animate()
                .alpha(0.0f)
                .scaleX(0.965f)
                .scaleY(0.965f)
                .setDuration(240)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(
                        new Runnable() {
                            @Override
                            public void run() {
                                showCustomDatePicker(
                                        activity,
                                        mainDialog,
                                        mainContent,
                                        firstDate,
                                        secondStep
                                );
                            }
                        }
                )
                .start();
    }

    private static void returnToMainDialog(
            final Activity activity,
            final Dialog pickerDialog,
            final View mainContent
    ) {
        lastBackPressedAt = 0L;

        dismissDialogAnimated(
                pickerDialog,
                new Runnable() {
                    @Override
                    public void run() {
                        showMainContent(mainContent);
                    }
                }
        );
    }

    private static void showMainContent(
            View mainContent
    ) {
        if (mainContent == null) {
            return;
        }

        try {
            mainContent.animate().cancel();
            mainContent.setAlpha(0.0f);
            mainContent.setScaleX(0.950f);
            mainContent.setScaleY(0.950f);
            animateViewIn(mainContent, 320);
        } catch (Exception ignored) {
        }
    }

    private static void handleMainBack(
            Activity activity,
            Dialog dialog
    ) {
        long now = System.currentTimeMillis();

        if (now - lastBackPressedAt <= BACK_EXIT_DELAY_MS) {
            closeApp(activity, dialog);
            return;
        }

        lastBackPressedAt = now;

        showToast(
                activity,
                "Tap back once more to leave."
        );
    }

    private static void showToast(
            Activity activity,
            String message
    ) {
        if (activity == null || message == null) {
            return;
        }

        Toast.makeText(
                activity,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    private static void playInvalidChoiceFeedback(
            final View view
    ) {
        if (view == null) {
            return;
        }

        try {
            view.animate().cancel();
            view.setTranslationX(0.0f);

            view.animate()
                    .translationX(-10.0f)
                    .setDuration(55)
                    .withEndAction(
                            new Runnable() {
                                @Override
                                public void run() {
                                    view.animate()
                                            .translationX(10.0f)
                                            .setDuration(85)
                                            .withEndAction(
                                                    new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            view.animate()
                                                                    .translationX(0.0f)
                                                                    .setDuration(75)
                                                                    .start();
                                                        }
                                                    }
                                            )
                                            .start();
                                }
                            }
                    )
                    .start();
        } catch (Exception ignored) {
        }
    }

    private static class StableNumberPicker extends NumberPicker {
        private int forcedTextColor = Color.WHITE;

        public StableNumberPicker(Context context) {
            super(context);
        }

        public void setForcedTextColor(int color) {
            forcedTextColor = color;
            forceNumberPickerChildrenStyle(
                    this,
                    forcedTextColor
            );
        }

        @Override
        public void addView(View child) {
            super.addView(child);
            forceTextChildStyle(
                    child,
                    forcedTextColor
            );
        }

        @Override
        public void addView(
                View child,
                int index
        ) {
            super.addView(
                    child,
                    index
            );
            forceTextChildStyle(
                    child,
                    forcedTextColor
            );
        }

        @Override
        public void addView(
                View child,
                ViewGroup.LayoutParams params
        ) {
            super.addView(
                    child,
                    params
            );
            forceTextChildStyle(
                    child,
                    forcedTextColor
            );
        }

        @Override
        public void addView(
                View child,
                int index,
                ViewGroup.LayoutParams params
        ) {
            super.addView(
                    child,
                    index,
                    params
            );
            forceTextChildStyle(
                    child,
                    forcedTextColor
            );
        }
    }

    private static void styleNumberPicker(
            final NumberPicker numberPicker,
            final int textColor
    ) {
        if (numberPicker == null) {
            return;
        }

        if (numberPicker instanceof StableNumberPicker) {
            ((StableNumberPicker) numberPicker).setForcedTextColor(textColor);
        }

        numberPicker.setBackgroundColor(Color.TRANSPARENT);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setClipToPadding(false);
        numberPicker.setWillNotDraw(false);

        try {
            numberPicker.setWrapSelectorWheel(true);
        } catch (Exception ignored) {
        }

        refreshNumberPickerStyle(
                numberPicker,
                textColor
        );

        numberPicker.post(
                new Runnable() {
                    @Override
                    public void run() {
                        refreshNumberPickerStyle(
                                numberPicker,
                                textColor
                        );
                    }
                }
        );

        numberPicker.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        refreshNumberPickerStyle(
                                numberPicker,
                                textColor
                        );
                    }
                },
                120L
        );
    }

    private static void refreshNumberPickerStyle(
            final NumberPicker numberPicker,
            final int textColor
    ) {
        if (numberPicker == null) {
            return;
        }

        trySetNumberPickerMethod(
                numberPicker,
                "setTextColor",
                textColor
        );

        trySetNumberPickerMethod(
                numberPicker,
                "setSelectorTextColor",
                textColor
        );

        forceNumberPickerPaintStyle(
                numberPicker,
                textColor
        );

        forceNumberPickerDividerStyle(numberPicker);

        forceNumberPickerInputTextStyle(
                numberPicker,
                textColor
        );

        forceNumberPickerChildrenStyle(
                numberPicker,
                textColor
        );

        try {
            numberPicker.invalidate();
        } catch (Exception ignored) {
        }
    }

    private static void trySetNumberPickerMethod(
            NumberPicker numberPicker,
            String methodName,
            int color
    ) {
        try {
            Method method =
                    NumberPicker.class.getMethod(
                            methodName,
                            int.class
                    );

            method.invoke(
                    numberPicker,
                    Integer.valueOf(color)
            );
        } catch (Exception ignored) {
        }
    }

    private static void forceNumberPickerPaintStyle(
            NumberPicker numberPicker,
            int textColor
    ) {
        try {
            Field selectorWheelPaintField =
                    NumberPicker.class.getDeclaredField("mSelectorWheelPaint");

            selectorWheelPaintField.setAccessible(true);

            Object paintObject =
                    selectorWheelPaintField.get(numberPicker);

            if (paintObject instanceof Paint) {
                Paint paint = (Paint) paintObject;

                paint.setColor(textColor);
                paint.setAntiAlias(true);
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                paint.setTextSize(
                        sp(
                                numberPicker.getContext(),
                                16
                        )
                );
            }
        } catch (Exception ignored) {
        }
    }

    private static void forceNumberPickerDividerStyle(
            NumberPicker numberPicker
    ) {
        try {
            Field dividerField =
                    NumberPicker.class.getDeclaredField("mSelectionDivider");

            dividerField.setAccessible(true);

            dividerField.set(
                    numberPicker,
                    new ColorDrawable(Color.TRANSPARENT)
            );
        } catch (Exception ignored) {
        }
    }

    private static void forceNumberPickerInputTextStyle(
            NumberPicker numberPicker,
            int textColor
    ) {
        try {
            Field inputTextField =
                    NumberPicker.class.getDeclaredField("mInputText");

            inputTextField.setAccessible(true);

            Object inputObject =
                    inputTextField.get(numberPicker);

            if (inputObject instanceof TextView) {
                forceTextChildStyle(
                        (View) inputObject,
                        textColor
                );
            }
        } catch (Exception ignored) {
        }
    }

    private static void forceNumberPickerChildrenStyle(
            NumberPicker numberPicker,
            int textColor
    ) {
        try {
            int count = numberPicker.getChildCount();

            for (int i = 0; i < count; i++) {
                forceTextChildStyle(
                        numberPicker.getChildAt(i),
                        textColor
                );
            }
        } catch (Exception ignored) {
        }
    }

    private static void forceTextChildStyle(
            View child,
            int textColor
    ) {
        if (!(child instanceof TextView)) {
            return;
        }

        TextView textView = (TextView) child;

        try {
            textView.setTextColor(textColor);
            textView.setTextSize(16);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setGravity(Gravity.CENTER);
            textView.setIncludeFontPadding(false);
            textView.setBackgroundColor(Color.TRANSPARENT);
            textView.setShadowLayer(
                    0.0f,
                    0.0f,
                    0.0f,
                    Color.TRANSPARENT
            );
        } catch (Exception ignored) {
        }

        if (textView instanceof EditText) {
            EditText editText = (EditText) textView;

            try {
                editText.setTextColor(textColor);
                editText.setHintTextColor(textColor);
                editText.setHighlightColor(Color.TRANSPARENT);
                editText.setCursorVisible(false);
                editText.setSelectAllOnFocus(false);
                editText.setSingleLine(true);
                editText.setInputType(InputType.TYPE_NULL);
                editText.setFocusable(false);
                editText.setFocusableInTouchMode(false);
                editText.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception ignored) {
            }
        }
    }

    private static float sp(
            Context context,
            int value
    ) {
        if (context == null) {
            return (float) value;
        }

        return value * context.getResources().getDisplayMetrics().scaledDensity;
    }

    private static void animateViewIn(
            View view,
            long duration
    ) {
        try {
            if (view == null) {
                return;
            }

            view.animate()
                    .alpha(1.0f)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(duration)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        } catch (Exception ignored) {
        }
    }

    private static void animatePickerIn(
            View overlay,
            View panel
    ) {
        try {
            if (overlay != null) {
                overlay.animate()
                        .alpha(1.0f)
                        .setDuration(280)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
            }

            if (panel != null) {
                panel.animate()
                        .alpha(1.0f)
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(340)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
            }
        } catch (Exception ignored) {
        }
    }

    private static void dismissDialogAnimated(
            final Dialog dialog,
            final Runnable endAction
    ) {
        try {
            if (dialog == null || !dialog.isShowing()) {
                if (endAction != null) {
                    endAction.run();
                }

                return;
            }

            Window window = dialog.getWindow();

            if (window == null) {
                dialog.dismiss();

                if (endAction != null) {
                    endAction.run();
                }

                return;
            }

            View decor = window.getDecorView();

            if (decor == null) {
                dialog.dismiss();

                if (endAction != null) {
                    endAction.run();
                }

                return;
            }

            decor.animate()
                    .alpha(0.0f)
                    .scaleX(0.965f)
                    .scaleY(0.965f)
                    .setDuration(230)
                    .setInterpolator(new DecelerateInterpolator())
                    .withEndAction(
                            new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        dialog.dismiss();
                                    } catch (Exception ignored) {
                                    }

                                    if (endAction != null) {
                                        endAction.run();
                                    }
                                }
                            }
                    )
                    .start();
        } catch (Exception ignored) {
            try {
                if (dialog != null) {
                    dialog.dismiss();
                }
            } catch (Exception ignoredToo) {
            }

            if (endAction != null) {
                endAction.run();
            }
        }
    }

    private static void blackOutDialog(
            Activity activity,
            Dialog dialog
    ) {
        try {
            if (dialog != null && dialog.isShowing()) {
                Window window = dialog.getWindow();

                if (window != null) {
                    window.setBackgroundDrawable(
                            new ColorDrawable(Color.BLACK)
                    );
                }

                TextView blackView = new TextView(activity);
                blackView.setBackgroundColor(Color.BLACK);
                blackView.setText("");
                blackView.setGravity(Gravity.CENTER);

                dialog.setContentView(
                        blackView,
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        )
                );
            }
        } catch (Exception ignored) {
        }
    }

    private static void blackOutActivity(
            Activity activity
    ) {
        try {
            if (activity == null) {
                return;
            }

            Window activityWindow = activity.getWindow();

            if (activityWindow == null) {
                return;
            }

            View decor = activityWindow.getDecorView();

            if (decor instanceof ViewGroup) {
                ViewGroup decorGroup = (ViewGroup) decor;

                TextView blackView = new TextView(activity);
                blackView.setBackgroundColor(Color.BLACK);
                blackView.setText("");
                blackView.setGravity(Gravity.CENTER);

                decorGroup.addView(
                        blackView,
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        )
                );

                blackView.bringToFront();
            }
        } catch (Exception ignored) {
        }
    }

    private static void closeApp(
            Activity activity,
            Dialog dialog
    ) {
        blackOutDialog(
                activity,
                dialog
        );

        blackOutActivity(activity);

        try {
            if (activity != null) {
                activity.finish();
                activity.overridePendingTransition(0, 0);
                activity.moveTaskToBack(true);
            }
        } catch (Exception ignored) {
        }

        try {
            Process.killProcess(Process.myPid());
        } catch (Exception ignored) {
        }

        try {
            Runtime.getRuntime().halt(0);
        } catch (Throwable ignored) {
        }

        try {
            System.exit(0);
        } catch (Exception ignored) {
        }
    }

    private static int dp(
            DisplayMetrics dm,
            int value
    ) {
        return (int) (value * dm.density + 0.5f);
    }
}