package pooop.android.sidedish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static final String MANAGER_LOGIN_TEST = "poop";

    private EditText mLoginEditText;
    private Button mLoginButton;
    private String mLoginStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginEditText = (EditText) findViewById(R.id.login_edit_text);
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

        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mLoginStr.equals(MANAGER_LOGIN_TEST)){
                    Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT)
                            .show();
                }
                else{
                    Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }
}
