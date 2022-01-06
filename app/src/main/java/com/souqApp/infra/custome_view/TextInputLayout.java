package com.souqApp.infra.custome_view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import android.util.AttributeSet;

import androidx.annotation.Nullable;


public class TextInputLayout extends com.google.android.material.textfield.TextInputLayout {

    Typeface typeface;

    public TextInputLayout(Context context) {
        super(context);
    }

    public TextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
    }

    public TextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setError(@Nullable CharSequence error) {
        if (TextUtils.isEmpty(error)) {
            // If error isn't enabled, and the error is empty, just return
            setErrorEnabled(false);
            return;
        }
        SpannableString errorTxt = new SpannableString(error);
        errorTxt.setSpan(new TypefaceSpan(typeface), 0, errorTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setError(errorTxt);
    }

    @Override
    public void setHelperText(@Nullable CharSequence helperText) {

        if (TextUtils.isEmpty(helperText)) {
            if (isHelperTextEnabled()) {
                setHelperTextEnabled(false);
            }
        } else {
            if (!isHelperTextEnabled()) {
                setHelperTextEnabled(true);
            }
        }

        if (TextUtils.isEmpty(helperText)) {
            // If error isn't enabled, and the error is empty, just return
            return;
        }
        SpannableString errorTxt = new SpannableString(helperText);
        errorTxt.setSpan(new TypefaceSpan(typeface), 0, errorTxt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setHelperText(errorTxt);

    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        //Reset EditText's background color to default.
    }

    public static class TypefaceSpan extends MetricAffectingSpan {
        private final Typeface mTypeface;

        TypefaceSpan(Typeface typeface) {
            mTypeface = typeface;
        }

        @Override
        public void updateMeasureState(TextPaint p) {
            p.setTypeface(mTypeface);
            p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setTypeface(mTypeface);
            tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
}