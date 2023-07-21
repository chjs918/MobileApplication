package mobile.example.movingballtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	BallView ballView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		ballView = new BallView(this);
		setContentView(ballView);

	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		
	}


	@Override
	protected void onResume() {
		super.onResume();
		
	}

	
	class BallView extends View{

		Paint paint;

		int width;
		int height;
		
		int x;
		int y;
		int r;
		
		boolean isStart;
		
		public BallView(Context context) {
			super(context);
			paint = new Paint();
			paint.setColor(Color.RED);
			paint.setAntiAlias(true);
			isStart = true;
			r = 100;
		}
		
		public void onDraw(Canvas canvas) {
			if(isStart) {
				width = canvas.getWidth();
				height = canvas.getHeight(); 
				x =  width / 2;
				y =  height / 2;
				isStart = false;
			} 
			
			canvas.drawCircle(x, y, r, paint);
		}
		
	}	
}
