package com.valdroide.mycitysshopsadm.main.notification.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.main.notification.NotificationActivityPresenter;
import com.valdroide.mycitysshopsadm.utils.Utils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends AppCompatActivity implements NotificationActivityView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.editTextEmail)
    EditText editTextEmail;
    @Bind(R.id.content_navigation)
    RelativeLayout conteiner;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Inject
    NotificationActivityPresenter presenter;
    @Bind(R.id.editTextEmailHint)
    TextInputLayout editTextEmailHint;
    @Bind(R.id.textViewUnavailable)
    TextView textViewUnavailable;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        Utils.writelogFile(this, "Se inicia ButterKnife(Notification)");
        ButterKnife.bind(this);
        Utils.writelogFile(this, "Se inicia Injection(Notification)");
        setupInjection();
        Utils.writelogFile(this, "Se inicia presenter Oncreate(Notification)");
        presenter.onCreate();
        Utils.writelogFile(this, "Se inicia toolbar Oncreate(Notification)");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.notification_title);
        initDialog();
        pDialog.show();
        editTextEmailHint.setHint(getString(R.string.hint_notification));
        presenter.validateNotificationExpire(this, Utils.getFechaLogFile());
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getNotificationActivityComponent(this, this).inject(this);
    }

    public void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.processing));
        pDialog.setCancelable(false);
    }

    @OnClick(R.id.fab)
    public void sendNotification() {
        Utils.writelogFile(this, "sendNotification click buttonSend(Notification)");
        try {
            if (editTextEmail.getText().toString().equals(""))
                Utils.showSnackBar(conteiner, getString(R.string.notification_empty));
            else {
                Utils.writelogFile(this, "sendNotification sendNotification(Notification)");
                pDialog.show();
                presenter.sendNotification(this, editTextEmail.getText().toString());
            }
        } catch (Exception e) {
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Notification)");
            setError(e.getMessage());
        }
    }

    @Override
    public void setSuccess(String date) {
        Utils.writelogFile(this, "setSuccess(Notification)");
        editTextEmail.setText("");
        setEnable(false, date);
        Utils.showSnackBar(conteiner, getString(R.string.send_notification_success));
    }

    @Override
    public void setError(String error) {
        Utils.writelogFile(this, "setError: " + error + "(Notification)");
        if (pDialog.isShowing())
            pDialog.dismiss();
        Utils.showSnackBar(conteiner, error);
    }

    @Override
    public void isAvailable(boolean is_available, String date) {
        Utils.writelogFile(this, "isAvailable(Notification)");
        if (is_available) {
            Utils.writelogFile(this, "is available y presenter sendNotification(Notification)");
            setEnable(is_available, date);
        } else {
            Utils.writelogFile(this, "is unavailable(Notification)");
            setEnable(is_available, date);
        }
    }

    public void setEnable(boolean isEnable, String date) {
        Utils.writelogFile(this, "setEnable editText (Notification)");
        try {
            setAttributeEdit(editTextEmail, isEnable, date);
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, "setEnable error " + e.getMessage() + " (Notification)");
        }
    }

    public void setAttributeEdit(EditText editText, boolean isEnable, String date) {
        Utils.writelogFile(this, "setAttributeEdit (Notification)");
        editText.setEnabled(isEnable);
        editText.setFocusable(isEnable);
        editText.setFocusableInTouchMode(isEnable);
        fab.setEnabled(isEnable);
        if (isEnable) {
            textViewUnavailable.setVisibility(View.GONE);
        } else {
            textViewUnavailable.setVisibility(View.VISIBLE);
            textViewUnavailable.setText(getString(R.string.notification_unavailable) + " " + date);
        }
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        Utils.writelogFile(this, "onDestroy(Notification)");
        presenter.onDestroy();
        super.onDestroy();
    }
}
