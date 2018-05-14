package shopping.com.androidioc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

@SetContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewBind(R.id.tv)
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectUtils.inject(this);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"我被点击了",Toast.LENGTH_LONG).show();
            }
        });

    }
}
