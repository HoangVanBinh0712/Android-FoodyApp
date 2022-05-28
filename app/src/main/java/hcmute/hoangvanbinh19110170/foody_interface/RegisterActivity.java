package hcmute.hoangvanbinh19110170.foody_interface;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    int day, month, year;
    DatePickerDialog.OnDateSetListener setListenerOpen;

    EditText email2,password2,confirmpassword,edtName,edtAddress,edtPhone,edtBirth;
    CheckBox edtRole;
    Button conFirm , backToSign;
    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registeractivity);

        Anhxa();

    }
    public void Anhxa(){
        database = new Database(this,"FoodyDB.sqlite",null,1);

        email2 = findViewById(R.id.email2);
        password2 = findViewById(R.id.password2);
        confirmpassword = findViewById(R.id.confirmpassword);
        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
        edtBirth = findViewById(R.id.edtBirth);
        edtRole = findViewById(R.id.edtRole);

        conFirm = findViewById(R.id.btnConfirm);
        backToSign = findViewById(R.id.btnSignin);

        //On click
        edtBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, setListenerOpen,year, month,day);
                datePickerDialog.show();
            }
        });
        setListenerOpen = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                month = month+1;
                String date = year+"-"+month+"-"+dayofMonth;
                edtBirth.setText(date);
            }
        };

        conFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password,confirmPassword,name,address,phoneNumber,birthDay;
                Boolean isSeller = false;
                email = email2.getText().toString().trim();
                password = password2.getText().toString().trim();
                confirmPassword = confirmpassword.getText().toString().trim();
                name = edtName.getText().toString().trim();
                address = edtAddress.getText().toString().trim();
                phoneNumber = edtPhone.getText().toString();
                birthDay = edtBirth.getText().toString().trim();
                isSeller = edtRole.isChecked();
                String role = "0";
                if(isSeller) role = "1";
                if(password.equals(confirmPassword))
                {
                    try {
                        Cursor cursor = database.GetData("Select * from User where Email= '" + email +"' or Phone = '"+phoneNumber+"'");
                        if(cursor.moveToNext())
                        {
                            Toast.makeText(RegisterActivity.this, "Email or Phone is already Taken", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        database.INSERT_User(name,password,birthDay,address,email,phoneNumber,role);
                        Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }catch (Exception err){
                        Toast.makeText(RegisterActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(RegisterActivity.this, "PassWord Not Match", Toast.LENGTH_SHORT).show();
                }

            }
        });

        backToSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(RegisterActivity.this,SignInActivity.class);
//                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        email2.setText("");
        password2.setText("");
        confirmpassword.setText("");
        edtName.setText("");
        edtAddress.setText("");
        edtPhone.setText("");
        edtBirth.setText("");
        edtRole.setChecked(false);
    }
}

