package mobile.example.sensorbasictest;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	private TextView tvText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvText = findViewById(R.id.tvText);
		
	}
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnSensor:
			
			String result = "";
			

			
			tvText.setText(result);
			
			break;
		}
	}
	
	
}
