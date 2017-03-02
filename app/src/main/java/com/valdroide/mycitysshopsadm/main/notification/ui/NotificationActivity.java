package com.valdroide.mycitysshopsadm.main.notification.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.valdroide.mycitysshopsadm.MyCitysShopsAdmApp;
import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.main.notification.NotificationActivityPresenter;
import com.valdroide.mycitysshopsadm.utils.Utils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.onClick;

public class NotificationActivity extends AppCompatActivity implements NotificationActivityView {

    //    @Bind(R.id.editTextTitle)
//    EditText editTextTitle;
    @Bind(R.id.editTextNotification)
    EditText editTextNotification;
    @Bind(R.id.buttonSend)
    Button buttonSend;
    @Bind(R.id.activity_notification)
    RelativeLayout conteiner;

    @Inject
    NotificationActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.notification_title);
        setupInjection();
        presenter.onCreate();
    }

    private void setupInjection() {
        MyCitysShopsAdmApp app = (MyCitysShopsAdmApp) getApplication();
        app.getNotificationActivityComponent(this, this).inject(this);
    }

    @OnClick(R.id.buttonSend)
    public void sendNotification() {
        if (editTextNotification.getText().toString().equals(""))
            Utils.showSnackBar(conteiner, getString(R.string.notification_empty));
        else
            presenter.sendNotification(this, editTextNotification.getText().toString());
    }

    @Override
    public void sentSuccess() {
        Utils.showSnackBar(conteiner, getString(R.string.send_notification_success));
    }

    @Override
    public void sentError(String error) {
        Utils.showSnackBar(conteiner, error);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
