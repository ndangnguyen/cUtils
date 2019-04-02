package APP_PACKAGE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.gameloft.android.wrapper.Base64;
import com.gameloft.android.wrapper.Utils;

import org.json.JSONObject;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.microedition.lcdui.AlertDialog;

import _GAME_PKG_.R;

public class cUtils {
    public static void setFont(Context context, ViewGroup group) {
        int count = group.getChildCount();
        View v;
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Sansation_Regular.ttf");
        if(Build.VERSION.SDK_INT < 21 && LogoActivity.language.equals("vi"))
            font = Typeface.DEFAULT;
        if (android.os.Build.MODEL.contains("BNTV600") &&  LogoActivity.language.equals("vi") )
            font = Typeface.MONOSPACE; //fix font in Nook book
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView) {
                if(((TextView)v).getTypeface()!=null) {
                    if (((TextView) v).getTypeface().getStyle() == Typeface.BOLD) {
                        //do your stuff
                        ((TextView) v).setTypeface(font, Typeface.BOLD);
                    }
                    else
                    {
                        ((TextView) v).setTypeface(font);
                    }
                }
                else
                {
                    ((TextView) v).setTypeface(font);
                }
                ((TextView) v).setSelected(true);
            }
            else if (v instanceof EditText) {
                if(((EditText)v).getTypeface()!=null) {
                    if (((EditText) v).getTypeface().getStyle() == Typeface.BOLD) {
                        //do your stuff
                        ((EditText) v).setTypeface(font, Typeface.BOLD);
                    }
                    else
                    {
                        ((EditText) v).setTypeface(font);
                    }

                }
                else
                {
                    ((EditText) v).setTypeface(font);
                }
            }
            else if (v instanceof Button) {
                if(((Button)v).getTypeface()!=null) {
                    if (((Button) v).getTypeface().getStyle() == Typeface.BOLD) {
                        //do your stuff
                        ((Button) v).setTypeface(font, Typeface.BOLD);
                    }
                    else
                    {
                        ((Button) v).setTypeface(font);
                    }
                }
                else
                {
                    ((Button) v).setTypeface(font);
                }
                ((Button) v).setSelected(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    ((Button) v).setAllCaps(true);
                }
            }
            else if (v instanceof ViewGroup)
                setFont(context,(ViewGroup) v);
        }
    }
    public static String _GAME_PKG__STRING_=_GAME_PKG_STRING_;
    public static int s_previousActivity = 0;
    public static boolean isTutorial(Context ct)
    {
        return getPreferenceBoolean(ct,"IS_TUTORIAL",true,_GAME_PKG__STRING_);
    }
    public static void DisableTutorial(Context ct)
    {
        setPreference(ct,"IS_TUTORIAL", new Boolean(false),_GAME_PKG__STRING_);
    }
    public static int Random(int max)
    {
        return new Random().nextInt(max);
    }
    public static void setPreference(String key, Object value)
    {
        setPreference(key, value,_GAME_PKG__STRING_);
    }
    public static void setPreference(String key, Object value, String pname)
    {
        setPreference(Utils.getContext(), key,  value,  pname);
    }
    public static void setPreference(Context context,String key, Object value, String pname)
    {
        Log.d("Utils", "setPreferences(" + key + ", " + value + ", " + pname + ")");
        SharedPreferences settings = context.getSharedPreferences(pname, 0);
        SharedPreferences.Editor editor = settings.edit();

        if (value instanceof String)
        {
            editor.putString(Encoded(key), Encoded((String) value));
        }
        else if (value instanceof Integer)
        {
            // editor.putInt(key, (Integer) value);
            editor.putString(Encoded(key), Encoded(String.valueOf(value)));
        }
        else if (value instanceof Boolean)
        {
            //  editor.putBoolean(key, (Boolean) value);
            editor.putString(Encoded(key), Encoded(String.valueOf(value)));
        }
        else if(value instanceof Long)
        {
            // editor.putLong(key, (Long)value);
            editor.putString(Encoded(key), Encoded(String.valueOf(value)));
        }
        editor.commit();
    }
    public static String getPreferenceString(String key, String pname)
    {
        return getPreferenceString(Utils.getContext(), key,  pname);
    }
    public static String getPreferenceString(Context context,String key, String pname)
    {
        SharedPreferences settings =context.getSharedPreferences(pname, 0);
        String res = settings.getString(Encoded(key), "");
        Log.d("Utils", "getPreferenceString(" + key + ", " + Decode(res)  + ", " + pname + ")");
        return Decode(res);

    }
    public static java.util.Hashtable getPreferenceHashTable(Context context,String pname)
    {
        SharedPreferences settings = context.getSharedPreferences(pname, 0);
        java.util.Hashtable h =  new java.util.Hashtable(settings.getAll());
        return h;
    }
    /**
     * To read a string from the preferences file.
     * @param key The key that identifies the preferences file.
     * @param defaultValue The default value to be returned if the preference does not exist.
     * @param pname the preference name
     * */
    public static String getPreferenceString(String key, String defaultValue, String pname)
    {
        return getPreferenceString(Utils.getContext(), key,  defaultValue,  pname);
    }
    public static String getPreferenceString(Context context,String key, String defaultValue, String pname)
    {
        SharedPreferences settings = context.getSharedPreferences(pname, 0);
        String res = settings.getString(Encoded(key), Encoded(defaultValue));
        Log.d("Utils", "getPreferenceString(" + key + ", " + defaultValue + " " + Decode(res)  + ", " + pname + ")");
        return Decode(res);

    }

    /**
     * To read an int from the preferences file.
     * @param key The key that identifies the preferences file.
     * @param defaultValue The default value to be returned if the preference does not exist.
     * @param pname the preference name
     * */
    public static int getPreferenceInt(String key, int defaultValue)
    {

        return  getPreferenceInt(key, defaultValue, _GAME_PKG__STRING_);
    }
    public static int getPreferenceInt( String key, int defaultValue, String pname)
    {
        return getPreferenceInt(Utils.getContext(), key,  defaultValue,  pname);
    }
    public static int getPreferenceInt(Context context,String key, int defaultValue, String pname)
    {
        SharedPreferences settings = context.getSharedPreferences(pname, 0);
        // int res = settings.getInt(key, defaultValue);
        String res = settings.getString(Encoded(key), Encoded(String.valueOf(defaultValue)));
        Log.d("Utils", "getPreferenceInt(" + key + ", " + defaultValue + " " + Decode(res)  + ", " + pname + ")");
        int k = Integer.parseInt(Decode(res));
        return k;
    }

    /**
     * To read a long from the preferences file.
     * @param key The key that identifies the preferences file.
     * @param defaultValue The default value to be returned if the preference does not exist.
     * @param pname the preference name
     * */
    public static boolean getPreferenceBoolean(String key, boolean defaultValue, String pname)
    {
        return getPreferenceBoolean(Utils.getContext(), key,  defaultValue,  pname);
    }
    public static boolean getPreferenceBoolean(Context context,String key, boolean defaultValue, String pname)
    {
        try
        {
            SharedPreferences settings =context.getSharedPreferences(pname, 0);
            // boolean res = settings.getBoolean(key, defaultValue);
            // Log.d("Utils", "getPreferenceBoolean(" + key + ", " + defaultValue + " " + res  + ", " + pname + ")");
            String res = settings.getString(Encoded(key), Encoded(String.valueOf(defaultValue)));
            Log.d("Utils", "getPreferenceBoolean(" + key + ", " + defaultValue + " " + Decode(res)  + ", " + pname + ")");

            return Decode(res).equals("true");
        }catch(Exception ex){
            Log.d("Utils", "getPreferenceBoolean( Exception" + key + ", " + defaultValue + " " + false  + ", " + pname + ")");
            return false;}
    }

    /**
     * To read a long from the preferences file.
     * @param key The key that identifies the preferences file.
     * @param defaultValue The default value to be returned if the preference does not exist.
     * @param pname the preference name
     * */
    public static long getPreferenceLong(String key, long defaultValue, String pname)
    {
        return getPreferenceLong(Utils.getContext(), key,  defaultValue,  pname);
    }
    public static long getPreferenceLong(Context context,String key, long defaultValue, String pname)
    {
        SharedPreferences settings = context.getSharedPreferences(pname, 0);
        // Long res = settings.getLong(key, defaultValue);
        // Log.d("Utils", "getPreferenceLong(" + key + ", " + defaultValue + " " + res + ", " + pname + ")");
        String res = settings.getString(Encoded(key), Encoded(String.valueOf(defaultValue)));
        Log.d("Utils", "getPreferenceLong(" + key + ", " + defaultValue + " " + Decode(res)  + ", " + pname + ")");
        long k = Long.parseLong(Decode(res));
        return k;
    }

    public static void setArrayListStringPrefs(String arrayName, ArrayList<String> array, String pname) {
        SharedPreferences settings = Utils.getContext().getSharedPreferences(pname, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(arrayName +"_size", array.size());
        for(int i=0;i<array.size();i++)
            editor.putString(arrayName + "_" + i, array.get(i).toString());
        editor.commit();
    }

    public static ArrayList<String> getArrayListStringPrefs(String arrayName,ArrayList<String> defaultValue, String pname) {
        SharedPreferences settings = Utils.getContext().getSharedPreferences(pname, 0);
        int size = settings.getInt(arrayName + "_size", 0);
        if(size<=0) return defaultValue;
        ArrayList<String> array = new ArrayList<String>(size);
        for(int i=0;i<size;i++)
            array.add(settings.getString(arrayName + "_" + i, null));
        return array;
    }

    public static void ShowCustomPopUp(final Context ct,final String title,final String content,final DialogCustomFunctionCallback cb,final boolean hasCancel)
    {
        ((Activity)ct).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!((Activity) ct).isFinishing()) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(ct, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(ct);
                    }
                    if (hasCancel) {
                        builder.setTitle(title)
                                .setMessage(content)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        if (cb != null)
                                            cb.onOK(dialog);


                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                        dialog.dismiss();
                                        if (cb != null)
                                            cb.onCancel(dialog);
                                        ;
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        final Dialog dialog = new Dialog(ct);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        dialog.setContentView(R.layout.block_unlegal_user_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.gravity = Gravity.CENTER;
                        dialog.getWindow().setAttributes(lp);
                        dialog.getWindow().setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
                        cUtils.setFont(ct, (ViewGroup) ((ViewGroup)dialog.findViewById(android.R.id.content)).getChildAt(0));
                        Button OK = (Button)dialog.findViewById(R.id.btnOk);
                        TextView txtBlockMess = (TextView)dialog.findViewById(R.id.txtBlockMess);
                        TextView txtBlockTitle = (TextView)dialog.findViewById(R.id.txtBlockTitle);
                        txtBlockMess.setText(content);
                        if(title!=null && !title.equals(""))
                        {
                            txtBlockTitle.setText(title);
                        }
                        OK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(cb!=null)
                                    cb.onOK(dialog);
                                dialog.dismiss();
                            }
                        });
                        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                        {
                            @Override
                            public void onCancel(DialogInterface dialog)
                            {
                                if(cb!=null)
                                    cb.onOK(dialog);
                                dialog.dismiss();
                            }
                        });
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }
                else
                {
                    Log.d("","#quy ERRORRRRRRRR Crash null ShowCustomPopUp");
                }
            }

        });
    }
    public static void IAP_Not_ReadyPopUp(Context ct)
    {
        cUtils.ShowCustomPopUp(ct, "", ct.getString(R.string.IAP_NOT_AVAILABLE), new cUtils.DialogCustomFunctionCallback() {
            @Override
            public void onOK(DialogInterface dialog) {
                APP_PACKAGE.SelectGameActivity.playSound(APP_PACKAGE.SelectGameActivity.playerConfirm);
                dialog.dismiss();
            }

            @Override
            public void onCancel(DialogInterface dialog) {

            }
        },false);

    }
    public static void IAP_Error_PopUp(Context ct,int error)
    {
        String errorString = MC_IAP.GetErrorText(ct,error) +"("+error+")";
        cUtils.ShowCustomPopUp(ct, "",errorString , new cUtils.DialogCustomFunctionCallback() {
            @Override
            public void onOK(DialogInterface dialog) {
                APP_PACKAGE.SelectGameActivity.playSound(APP_PACKAGE.SelectGameActivity.playerConfirm);
                dialog.dismiss();
            }

            @Override
            public void onCancel(DialogInterface dialog) {

            }
        },false);

    }
    public static void ShowPopUpConnectionError()
    {
        ShowCustomPopUp(Utils.getContext(), "", Utils.getContext().getString(R.string.NO_INTERNET), new DialogCustomFunctionCallback() {
            @Override
            public void onOK(DialogInterface dialog) {
                APP_PACKAGE.SelectGameActivity.playSound(APP_PACKAGE.SelectGameActivity.playerConfirm);
                dialog.dismiss();
            }

            @Override
            public void onCancel(DialogInterface dialog) {

            }
        },false);
    }
    public static void setSpanText(SpannableString ss, Drawable d, View view){
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM){
            public void draw(Canvas canvas, CharSequence text, int start,
                             int end, float x, int top, int y, int bottom,
                             Paint paint) {
                Drawable b = getDrawable();
                canvas.save();
                int bCenter = b.getIntrinsicHeight() / 2;
                int fontTop = paint.getFontMetricsInt().top;
                int fontBottom = paint.getFontMetricsInt().bottom;
                int transY = (bottom - b.getBounds().bottom) -
                        (((fontBottom - fontTop) / 2) - bCenter);
                canvas.translate(x, transY);
                b.draw(canvas);
                canvas.restore();
            }
        };
        if(view instanceof Button) {
            ss.setSpan(span, ss.length() - 1, ss.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ((Button) view).setText(ss);
        }
        else if((view instanceof TextView))
        {
            ss.setSpan(span, ss.length() - 1, ss.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ((TextView) view).setText(ss);
        }
    }

    public static void setSpanTextn(String s, Drawable d, View view){
        SpannableString ss = new SpannableString(s);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM){
            public void draw(Canvas canvas, CharSequence text, int start,
                             int end, float x, int top, int y, int bottom,
                             Paint paint) {
                Drawable b = getDrawable();
                canvas.save();
                int bCenter = b.getIntrinsicHeight() / 2;
                int fontTop = paint.getFontMetricsInt().top;
                int fontBottom = paint.getFontMetricsInt().bottom;
                int transY = (bottom - b.getBounds().bottom) -
                        (((fontBottom - fontTop) / 2) - bCenter);
                canvas.translate(x, transY);
                b.draw(canvas);
                canvas.restore();
            }
        };
        if(view instanceof Button) {
            ss.setSpan(span, s.indexOf('*'), s.indexOf('*')+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ((Button) view).setText(ss);
        }
        else if((view instanceof TextView))
        {
            ss.setSpan(span, s.indexOf('*'), s.indexOf('*')+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ((TextView) view).setText(ss);
        }
    }


    public static void setSpanTextPosition(Drawable d, TextView view){
        // SpannableString ss1 = new SpannableString(getString(R.string.watch_video_to_skip));
        String s="Get more * with daily Free Gacha or watching Ad Videos to unlock more Games.";
        SpannableString ss = new SpannableString(s);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM) {
            public void draw(Canvas canvas, CharSequence text, int start,
                             int end, float x, int top, int y, int bottom,
                             Paint paint) {
                Drawable b = getDrawable();
                canvas.save();

                int bCenter = b.getIntrinsicHeight() / 2;
                int fontTop = paint.getFontMetricsInt().top;
                int fontBottom = paint.getFontMetricsInt().bottom;
                int transY = (bottom - b.getBounds().bottom) - (((fontBottom - fontTop) / 2) - bCenter);
                canvas.translate(x, transY);
                b.draw(canvas);
                canvas.restore();
            }
        };
        ss.setSpan(span,s.indexOf('*'),s.indexOf('*')+1, 0);
        view.setText(ss);
    }

    public static void setSpanTextPositionOne(Drawable d, TextView view, String s){
        // SpannableString ss1 = new SpannableString(getString(R.string.watch_video_to_skip));
        // String s="Get more * with daily Free Gacha or watching Ad Videos to unlock more Games.";
        SpannableString ss = new SpannableString(s);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM) {
            public void draw(Canvas canvas, CharSequence text, int start,
                             int end, float x, int top, int y, int bottom,
                             Paint paint) {
                Drawable b = getDrawable();
                canvas.save();

                int bCenter = b.getIntrinsicHeight() / 2;
                int fontTop = paint.getFontMetricsInt().top;
                int fontBottom = paint.getFontMetricsInt().bottom;
                int transY = (bottom - b.getBounds().bottom) - (((fontBottom - fontTop) / 2) - bCenter);
                canvas.translate(x, transY);
                b.draw(canvas);
                canvas.restore();
            }
        };
        ss.setSpan(span,s.indexOf('*'),s.indexOf('*')+1, 0);
        view.setText(ss);
    }

    public static void FillAlpha(ViewGroup viewGroup,int[] idSkip)
    {
        for (int i=0; i< viewGroup.getChildCount();i++)
        {
            View view =viewGroup.getChildAt(i);
            if(!isViewEquals(view,idSkip))
            {
                view.setBackgroundColor(0x88000000);
            }
        }
    }
    static boolean isViewEquals(View view, int[] id)
    {
        for(int j =0; j<id.length;j++)
        {

            if(id[j] == view.getId())
            {
                return true;
            }
        }
        return false;
    }
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static void setSpanText(final String string,final int[] drawable,final View view){
        view.post(new Runnable() {
            @Override
            public void run() {
                int nextIcon =0;
                SpannableString ss = new SpannableString(string);
                for(int i = 0; i <ss.length();i++) {
                    if(drawable.length >0) {
                        if(drawable.length>nextIcon) {
                            Drawable d=view.getContext().getResources().getDrawable(drawable[nextIcon]);;

                            Bitmap b = ((BitmapDrawable)d).getBitmap();
                            float size =0;
                            if (view instanceof Button) {

                                size =((Button)view).getTextSize();
                            }
                            else if(view instanceof TextView)
                            {
                                size =((TextView)view).getTextSize();
                            }
                            if(size!=0)
                            {
                                TextPaint textpaint = new TextPaint((TextPaint.ANTI_ALIAS_FLAG));
                                textpaint.setTextSize((size));
                                Rect bounds = new Rect();
                                textpaint.getTextBounds(string, 0, string.length(), bounds);
                                float rotai = bounds.height()/(float)d.getIntrinsicHeight();
                                Log.d("","#quy d.getIntrinsicHeight()" + d.getIntrinsicHeight());
                                Log.d("","#quyview.getHeight()" + bounds.height());
                                Log.d("","#quy rotai" + rotai);
                                int increaPercent =1;
                                Bitmap bitmapResized = Bitmap.createScaledBitmap(b,(int)(rotai* d.getIntrinsicWidth()  + bounds.height()*increaPercent/100), bounds.height() + bounds.height()*increaPercent/100, true);
                                d =new BitmapDrawable(view.getContext().getResources(), bitmapResized);
                            }
                            else
                            {
                                float rotai = view.getHeight()/(float)d.getIntrinsicHeight();
//                                Log.d("","d.getIntrinsicHeight()" + d.getIntrinsicHeight());
//                                Log.d("","view.getHeight()" + view.getHeight());
//                                Log.d("","rotai" + rotai);
                                Bitmap bitmapResized = Bitmap.createScaledBitmap(b,(int)(rotai* d.getIntrinsicWidth()), view.getHeight(), true);
                                d =new BitmapDrawable(view.getContext().getResources(), bitmapResized);
                            }
                            nextIcon++;
                            if(nextIcon == drawable.length)
                            {
                                nextIcon =  drawable.length -1;
                            }
                            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM) {
                                public void draw(Canvas canvas, CharSequence text, int start,
                                                 int end, float x, int top, int y, int bottom,
                                                 Paint paint) {
                                    Drawable b = getDrawable();
                                    canvas.save();
                                    int bCenter = b.getIntrinsicHeight() / 2;
                                    int fontTop = paint.getFontMetricsInt().top;
                                    int fontBottom = paint.getFontMetricsInt().bottom;
                                    int transY = (bottom - b.getBounds().bottom) -
                                            (((fontBottom - fontTop) / 2) - bCenter);
                                    canvas.translate(x, transY);
                                    b.draw(canvas);
                                    canvas.restore();
                                }
                            };
                            if (ss.charAt(i) == '*') {
                                if (view instanceof Button) {
                                    ss.setSpan(span, i, i + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                    ((Button) view).setText(ss);
                                } else if ((view instanceof TextView)) {
                                    ss.setSpan(span, i, i + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                    ((TextView) view).setText(ss);
                                }
                            }
                        }
                    }
                }
            }
        });

    }
    public static int ByteArrayToInt(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }
    public static byte[] intToByteArray(int a)
    {
        byte[] ret = new byte[4];
        ret[3] = (byte) (a & 0xFF);
        ret[2] = (byte) ((a >> 8) & 0xFF);
        ret[1] = (byte) ((a >> 16) & 0xFF);
        ret[0] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }

    public static void setPreviousActivity (int PreviousActivity)
    {
        s_previousActivity = PreviousActivity;
    }

    public static int getPerviousActivity (){
        return s_previousActivity;
    }

    public interface DialogCustomFunctionCallback
    {
        void onOK(DialogInterface dialog);
        void onCancel(DialogInterface dialog);
    }
    public static void ShowMandatoryCloud(Context ct,final DialogCustomFunctionCallback cb)
    {
        if(!((Activity)ct).isFinishing())
        {

            final Dialog dialog = new Dialog(ct);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dialog.setContentView(R.layout.cloud_save_mandatory);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
            cUtils.setFont(ct, (ViewGroup) ((ViewGroup)dialog.findViewById(android.R.id.content)).getChildAt(0));
            Button OK = (Button)dialog.findViewById(R.id.btnOk);
            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    APP_PACKAGE.SelectGameActivity.playSound(APP_PACKAGE.SelectGameActivity.playerConfirm);
                    cb.onOK(dialog);
                    dialog.dismiss();
                }
            });

            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }
        else
        {
            Log.d("","#cloud ShowMandatoryCloud Activity is closed");
        }
    }
    public static void ShowOptionalCloud(Context ct,int coin,final DialogCustomFunctionCallback cb)
    {
        if(!((Activity)ct).isFinishing())
        {

            final Dialog dialog = new Dialog(ct);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dialog.setContentView(R.layout.cloud_save_optional);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
            cUtils.setFont(ct, (ViewGroup) ((ViewGroup)dialog.findViewById(android.R.id.content)).getChildAt(0));
            Button OK = (Button)dialog.findViewById(R.id.btnRestore);
            TextView txtCoin = (TextView)dialog.findViewById(R.id.txtCoin);
            TextView txtBlockTitle = (TextView)dialog.findViewById(R.id.txtBlockTitle);
            txtCoin.setText(String.valueOf(coin));
            txtBlockTitle.setText(ct.getString(R.string.customer_care_send_you).toUpperCase());
            AnimationView coin_animation =(AnimationView)dialog.findViewById(R.id.coin_animation);
            coin_animation.SetAnimationData(new ASprite(ct).Load("Anim_coin.sprite"),0);
            coin_animation.setAlignment(coin_animation.LEFT);
            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cb.onOK(dialog);
                    dialog.dismiss();
                }
            });
            Button Cancel = (Button)dialog.findViewById(R.id.btnDiscard);
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    APP_PACKAGE.SelectGameActivity.playSound(APP_PACKAGE.SelectGameActivity.playerConfirm);
                    cb.onCancel(dialog);
                    dialog.dismiss();
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }
        else
        {
            Log.d("","#cloud ShowOptionalCloud Activity is closed");
        }
    }
    public static String Encoded(String k)
    {
        try {
            return _encrypt(k, String.valueOf(getSecreteKey(Utils.getDeviceId()))).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String Decode(String k)
    {
        try {
            String content = _decrypt(k,String.valueOf(getSecreteKey(Utils.getDeviceId())));
            // Log.d("","#cloud decode" + content);
            return content.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    //    public static String ALGO = "DESede/CBC/PKCS7Padding";
    public static String ALGO = "DESede/ECB/PKCS7Padding";
    public static String _encrypt(String message, String secretKey) throws Exception {

        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, getSecreteKey(secretKey));
        byte[] plainTextBytes = message.getBytes("UTF-8");
        byte[] buf = cipher.doFinal(plainTextBytes);
        byte[] base64Bytes =android.util.Base64.encode(buf, android.util.Base64.DEFAULT);
        String base64EncryptedString = new String(base64Bytes);
        // Log.d("","#cloud _encrypt" + base64EncryptedString);
        return base64EncryptedString;
    }

    public static String _decrypt(String encryptedText, String secretKey) throws Exception {

        byte[] message = android.util.Base64.decode(encryptedText.getBytes(), android.util.Base64.DEFAULT);

        Cipher decipher = Cipher.getInstance(ALGO);
        decipher.init(Cipher.DECRYPT_MODE, getSecreteKey(secretKey));

        byte[] plainText = decipher.doFinal(message);

        return new String(plainText, "UTF-8");
    }

    public static SecretKey getSecreteKey(String secretKey) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        return key;
    }
    public static long m_deviceUpTimeMillis = 0;
    public static long s_elapsedRealTimeMillis = 0;
    public static long GetElapsedRealTime ()
    {
        long lastDeviceUpTime = m_deviceUpTimeMillis;
        m_deviceUpTimeMillis = android.os.SystemClock.elapsedRealtime();

        if ( s_elapsedRealTimeMillis == 0 )
        {
            s_elapsedRealTimeMillis = System.currentTimeMillis();
        }
        else
        {
            long timeElapsed;

            if ( lastDeviceUpTime > m_deviceUpTimeMillis )
            {
                timeElapsed = m_deviceUpTimeMillis;
            }
            else
            {
                timeElapsed = m_deviceUpTimeMillis - lastDeviceUpTime;
            }

            s_elapsedRealTimeMillis += timeElapsed;
        }

        return s_elapsedRealTimeMillis;
    }
    final static String ANTI_HACK_TIME ="ANTI_HACK_TIME_s_elapsedRealTimeMillis";
    final static String ANTI_HACK_TIME_2 ="ANTI_HACK_TIME_m_deviceUpTimeMillis";
    final static String ANTI_HACK_IAP_PENDING_TRANS ="ANTI_HACK_IAP_PENDING_TRANS";
    public static void SaveTimeAntiHack()
    {
        setPreference(ANTI_HACK_TIME,s_elapsedRealTimeMillis,_GAME_PKG__STRING_);
        setPreference(ANTI_HACK_TIME_2,m_deviceUpTimeMillis ,_GAME_PKG__STRING_);
    }
    public static void getAntiHackTime()
    {
        s_elapsedRealTimeMillis = getPreferenceLong(ANTI_HACK_TIME,0,_GAME_PKG__STRING_);
        m_deviceUpTimeMillis = getPreferenceLong(ANTI_HACK_TIME_2,0,_GAME_PKG__STRING_);
//        if(s_elapsedRealTimeMillis == 0)
//        {
//            GetElapsedRealTime();
//            GetElapsedRealTime();
//            SaveTimeAntiHack();
//        }

    }
    public static void setCountLaunchGame(int content)
    {
        setPreference("COUNT_LAUNCH",content,_GAME_PKG__STRING_);
    }
    public static int getCountLaunchGame()
    {
        return getPreferenceInt("COUNT_LAUNCH",0,_GAME_PKG__STRING_);
    }

    public static void setCountLaunchGameAfterPassAgeGate(int i)
    {
        setPreference("COUNT_LAUNCH_PASS_AGE_GATE",i,_GAME_PKG__STRING_);
    }

    public static int getCountLaunchGameAfterPassAgeGate()
    {
        return getPreferenceInt("COUNT_LAUNCH_PASS_AGE_GATE",0,_GAME_PKG__STRING_);
    }

    public static boolean isReadyToShowRatingPopup() {
        if (getCountLaunchGameAfterPassAgeGate() >= 2) {
            return true;
        }
        return false;
    }

    public static void setIsNeverShowRatingPopUp(boolean isNeverShow)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Utils.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IS_NEVER_SHOW", isNeverShow);
        editor.commit();
    }

    public static boolean getIsNeverShowRatingPopUp()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Utils.getContext());
        return sharedPreferences.getBoolean("IS_NEVER_SHOW",false);
    }

    public static void AddBoughtIAPItemPendingTrans(String id)
    {
        String list = getPreferenceString(ANTI_HACK_IAP_PENDING_TRANS,"",_GAME_PKG__STRING_);
        if(list.indexOf(id)!=-1) {
            Log.d("","#IAP We have this id" + id);
            return;
        }
        list+=id+";";
        setPreference(ANTI_HACK_IAP_PENDING_TRANS,list,_GAME_PKG__STRING_);
        Log.d("","#IAP Saving bought ID" + id);
    }
    public static boolean HasAddedPendingItiem(String id)
    {
        String str = getPreferenceString(ANTI_HACK_IAP_PENDING_TRANS,"",_GAME_PKG__STRING_);

        if(str.indexOf(id)!=-1) {
            Log.d("","#IAP We have this id" + id);
            return true;
        }
        return false;
    }

    public static void setSpanTextTwoIcon(String s, Drawable d,Drawable d1, View view){
        SpannableString ss = new SpannableString(s);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM){
            public void draw(Canvas canvas, CharSequence text, int start,
                             int end, float x, int top, int y, int bottom,
                             Paint paint) {
                Drawable b = getDrawable();
                canvas.save();
                int bCenter = b.getIntrinsicHeight() / 2;
                int fontTop = paint.getFontMetricsInt().top;
                int fontBottom = paint.getFontMetricsInt().bottom;
                int transY = (bottom - b.getBounds().bottom) -
                        (((fontBottom - fontTop) / 2) - bCenter);
                canvas.translate(x, transY);
                b.draw(canvas);
                canvas.restore();
            }
        };
        d1.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span1 = new ImageSpan(d1, ImageSpan.ALIGN_BOTTOM){
            public void draw(Canvas canvas, CharSequence text, int start,
                             int end, float x, int top, int y, int bottom,
                             Paint paint) {
                Drawable b = getDrawable();
                canvas.save();
                int bCenter = b.getIntrinsicHeight() / 2;
                int fontTop = paint.getFontMetricsInt().top;
                int fontBottom = paint.getFontMetricsInt().bottom;
                int transY = (bottom - b.getBounds().bottom) -
                        (((fontBottom - fontTop) / 2) - bCenter);
                canvas.translate(x, transY);
                b.draw(canvas);
                canvas.restore();
            }
        };
        if(view instanceof Button) {
            ss.setSpan(span, s.indexOf('*'), s.indexOf('*')+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ss.setSpan(span1, s.indexOf('#'), s.indexOf('#')+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ((Button) view).setText(ss);

        }
        else if((view instanceof TextView))
        {
            ss.setSpan(span, s.indexOf('*'), s.indexOf('*')+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ss.setSpan(span1, s.indexOf('#'), s.indexOf('#')+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ((TextView) view).setText(ss);
        }
    }
    private static char[] getCharFromVersion (String version)
    {
        if(version == null)
            return null;

        String[] strVersionArray = changeVerisonToArray(version);
        StringBuffer strBuffer = new StringBuffer();
        int iValue = 4 - strVersionArray.length;
        if(iValue > 0)
        {
            for(int i = 0; i < iValue; i++)
            {
                strBuffer.append('0');
            }

            for(int i = 0; i < strVersionArray.length; i++)
            {
                strBuffer.append(strVersionArray[i].toCharArray()[0]);
            }
        }
        else
        {
            for(int i = 0; i < 4; i++)
            {
                strBuffer.append(strVersionArray[i].toCharArray()[0]);
            }
        }

        // version number
        char[] chVersionArray = strBuffer.toString().toCharArray();
        return chVersionArray;
    }

    public static int getIntFromIGP (String igpCode)
    {
        if(igpCode == null)
            return 0;
        int code = 0;
        for(int i = 0; i < 4; i ++)
        {
            code *= 256;
            if(igpCode.length() >= i)
            {
                code += igpCode.charAt(i);
            }
        }
        return code;
    }
    public static String[] changeVerisonToArray(String strSrc)
    {
        String strSeparator = ".";
        int iCount = 0;
        int iOffset = 0;
        int iNext = 0;

        while((iNext = strSrc.indexOf(strSeparator, iOffset)) != -1)
        {
            iOffset = iNext + 1;
            ++iCount;
        }

        // if no match was found, reture original string
        if(iOffset == 0)
        {
            return new String[] { strSrc };
        }

        String[] resultArray = new String[iCount+1];
        iCount = 0;
        iOffset = 0;
        iNext = 0;
        while((iNext = strSrc.indexOf(strSeparator, iOffset)) != -1)
        {
            resultArray[iCount] = strSrc.substring(iOffset, iNext);
            iOffset = iNext + 1;
            ++iCount;
        }

        // add remaining segment
        resultArray[iCount] = strSrc.substring(iOffset, strSrc.length());

        return resultArray;
    }

    public static void setFullScreenPopup(Dialog dialog) {
        View decorView = dialog.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            // lower api
            decorView.setSystemUiVisibility(View.GONE);
        }
        else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static final int STATUS_UPDATE_VERSION_NONE = 0;
    public static final int STATUS_UPDATE_VERSION_OPTIONAL = 1;
    public static final int STATUS_UPDATE_VERSION_MANDATORY = 2;
    public static final int STATUS_VERSION_BLOCKING_FORCE_TO_UPDATE = 3;
    public static final int STATUS_UPDATE_VERSION_OPTIONAL_WITH_WIFI = 4;
    public static final int STATUS_UPDATE_VERSION_MANDATORY_WITH_WIFI = 5;

    public static final int SCREEN_NVS_OPTIONAL = 274647;
    public static final int SCREEN_NVS_UPGRADE_REQUIRED = 274648;
    public static final int NVS_TRACKING_START_UPDATE = 274649;
    public static final int NVS_TRACKING_SCR_GAMESTATE = 238556;

    public static int mUpdateVersionStatus;
    public static boolean isCheckUpdateStatus;
    public static boolean isNeedToShowUpdateDialog = true;

    public static void CheckVersionUpdateStatus()
    {
        int iStatus = AutoUpdate.getStatus();

        if (iStatus == AutoUpdate.UPDATE_VERSION_OPTIONAL){
            mUpdateVersionStatus = STATUS_UPDATE_VERSION_OPTIONAL;
        }
        else if (iStatus == AutoUpdate.UPDATE_VERSION_MANDATORY){
            mUpdateVersionStatus = STATUS_UPDATE_VERSION_MANDATORY;
        }
        else if (iStatus == AutoUpdate.VERSION_BLOCKING_FORCE_TO_UPDATE){
            mUpdateVersionStatus = STATUS_VERSION_BLOCKING_FORCE_TO_UPDATE;
        }
        else if (iStatus == AutoUpdate.UPDATE_VERSION_OPTIONAL_WITH_WIFI){
            mUpdateVersionStatus = STATUS_UPDATE_VERSION_OPTIONAL_WITH_WIFI;
        }
        else if (iStatus == AutoUpdate.UPDATE_VERSION_MANDATORY_WITH_WIFI){
            mUpdateVersionStatus = STATUS_UPDATE_VERSION_MANDATORY_WITH_WIFI;
        }
        else{
            mUpdateVersionStatus = STATUS_UPDATE_VERSION_NONE;
        }
        isNeedToShowUpdateDialog = mUpdateVersionStatus != STATUS_UPDATE_VERSION_NONE;
    }

    public static void CheckUpdateVersionDialog(final Context ct)
    {
        System.out.println("AutoUpdate - Nguyen CheckUpdateVersionDialog");
        PopupManager popupManager = PopupManager.getInstance();
        if (mUpdateVersionStatus != STATUS_UPDATE_VERSION_NONE)
        {
            if (mUpdateVersionStatus == STATUS_UPDATE_VERSION_OPTIONAL){

                System.out.println("AutoUpdate - Nguyen 1");
                popupManager.checkShowPopup(popupManager.NVS_POPUP_ID, new PopupManager.PopupCallback() {
                    @Override
                    public void onShowPopup() {
                        showNewVersionUpdate(STATUS_UPDATE_VERSION_OPTIONAL, ct);
                    }
                });
            }
            else if (mUpdateVersionStatus == STATUS_UPDATE_VERSION_MANDATORY){
                System.out.println("AutoUpdate - Nguyen 2"); //
                popupManager.checkShowPopup(popupManager.NVS_POPUP_ID, new PopupManager.PopupCallback() {
                    @Override
                    public void onShowPopup() {
                        showNewVersionUpdate(STATUS_UPDATE_VERSION_MANDATORY, ct);
                    }
                });
            }
            else if (mUpdateVersionStatus == STATUS_VERSION_BLOCKING_FORCE_TO_UPDATE){
                System.out.println("AutoUpdate - Nguyen 3"); //
                popupManager.checkShowPopup(popupManager.NVS_POPUP_ID, new PopupManager.PopupCallback() {
                    @Override
                    public void onShowPopup() {
                        showNewVersionUpdate(STATUS_VERSION_BLOCKING_FORCE_TO_UPDATE, ct);
                    }
                });
            }
            else if (mUpdateVersionStatus == STATUS_UPDATE_VERSION_OPTIONAL_WITH_WIFI){
                System.out.println("AutoUpdate - Nguyen 4");
                popupManager.checkShowPopup(popupManager.NVS_POPUP_ID, new PopupManager.PopupCallback() {
                    @Override
                    public void onShowPopup() {
                        showNewVersionUpdate(STATUS_UPDATE_VERSION_OPTIONAL_WITH_WIFI, ct);
                    }
                });
            }
            else if (mUpdateVersionStatus == STATUS_UPDATE_VERSION_MANDATORY_WITH_WIFI){
                System.out.println("AutoUpdate - Nguyen 5"); //
                popupManager.checkShowPopup(popupManager.NVS_POPUP_ID, new PopupManager.PopupCallback() {
                    @Override
                    public void onShowPopup() {
                        showNewVersionUpdate(STATUS_UPDATE_VERSION_MANDATORY_WITH_WIFI, ct);
                    }
                });
            }
            isCheckUpdateStatus = true;
            mUpdateVersionStatus = STATUS_UPDATE_VERSION_NONE;
        }
    }

    public static void showNewVersionUpdate(int isStatus, final Context ct) {
        System.out.println("AutoUpdate - showNewVersionUpdate");
        SelectGameActivity.s_isShowNVS = true;
        MCG_GLOT.setIsOpenActivity(); // Avoid pause tracking when show popup NVS

        MenuLanguageActivity.updateLanguages();

        final Dialog dialog = new Dialog(ct);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cUtils.setFullScreenPopup(dialog);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if ((isStatus == STATUS_UPDATE_VERSION_MANDATORY) || (isStatus == STATUS_VERSION_BLOCKING_FORCE_TO_UPDATE) || (isStatus == STATUS_UPDATE_VERSION_MANDATORY_WITH_WIFI)) {
            System.out.println("AutoUpdate - showNewVersionUpdate - nvs_upgrade_required_dialog");
            if (TrackingManager.GetManager() != null) {
                TrackingManager.GetManager().AddEvent(51909,
                        new String[]{
                                (SelectGameActivity.isFirstLaunch())?("INT:" + Integer.toString(MCG_GLOT.TRACKING_SCREEN_GAME_STATE_SCREEN))
                                        :("INT:" + Integer.toString(MCG_GLOT.TRACKING_SCREEN_MAIN_MENU)),
                                "INT:" + Integer.toString(SCREEN_NVS_UPGRADE_REQUIRED)}
                );
            }
            dialog.setContentView(R.layout.nvs_upgrade_required_dialog);
        } else {
            System.out.println("AutoUpdate - showNewVersionUpdate - nvs_optional_dialog");
            if (TrackingManager.GetManager() != null) {
                TrackingManager.GetManager().AddEvent(51909,
                        new String[]{
                                (SelectGameActivity.isFirstLaunch())?("INT:" + Integer.toString(MCG_GLOT.TRACKING_SCREEN_GAME_STATE_SCREEN))
                                        :( "INT:" + Integer.toString(MCG_GLOT.TRACKING_SCREEN_MAIN_MENU)),
                                "INT:" + Integer.toString(MCG_Online.isMandatoryUpdate? SCREEN_NVS_UPGRADE_REQUIRED : SCREEN_NVS_OPTIONAL)}
                );
            }
            dialog.setContentView(R.layout.nvs_optional_dialog);
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        cUtils.setFont(Utils.getContext(), (ViewGroup)((ViewGroup) dialog.findViewById(android.R.id.content)).getChildAt(0));

        final TextView tvAvailableStatus = dialog.findViewById(R.id.tvAvailableStatus);
        final TextView tvConnectionStatus = dialog.findViewById(R.id.tvConnectionStatus);
        final ImageButton btnSetting = dialog.findViewById(R.id.btnSetting);
        final Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        final Button btnNo = dialog.findViewById(R.id.btnNo);
        final LinearLayout connectionLayout = dialog.findViewById(R.id.connectionLayout);

        String strMessage = "";
        if (!Utils.hasConnectivity()) {
            connectionLayout.setVisibility(View.VISIBLE);
        }

        if (isStatus == STATUS_UPDATE_VERSION_OPTIONAL) {
            strMessage = Utils.getContext().getString(R.string.UPDATE_NON_MANDATORY);
        }
        else if (isStatus == STATUS_UPDATE_VERSION_MANDATORY) {
            strMessage = Utils.getContext().getString(R.string.UPDATE_MANDATORY);
        }
        else if (isStatus == STATUS_VERSION_BLOCKING_FORCE_TO_UPDATE) {
            strMessage = Utils.getContext().getString(R.string.UPDATE_MANDATORY);
        }
        else if (isStatus == STATUS_UPDATE_VERSION_OPTIONAL_WITH_WIFI) {
            strMessage = Utils.getContext().getString(R.string.UPDATE_NON_MANDATORY);
            tvConnectionStatus.setText(Utils.getContext().getString(R.string.NVS_NEED_WIFI));
            if (!Utils.isConnectedWifi()) {
                connectionLayout.setVisibility(View.VISIBLE);
            }
        }
        else if (isStatus == STATUS_UPDATE_VERSION_MANDATORY_WITH_WIFI) {
            strMessage = Utils.getContext().getString(R.string.UPDATE_MANDATORY);
            tvConnectionStatus.setText(Utils.getContext().getString(R.string.NVS_NEED_WIFI));
            if (!Utils.isConnectedWifi()) {
                connectionLayout.setVisibility(View.VISIBLE);
            }
        }

        // #if CLASSIC_NVS_GETTEXT_FROM_SERVER
        if (MCG_Online.s_hasOnlineTextAvailable && MCG_Online.s_NVSOnlineText != null && !MCG_Online.s_NVSOnlineText.equals("")) {
            strMessage = MCG_Online.s_NVSOnlineText;
        }
        // #endif

        tvAvailableStatus.setText(strMessage);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectGameActivity.playSound(SelectGameActivity.playerConfirm);

                if (Utils.hasConnectivity()) //network available
                {
                    int status = AutoUpdate.getStatus();
                    if (((status == STATUS_UPDATE_VERSION_OPTIONAL) && Utils.hasConnectivity()) ||
                            ((status == STATUS_UPDATE_VERSION_OPTIONAL_WITH_WIFI) && Utils.isConnectedWifi()) ||
                            ((status == STATUS_VERSION_BLOCKING_FORCE_TO_UPDATE) && Utils.hasConnectivity()) ||
                            ((status == STATUS_UPDATE_VERSION_MANDATORY) && Utils.hasConnectivity()) ||
                            ((status == STATUS_UPDATE_VERSION_MANDATORY_WITH_WIFI) && Utils.isConnectedWifi())) {
                        if (TrackingManager.GetManager() != null) {
                            TrackingManager.GetManager().AddEvent(51909,
                                    new String[] {
                                            "INT:" + Integer.toString(MCG_Online.isMandatoryUpdate ? SCREEN_NVS_UPGRADE_REQUIRED : SCREEN_NVS_OPTIONAL),
                                            "INT:" + Integer.toString(NVS_TRACKING_START_UPDATE)
                                    }
                            );
                        }

                        String langGGPU;
                        if (MenuLanguageActivity.getCurrentLanguage().equals("sp"))
                            langGGPU = "es";
                        else if (MenuLanguageActivity.getCurrentLanguage().equals("le"))
                            langGGPU = "es_latam";
                        else
                            langGGPU = MenuLanguageActivity.getCurrentLanguage();
                        try {
                            String directLinkToGGP = Utils.GenerateLinkUpdateNewVersion(langGGPU);
                            Intent googlePlayUpdate = new Intent(Intent.ACTION_VIEW, Uri.parse(directLinkToGGP));
                            Utils.getContext().startActivity(googlePlayUpdate);
                            if (status == STATUS_UPDATE_VERSION_OPTIONAL || status == STATUS_UPDATE_VERSION_OPTIONAL_WITH_WIFI)
                            {
                                dialog.dismiss();
                                PopupManager.getInstance().checkShowQueuePopup();
                            }
                        } catch (Exception e) {
                            SelectGameActivity.ShowErrorBrowserPop();
                        }
                    } else if (((status == STATUS_UPDATE_VERSION_OPTIONAL_WITH_WIFI) && !Utils.isConnectedWifi()) ||
                            ((status == STATUS_UPDATE_VERSION_MANDATORY_WITH_WIFI) && !Utils.isConnectedWifi())) {
                        connectionLayout.setVisibility(View.VISIBLE);
                    }
                }
                else //lost connection
                {
                    connectionLayout.setVisibility(View.VISIBLE);
                }

                if (tvConnectionStatus.getVisibility() == View.VISIBLE) {
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(200); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(6);
                    tvConnectionStatus.startAnimation(anim);
                }
            }
        });
        btnUpdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Utils.vibrate();
                        break;
                }
                return false;
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    if (TrackingManager.GetManager() != null) {
                        TrackingManager.GetManager().AddEvent(51909,
                                new String[]{
                                        "INT:" + Integer.toString(MCG_Online.isMandatoryUpdate? SCREEN_NVS_UPGRADE_REQUIRED : SCREEN_NVS_OPTIONAL),
                                        (SelectGameActivity.isFirstLaunch())?( "INT:" + Integer.toString(MCG_GLOT.TRACKING_SCREEN_GAME_STATE_SCREEN)):("INT:" + Integer.toString(MCG_GLOT.TRACKING_SCREEN_MAIN_MENU))}
                        );
                    }
                    dialog.dismiss();
                    SelectGameActivity.playSound(SelectGameActivity.playerConfirm);
					#if CLASSIC_USE_ONLINE_BANNING
                    // checkShowBanningPopup();
					#endif
                    PopupManager.getInstance().checkShowQueuePopup();
                }
            }
        });
        btnNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Utils.vibrate();
                        break;
                }
                return false;
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectGameActivity.playSound(SelectGameActivity.playerConfirm);
                Utils.getActivity().startActivityForResult(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS), 0);
            }
        });

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    int status = AutoUpdate.getStatus();
                    if ((status == STATUS_UPDATE_VERSION_OPTIONAL) || (status == STATUS_UPDATE_VERSION_OPTIONAL_WITH_WIFI)) {
                        if (dialog.isShowing())
                        {
                            if (TrackingManager.GetManager() != null) {
                                TrackingManager.GetManager().AddEvent(51909,
                                        new String[]{
                                                "INT:" + Integer.toString(MCG_Online.isMandatoryUpdate? SCREEN_NVS_UPGRADE_REQUIRED : SCREEN_NVS_OPTIONAL),
                                                (SelectGameActivity.isFirstLaunch())?( "INT:" + Integer.toString(MCG_GLOT.TRACKING_SCREEN_GAME_STATE_SCREEN)):("INT:" + Integer.toString(MCG_GLOT.TRACKING_SCREEN_MAIN_MENU))}
                                );
                            }
                            dialog.dismiss();
                            #if CLASSIC_USE_ONLINE_BANNING
                            // checkShowBanningPopup();
                            #endif
                        }
                    }
                    else {
                        Intent myIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME);
                        Utils.getContext().startActivity(myIntent);
                    }
                }
                return true;
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void CheckNVS(Context ct)
    {
        System.out.println("AutoUpdate - isCheckUpdateStatus111111111111 = "+ cUtils.isCheckUpdateStatus);
        if(!cUtils.isCheckUpdateStatus){
            System.out.println("AutoUpdate - CheckVersionUpdateStatus");
            cUtils.CheckVersionUpdateStatus();
        }
        System.out.println("AutoUpdate - isNeedToShowUpdateDialog22222222222222 = "+ cUtils.isNeedToShowUpdateDialog);
        if(cUtils.isNeedToShowUpdateDialog){
            System.out.println("AutoUpdate - CheckUpdateVersionDialog");
            cUtils.CheckUpdateVersionDialog(ct);
        }
    }

    public static void checkShowBanningPopup()
    {
        if (MCG_Online.s_isBannedPlayer && !MCG_Online.BanMessage.equals(""))
            SelectGameActivity.isShowBanningPopup=true;
        else
            SelectGameActivity.isShowBanningPopup=false;

        if (Utils.hasConnectivity())
        {
            Log.d("Nguyen", "Nguyen - ONLINE_BANNING------- HAS connection");
            if (MCG_Online.s_isBannedPlayer && !MCG_Online.BanMessage.equals("") && MCG_Online.isBannedFromGame)
            {
                Log.d("Nguyen", "Nguyen - ONLINE_BANNING------- 1");
                SelectGameActivity.showBanPopup(Utils.getContext());
            }
        }
        else
        {
            Log.d("Nguyen", "Nguyen - ONLINE_BANNING------- LOST connection");
            if (MCG_Online.s_isBannedPlayer && !MCG_Online.BanMessage.equals(""))
            {
                Log.d("Nguyen", "Nguyen - ONLINE_BANNING------- 2");
                SelectGameActivity.showBanPopup(Utils.getContext());
            }
        }
    }
}
