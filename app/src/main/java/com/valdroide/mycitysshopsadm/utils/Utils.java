package com.valdroide.mycitysshopsadm.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.main.account.ui.AccountActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Utils {

//    public static String URL_IMAGE = "http://10.0.2.2:8080/my_citys_shops_adm/account/image_account/";
//    public static String URL_IMAGE_OFFER = "http://10.0.2.2:8080/my_citys_shops_adm/offer/image_offer/";
    // public static String URL_IMAGE = "http://10.0.3.2:8080/my_citys_shops_adm/account/image_account/";
    // public static String URL_IMAGE_OFFER = "http://10.0.3.2:8080/my_citys_shops_adm/offer/image_offer/";

    //public static String URL_IMAGE = "http://myd.esy.es/myd/clothes/image_clothes/";
//    public static String URL_IMAGE = "http://myd.esy.es/my_citys_shops_adm/account/image_account/";
//    public static String URL_IMAGE_OFFER = "http://myd.esy.es/my_citys_shops_adm/offer/image_offer/";

    //DEB
//    public static String URL_IMAGE = "http://mycitysshops.esy.es/deb/my_citys_shops_adm/account/image_account/";
//    public static String URL_IMAGE_OFFER = "http://mycitysshops.esy.es/deb/my_citys_shops_adm/offer/image_offer/";
 //   public static String URL_IMAGE_DRAW = "http://mycitysshops.esy.es/deb/my_citys_shops_adm/draw/image_draw/";
    //PRODUCT
    public static String URL_IMAGE = "http://mycitysshops.esy.es/my_citys_shops_adm/account/image_account/";
    public static String URL_IMAGE_OFFER = "http://mycitysshops.esy.es/my_citys_shops_adm/offer/image_offer/";
    public static String URL_IMAGE_DRAW = "http://mycitysshops.esy.es/my_citys_shops_adm/draw/image_draw/";

    //FECHAS
    //"dd/MM/yyyy" log file
    //"dd-MM-yyyy" draw
    public static String getFechaLogFile(String format) {
        Date dateOficial = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(dateOficial);
    }

    public static String getFechaOficial() {
        Date dateOficial = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(dateOficial);
    }

    public static String getFechaOficialSeparate() {
        Date dateOficial = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dateOficial);
    }

    public static boolean oldPhones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return false;
        else
            return true;
    }

    public static String getLastDayNotification() {
        Date dateOficial = new Date();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        cal.setTime(dateOficial);
        cal.add(Calendar.DATE, 7);

        return df.format(cal.getTime());
    }

    // notification "dd/MM/yyyy"
    //draw end yyyy-MM-dd HH:mm
    public static boolean validateExpirateCurrentTime(String dateExperate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date strDate = null;
        try {
            strDate = sdf.parse(dateExperate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (strDate != null)
            if (System.currentTimeMillis() >= strDate.getTime()) {//si la fecha de hoy es mayor a la fecha dada, es true
                return true;
            }
        return false;
    }
   //"yyyy-MM-dd" validate create draw
   //"dd-MM-yyyy" validate list
    public static boolean validateExpirateVsLimit(String dateExperate, String dateLimit, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date strDate = null;
        Date limitDate = null;
        try {
            strDate = sdf.parse(dateExperate);
            limitDate = sdf.parse(dateLimit);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (strDate != null && limitDate != null)
            if (limitDate.getTime() <= strDate.getTime()) {
                return true;
            }
        return false;
    }

//    public static boolean validateCurrentDate(String dateExperate) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        Date strDate = null;
//        try {
//            strDate = sdf.parse(dateExperate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return false;
//        }
//        if (strDate != null)
//            if (System.currentTimeMillis() <= strDate.getTime()) {//la fecha es menor a la actual
//                return false;
//            }
//        return true;
//    }

    // "yyyy-MM-dd HH:mm:ss"
    //"dd-MM-yyyy HH:mm:ss"
    //yyyy-MM-dd HH:mm
    //dd-MM-yyyy HH:mm
    public static String formatViewDate(String dateString, String formatEnter, String formatExit) {
        SimpleDateFormat sdfActual = new SimpleDateFormat(formatEnter);
        SimpleDateFormat sdfClose = new SimpleDateFormat(formatExit);
        Date strDate = null;
        try {
            strDate = sdfActual.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        if (strDate != null)
            return sdfClose.format(strDate.getTime());
        else
            return "";
    }

    public static Long dateEndTimeFormat(String dateEnd) {
        SimpleDateFormat sdfActual = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date strDate = null;
        try {
            if (!dateEnd.isEmpty())
                strDate = sdfActual.parse(dateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        if (strDate != null)
            return strDate.getTime();
        else
            return null;
    }

    public static int randomNumber() {
        int min = 0;
        int max = 10;
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    public static void showSnackBar(View conteiner, String msg) {
        Snackbar.make(conteiner, msg, Snackbar.LENGTH_LONG).show();
    }

    public static byte[] readBytes(Uri uri, Context context) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static void setPicasso(Context context, String url, final int resource, final ImageView imageView) {
        Picasso.with(context)
                .load(url).fit()
                .placeholder(resource)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        imageView.setImageResource(resource);
                    }
                });
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
                for (int i = 0; i <= 3; i++) {
                    if (internetConnectionAvailable(5000)) {
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean internetConnectionAvailable(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
            return false;
        } catch (ExecutionException e) {
            return false;
        } catch (TimeoutException e) {
            return false;
        }
        return inetAddress != null && !inetAddress.equals("");
    }

    //TITLE GRUOP
    public static Typeface setFontPacificoTextView(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Pacifico.ttf");
    }

    //TITLE ITEM
    public static Typeface setFontGoodDogTextView(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/GoodDog.otf");
    }

    //TITLE DIALOG
    public static Typeface setFontExoTextView(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Exo.otf");
    }

    //TITLE TEXT DIALOG
    public static Typeface setFontRalewatTextView(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Raleway.ttf");
    }


    private static Map<Character, Character> MAP_NORM;

    public static String removeAccents(String value) {
        if (MAP_NORM == null || MAP_NORM.size() == 0) {
            MAP_NORM = new HashMap<Character, Character>();
            MAP_NORM.put('À', 'A');
            MAP_NORM.put('Á', 'A');
            MAP_NORM.put('Â', 'A');
            MAP_NORM.put('Ã', 'A');
            MAP_NORM.put('Ä', 'A');
            MAP_NORM.put('È', 'E');
            MAP_NORM.put('É', 'E');
            MAP_NORM.put('Ê', 'E');
            MAP_NORM.put('Ë', 'E');
            MAP_NORM.put('Í', 'I');
            MAP_NORM.put('Ì', 'I');
            MAP_NORM.put('Î', 'I');
            MAP_NORM.put('Ï', 'I');
            MAP_NORM.put('Ù', 'U');
            MAP_NORM.put('Ú', 'U');
            MAP_NORM.put('Û', 'U');
            MAP_NORM.put('Ü', 'U');
            MAP_NORM.put('Ò', 'O');
            MAP_NORM.put('Ó', 'O');
            MAP_NORM.put('Ô', 'O');
            MAP_NORM.put('Õ', 'O');
            MAP_NORM.put('Ö', 'O');
            MAP_NORM.put('Ñ', 'N');
            MAP_NORM.put('Ç', 'C');
            MAP_NORM.put('ª', 'A');
            MAP_NORM.put('º', 'O');
            MAP_NORM.put('§', 'S');
            MAP_NORM.put('³', '3');
            MAP_NORM.put('²', '2');
            MAP_NORM.put('¹', '1');
            MAP_NORM.put('à', 'a');
            MAP_NORM.put('á', 'a');
            MAP_NORM.put('â', 'a');
            MAP_NORM.put('ã', 'a');
            MAP_NORM.put('ä', 'a');
            MAP_NORM.put('è', 'e');
            MAP_NORM.put('é', 'e');
            MAP_NORM.put('ê', 'e');
            MAP_NORM.put('ë', 'e');
            MAP_NORM.put('í', 'i');
            MAP_NORM.put('ì', 'i');
            MAP_NORM.put('î', 'i');
            MAP_NORM.put('ï', 'i');
            MAP_NORM.put('ù', 'u');
            MAP_NORM.put('ú', 'u');
            MAP_NORM.put('û', 'u');
            MAP_NORM.put('ü', 'u');
            MAP_NORM.put('ò', 'o');
            MAP_NORM.put('ó', 'o');
            MAP_NORM.put('ô', 'o');
            MAP_NORM.put('õ', 'o');
            MAP_NORM.put('ö', 'o');
            MAP_NORM.put('ñ', 'n');
            MAP_NORM.put('ç', 'c');
        }
        if (value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(value);
        for (int i = 0; i < value.length(); i++) {
            Character c = MAP_NORM.get(sb.charAt(i));
            if (c != null) {
                sb.setCharAt(i, c.charValue());
            }
        }
        return sb.toString();
    }

    public static boolean validateLogFile(Context context) {
        try {
            if (getDateLogFile(context).equals("") || !getFechaLogFile("dd/MM/yyyy").equals(getDateLogFile(context)))
                return createLogFile(context);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean createLogFile(Context context) {
        try {
            File logFile = new File(context.getFilesDir() + "/" + context.getString(R.string.log_file_name));
            if (logFile.createNewFile()) {
                writelogFile(context, "Se crea archivo log correctamente");
                setDateLogFile(context, getFechaLogFile("dd/MM/yyyy"));
                return true;
            } else {
                logFile.delete();
                logFile.createNewFile();
                setDateLogFile(context, getFechaLogFile("dd/MM/yyyy"));
                return true;
            }
        } catch (Exception e) {
            writelogFile(context, e.getMessage());
            return false;
        }
    }

    public static void writelogFile(Context context, String msg) {
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter write = null;
        try {
            fileOutputStream = new FileOutputStream(new File(context.getFilesDir() + "/" +
                    context.getResources().getString(R.string.log_file_name)), true);
            write = new OutputStreamWriter(fileOutputStream);
            write.append(msg + " " + Utils.getFechaOficialSeparate() + "\n");

            write.close();
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (Exception e) {

        } finally {
            if (fileOutputStream != null)
                fileOutputStream = null;
            if (write != null)
                write = null;
        }
    }

    public static void setIdDrawEnd(Context context, int id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.draw_id_shared), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.id_draw), id);
        editor.commit();
    }

    public static int getIdDrawEnd(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.draw_id_shared), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(context.getString(R.string.id_draw), 0);
    }

    public static void setIdCity(Context context, int id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_city_id_shared), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.id_city), id);
        editor.commit();
    }

    public static int getIdCity(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_city_id_shared), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(context.getString(R.string.id_city), 0);
    }

    public static void resetIdCity(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_city_id_shared), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.id_city), 0);
        editor.commit();
    }

    public static void setNameShop(Context context, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shop_name_shared), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.name_shop), name);
        editor.commit();
    }

    public static String getNameShop(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shop_name_shared), Context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.name_shop), "");
    }

    public static void setIdShop(Context context, int id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shop_id_shared), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.id_shop), id);
        editor.commit();
    }

    public static int getIdShop(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shop_id_shared), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(context.getString(R.string.id_shop), 0);
    }

    public static void setIdFollow(Context context, int id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_follow_id_shared), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.id_follow), id);
        editor.commit();
    }

    public static int getIdFollow(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_follow_id_shared), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(context.getString(R.string.id_follow), 0);
    }

    public static void setDateLogFile(Context context, String date) {
        writelogFile(context, "Se actualiza date Log SharePreferences");
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.log_date_shared), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.log_date), date);
        editor.commit();
        writelogFile(context, "Se actualiza date Log SharePreferences Correctamente");
    }

    public static String getDateLogFile(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.log_date_shared), Context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.log_date), "");
    }

    ////EMAIL SENDER
    public static CommandMap createMailcapCommandMap() {
        try {
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);
            return mc;
        } catch (Exception e) {
            return null;
        }
    }

    public static Session createPropertiesAndSession(final String from, final String password) {
        try {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.socketFactory.port", "465");
            properties.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "465");
            return createSession(properties, from, password);
        } catch (Exception e) {
            return null;
        }
    }

    public static Session createSession(Properties properties, final String from, final String password) {
        try {
            return Session.getDefaultInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(from, password);
                        }
                    });
        } catch (Exception e) {
            return null;
        }
    }

    public static MimeMessage createMimeMessage(Session session, final String from, final String to, final String Shop_name, Context context) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Soporte " + Shop_name + " " + "Ciudad: " + Utils.getIdCity(context));
            return message;
        } catch (Exception e) {
            return null;
        }
    }

    public static BodyPart createMimeBodyPart(String comment) {
        try {
            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText(comment);
            return messageBodyPart1;
        } catch (Exception e) {
            return null;
        }
    }

    public static MimeBodyPart createMimeBodyPart2(String filename) {
        try {
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName("Error_Log.txt");
            return messageBodyPart2;
        } catch (Exception e) {
            return null;
        }
    }

    public static Multipart createMultipart(BodyPart messageBodyPart1, MimeBodyPart messageBodyPart2) {
        try {
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);
            return multipart;
        } catch (Exception e) {
            return null;
        }
    }

    public static void ImageDialogLogo(final Activity activity, final Fragment fragment, final int GALLERY) {
        AlertDialog.Builder myAlertDialog;
        if (activity != null)
            myAlertDialog = new AlertDialog.Builder(
                    activity);
        else
            myAlertDialog = new AlertDialog.Builder(
                    fragment.getActivity());
        myAlertDialog.setTitle("Galeria");
        myAlertDialog.setMessage("Seleccione una foto.");

        myAlertDialog.setPositiveButton("Galeria",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pickIntent = new Intent(
                                Intent.ACTION_GET_CONTENT, null);
                        pickIntent.setType("image/*");
                        pickIntent.putExtra(
                                "return-data", true);
                        if (activity != null)
                            activity.startActivityForResult(
                                    pickIntent,
                                    GALLERY);
                        else
                            fragment.startActivityForResult(
                                    pickIntent,
                                    GALLERY);
                    }
                });
        myAlertDialog.show();
    }

    public static void startCropImageActivity(Activity activity, Fragment fragment, Uri imageUri) {
        if (activity != null) {
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(true)
                    .start(activity);
        } else {
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(true)
                    .start(fragment.getContext(), fragment);
        }
    }

    public static boolean hasPermission(Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    public static void checkForPermission(Context context, final Activity activity, final Fragment fragment, int PERMISSION_GALERY) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (activity != null) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_GALERY);
            } else {
                fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_GALERY);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }
}
