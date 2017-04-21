package pooop.android.sidedish;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private String mLoginStr = "";
    private String mLoginPwd = "";
    private Employee currEmployee;

    private UserController mUserController;

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserController = UserController.getInstance(this);

        // create admin if not already created
        if(mUserController.getUsers().size() == 0){
            mUserController.addUser("admin", 1, "admin");
        }

        setContentView(R.layout.activity_login);

        mLoginEditText = (EditText) findViewById(R.id.login_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mLoginButton = (Button) findViewById(R.id.login_button);

        mLoginEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Unused
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginStr = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Unused
            }
        });

        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Unused
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginPwd = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Unused
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mUserController.isValidUser(mLoginStr, mLoginPwd)){
                        Employee currEmployee = mUserController.getEmployeeInfo(mLoginStr, mLoginPwd);
                        Intent intent = WaitStaffActivity.newIntent(getApplicationContext());
                        intent.putExtra("type", currEmployee.getTypeInt());
                        startActivity(intent);

                        // Back button to exit app rather than logout
                        finish();


                }
                else{
                    Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }
}
