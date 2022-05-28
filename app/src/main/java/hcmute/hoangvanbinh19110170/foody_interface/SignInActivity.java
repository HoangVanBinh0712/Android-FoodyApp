package hcmute.hoangvanbinh19110170.foody_interface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText loginEmail,loginpassword;
    Button btnLogin;
    Database database;
    TextView txtCreateAcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        AnhXa();
        if(!sharedPreferences.getString("userId","").equals(""))
        {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!sharedPreferences.getString("userId","").equals(""))
        {
            finish();
        }
    }

    public void AnhXa(){
        sharedPreferences = getSharedPreferences("dataCookie", MODE_MULTI_PROCESS);
        database = new Database(this,"FoodyDB.sqlite",null,1);

        loginEmail = findViewById(R.id.loginEmail);
        loginpassword = findViewById(R.id.loginpassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtCreateAcc = findViewById(R.id.txtCreateAcc);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString().trim();
                String password = loginpassword.getText().toString().trim();
                if(email == "" || password == "")
                    Toast.makeText(SignInActivity.this, "Empty Email or Password", Toast.LENGTH_SHORT).show();
                else{
                    Cursor cus  = database.GetData("SELECT * from User where Email = '"+ email +"' and Password = '"+password+ "'");
                    while(cus.moveToNext())
                    {
                        int userId = cus.getInt(0);
                        String name = cus.getString(1);
                        String role = cus.getString(7);
                        Toast.makeText(SignInActivity.this,"Wellcom " + name, Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId",Integer.toString(userId));
                        editor.putString("user_Role",role);
                        editor.commit();
                        finish();
                        return;
                    }
                    Toast.makeText(SignInActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}