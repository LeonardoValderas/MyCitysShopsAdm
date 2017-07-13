package com.valdroide.mycitysshopsadm.main.draw.fragments.draw.ui;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.InputFilter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.theartofdev.edmodo.cropper.CropImage;
import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.entities.shop.Draw;
import com.valdroide.mycitysshopsadm.main.draw.Communicator;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.DrawFragmentPresenter;
import com.valdroide.mycitysshopsadm.utils.Utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class DrawFragment extends Fragment implements DrawFragmentView {


    @Bind(R.id.imageViewImage)
    ImageView imageViewImage;
    @Bind(R.id.editTextThing)
    EditText editTextThing;
    @Bind(R.id.radioFollowing)
    RadioButton radioFollowing;
    @Bind(R.id.radioAll)
    RadioButton radioAll;
    @Bind(R.id.editTextDescription)
    EditText editTextDescription;
    @Bind(R.id.buttonDateEnd)
    Button buttonDateEnd;
    @Bind(R.id.buttonTimeEnd)
    Button buttonTimeEnd;
    @Bind(R.id.buttonDateUse)
    Button buttonDateUse;
    @Bind(R.id.activity_draw)
    RelativeLayout conteiner;

    @Inject
    DrawFragmentPresenter presenter;

    public static final int PERMISSION_GALERY = 1;
    public static final int GALERY = 1;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    private Communicator communication;
    private byte[] imageByte;
    private ProgressDialog pDialog;
    private String encode = "", name_image = "", url_image = "", date = "", time = "", limitDate = "", city = "";
    private SimpleDateFormat formate = new SimpleDateFormat(
            "yyyy-MM-dd");
    private SimpleDateFormat formateView = new SimpleDateFormat(
            "dd-MM-yyyy");
    private DateFormat form = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.UK);
    private Calendar calendar = Calendar.getInstance();
    private Calendar calenda = Calendar.getInstance();
    private boolean isEnd = true;
    private MenuItem itemSave;

    public DrawFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draw, container, false);
        ButterKnife.bind(this, view);
        editTextThing.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(20)});
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        communication = (Communicator) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupInjection();
        presenter.onCreate();
        initDialog();
        presenter.getCity(getActivity());
        validateDateShop();
    }

    private void validateDateShop() {
        Utils.writelogFile(getActivity(), "validateDateShop(DrawFragment)");
        showProgressDialog();
        presenter.validateDateShop(getActivity());
    }

    private void setupInjection() {
        Utils.writelogFile(getActivity(), "setupInjection(DrawFragment)");
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getActivity().getApplication();
        app.getDrawFragmentComponent(this, this).inject(this);
    }

    private void initDialog() {
        Utils.writelogFile(getActivity(), "initDialog(DrawFragment)");
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.processing));
        pDialog.setCancelable(false);
    }

    private Draw fillDraw(String descrip, String end_date, String limit_date, int for_following, String condition) {
        Utils.writelogFile(getActivity(), "fillDraw(DrawFragment)");
        showProgressDialog();
        try {
            if (imageByte != null) {
                try {
                    encode = Base64.encodeToString(imageByte,
                            Base64.DEFAULT);
                } catch (Exception e) {
                    encode = "";
                }
                name_image = Utils.randomNumber() + Utils.getFechaOficial() + ".PNG";
                url_image = Utils.URL_IMAGE_DRAW + city + "/" + name_image;
            }
            String start_date = Utils.getFechaOficialSeparate();

            return new Draw(0, Utils.getIdShop(getActivity()), Utils.getIdCity(getActivity()), descrip, for_following,
                    condition, start_date, end_date+":00", limit_date, url_image, name_image,
                    start_date, encode);
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "fillDraw error: " + e.getMessage() + " (DrawFragment)");
            return null;
        }
    }


    private void cleanView() {
        Utils.writelogFile(getActivity(), "cleanView (DrawFragment)");
        try {
            editTextThing.setText("");
            editTextDescription.setText("");
            buttonDateEnd.setText(getString(R.string.date_end_draw));
            buttonTimeEnd.setText(getString(R.string.time_end_draw));
            buttonDateUse.setText(getString(R.string.date_limit));
            encode = "";
            name_image = "";
            url_image = "";
            date = "";
            time = "";
            imageByte = null;
            isEnd = true;
            imageViewImage.setImageResource(android.R.drawable.ic_menu_crop);
            itemSave.setEnabled(true);
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "cleanView error: " + e.getMessage() + " (DrawFragment)");
            setError(e.getMessage());
        }
    }

    @Override
    @OnClick(R.id.imageViewImage)
    public void getPhoto() {
        Utils.writelogFile(getActivity(), "getPhoto() click imageViewLogo y validate oldPhones(DrawFragment)");
        if (!Utils.oldPhones())
            Utils.checkForPermission(getActivity(), null, this, PERMISSION_GALERY);
        Utils.writelogFile(getActivity(), "hasPermission() y ImageDialogLogo(Account)");
        if (Utils.hasPermission(getActivity()))
            Utils.ImageDialogLogo(null, this, GALERY);
    }

    @Override
    public void setError(String msg) {
        Utils.writelogFile(getActivity(), "setError " + msg + " (DrawFragment)");
        itemSave.setEnabled(true);
        Utils.showSnackBar(conteiner, msg);
    }

    @Override
    @OnClick(R.id.buttonDateEnd)
    public void onClickDateButton() {
        Utils.writelogFile(getActivity(), "onClickDateButton(DrawFragment)");
        isEnd = true;
        pickerDate();
    }

    @Override
    @OnClick(R.id.buttonDateUse)
    public void onClickDateUseButton() {
        Utils.writelogFile(getActivity(), "onClickDateUseButton(DrawFragment)");
        isEnd = false;
        pickerDate();
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    @OnClick(R.id.buttonTimeEnd)
    public void onClickTimeButton() {
        Utils.writelogFile(getActivity(), "onClickTimeButton(DrawFragment)");
        pickerTime();
    }

    @Override
    public void setDate() {
        Utils.writelogFile(getActivity(), "setDate(DrawFragment)");
        if (isEnd) {
            Utils.writelogFile(getActivity(), "isEnd == true(DrawFragment)");
            buttonDateEnd.setText(formateView.format(calendar.getTime()));
            date = formate.format(calendar.getTime());
        } else {
            Utils.writelogFile(getActivity(), "isEnd == false(DrawFragment)");
            buttonDateUse.setText(formateView.format(calendar.getTime()));
            limitDate = formate.format(calendar.getTime());
        }
    }

    @Override
    public void setTime() {
        Utils.writelogFile(getActivity(), "setTime(DrawFragment)");
        buttonTimeEnd.setText(form.format(calenda.getTime()));
        time = form.format(calenda.getTime());
    }

    @Override
    public void createSuccess() {
        Utils.writelogFile(getActivity(), "createSuccess(DrawFragment)");
        cleanView();
        communication.refresh(false);
        presenter.validateBroadcast(getActivity());
        Utils.showSnackBar(conteiner, getString(R.string.draw_create_success));
    }

    @Override
    public void showProgressDialog() {
        pDialog.show();
    }

    @Override
    public void hidePorgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void refreshAdapter() {
        communication.refresh(false);
    }

    private void pickerDate() {
        Utils.writelogFile(getActivity(), "pickerDate(DrawFragment)");
        new DatePickerDialog(getActivity(), d, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void pickerTime() {
        Utils.writelogFile(getActivity(), "pickerTime(DrawFragment)");
        new TimePickerDialog(getActivity(), t,
                calenda.get(Calendar.HOUR_OF_DAY),
                calenda.get(Calendar.MINUTE), true).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setDate();
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calenda.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calenda.set(Calendar.MINUTE, minute);
            setTime();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_GALERY)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.ImageDialogLogo(null, this, GALERY);
            }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == GALERY) {
                Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);
                Utils.startCropImageActivity(null, this, imageUri);
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (result != null) {
                    if (resultCode == RESULT_OK) {
                        assignImage(result.getUri());
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        if (!result.getError().toString().contains("ENOENT"))
                            Utils.showSnackBar(conteiner, "Error al asignar imagen: " + result.getError());
                    }
                }
            }
        } catch (Exception e) {
            Utils.writelogFile(getActivity(), "onActivityResult image() error: " + e.getMessage() + " (Draw)");
            setError(e.getMessage());
        }
    }

    private void assignImage(Uri uri) {
        Utils.writelogFile(getActivity(), "setPicasso() (DrawFragment)");
        Utils.setPicasso(getActivity(), uri.toString(), android.R.drawable.ic_menu_crop, imageViewImage);
        try {
            imageByte = Utils.readBytes(uri, getActivity());
        } catch (IOException e) {
            Utils.writelogFile(getActivity(), "setPicasso() error : " + e.getMessage() + "(DrawFragment)");
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        Utils.writelogFile(getActivity(), "onDestroyView() (DrawFragment)");
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        Utils.writelogFile(getActivity(), "onDestroy() (DrawFragment)");
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.draw, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void validateDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_save) {
            item.setEnabled(false);
            itemSave = item;
            try {
                Utils.writelogFile(getActivity(), "action_save click y  presenter.createDraw(DrawFragment)");
                String dateTime = date + " " + time;
                pDialog.show();
                presenter.getCity(getActivity());

                if (imageByte == null) {
                    hidePorgressDialog();
                    setError(getString(R.string.error_image));
                } else if (editTextThing.getText().toString().isEmpty()) {
                    hidePorgressDialog();
                    setError(getString(R.string.object_empty));
                } else if (editTextDescription.getText().toString().isEmpty()) {
                    hidePorgressDialog();
                    setError(getString(R.string.conditions_empty));
                } else if (buttonDateEnd.getText().toString().equalsIgnoreCase(getString(R.string.date_end_draw))) {
                    hidePorgressDialog();
                    setError(getString(R.string.end_date_empty));
                } else if (buttonTimeEnd.getText().toString().equalsIgnoreCase(getString(R.string.time_end_draw))) {
                    hidePorgressDialog();
                    setError(getString(R.string.time_date_empty));
                } else if (Utils.validateExpirateCurrentTime(dateTime, getString(R.string.format_draw))) {
                    hidePorgressDialog();
                    setError(getString(R.string.data_end_before));
                } else if (buttonDateUse.getText().toString().equalsIgnoreCase(getString(R.string.date_limit))) {
                    hidePorgressDialog();
                    setError(getString(R.string.date_limit_empty));
                } else if (Utils.validateExpirateVsLimit(date, limitDate, "yyyy-MM-dd")) {
                    hidePorgressDialog();
                    setError(getString(R.string.error_dateLimit_vs_dateExpirate));
                } else if (this.city == null || this.city.isEmpty()) {
                    hidePorgressDialog();
                    setError(getString(R.string.city_name_error));
                } else {
                    validateDialog();
                    int radioSelected;
                    if (radioFollowing.isChecked()) {
                        Utils.writelogFile(getActivity(), "radioFollowing.isChecked()(DrawFragment)");
                        radioSelected = 0;
                    } else {
                        Utils.writelogFile(getActivity(), "!radioFollowing.isChecked()(DrawFragment)");
                        radioSelected = 1;
                    }

                    presenter.createDraw(getActivity(),
                            fillDraw(editTextThing.getText().toString(), dateTime, buttonDateUse.getText().toString(),
                                    radioSelected, editTextDescription.getText().toString()));
                }
            } catch (Exception e) {
                Utils.writelogFile(getActivity(), "action_save click error: " + e.getMessage() + " (DrawFragment)");
                setError(e.getMessage());
            }
        } else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(getActivity());
        }
        return true;
    }
}
