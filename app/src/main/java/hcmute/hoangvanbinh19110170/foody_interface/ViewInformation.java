package hcmute.hoangvanbinh19110170.foody_interface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import hcmute.hoangvanbinh19110170.foody_interface.Models.Purchase;

public class ViewInformation extends AppCompatActivity {
    EditText email2,edtName,edtAddress,edtPhone,edtBirth;
    Button btninfor, btnpassword;
    TextView txtRole;

    SharedPreferences sharedPreferences;
    Database database;

    //----
    private EditText oldpass, newpass, cfnewpass;
    private Button submit,cancel;

    String userId,user_Role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountsetting);
        init();
    }

    public void init()
    {
        sharedPreferences = getSharedPreferences("dataCookie", MODE_MULTI_PROCESS);
        database = new Database(this,"FoodyDB.sqlite",null,1);
        user_Role = sharedPreferences.getString("user_Role","");

        email2 = findViewById(R.id.email2);
        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
        edtBirth = findViewById(R.id.edtBirth);

        btninfor = findViewById(R.id.btnChangeInfor);
        btnpassword = findViewById(R.id.btnChangePassword);
        txtRole = findViewById(R.id.txtRole);
        userId = sharedPreferences.getString("userId","");
        if(userId.equals(""))
        {
            Intent intent = new Intent(ViewInformation.this,SignInActivity.class);
            startActivity(intent);
        }else
        {
            try {
                Cursor cursor =  database.GetData("SELECT * from User where Id = " + sharedPreferences.getString("userId",""));
                while (cursor.moveToNext()){
                    email2.setText(cursor.getString(5));
                    edtName.setText(cursor.getString(1));
                    edtBirth.setText(cursor.getString(3));
                    edtAddress.setText(cursor.getString(4));
                    edtPhone.setText(cursor.getString(6));
                    if(cursor.getString(7).equals("1"))
                        txtRole.setText("Seller");
                    else if(cursor.getString(7).equals("0"))
                        txtRole.setText("Buyer");
                    else txtRole.setText("Admin");
                }
            }catch (Exception ex)
            {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        btninfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userId.equals("")) {
                    return;
                }
                String email = email2.getText().toString();
                String name = edtName.getText().toString();
                String Birth = edtBirth.getText().toString();
                String address = edtAddress.getText().toString();
                String phone = edtPhone.getText().toString();
                try{
                    database.QueryData("UPDATE User set Email = '"+email+"', Name = '"+name+"', Address = '"+address+"',Birth = '"+Birth+"', Phone = '"+phone+"' WHERE Id = " + userId);
                    Toast.makeText(ViewInformation.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                }catch (Exception e)
                {
                    Toast.makeText(ViewInformation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogChangPass();
            }
        });
    }

    public void DialogChangPass() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_change_password);
        oldpass = (EditText) dialog.findViewById(R.id.edtOldPass);
        newpass = (EditText) dialog.findViewById(R.id.edtNewPass);
        cfnewpass = (EditText) dialog.findViewById(R.id.edtConfirmNewPass);

        submit = (Button) dialog.findViewById(R.id.btnChangePassSubmit);
        cancel = (Button) dialog.findViewById(R.id.btnChangePassCancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String old = oldpass.getText().toString();
                String newp = newpass.getText().toString();
                String cnewp = cfnewpass.getText().toString();
                if (old.equals(newp)) {
                    Toast.makeText(ViewInformation.this, "New Pass Is Not Different OldPass !", Toast.LENGTH_SHORT).show();
                    return;
                } else if (newp.equals(cnewp)) {
                    Cursor cursor = database.GetData("Select Password from User where Id =" + userId);
                    while (cursor.moveToNext()) {
                        if (old.equals(cursor.getString(0))) {
                            database.QueryData("Update User set Password ='" + newp + "' where Id=" + userId);
                            Toast.makeText(ViewInformation.this, "Update Password SuccessFully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ViewInformation.this, "Wrong Old PassWord", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }

                } else {
                    Toast.makeText(ViewInformation.this, "New Password not Match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

}