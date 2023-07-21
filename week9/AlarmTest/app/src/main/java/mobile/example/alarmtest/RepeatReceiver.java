package mobile.example.alarmtest;

import android.app.PendingIntent;
import android.content.*;
import android.widget.*;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RepeatReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent i) {
		Toast.makeText(context, "Hi all!", Toast.LENGTH_SHORT).show();

		//원래 context부분이 this였는데 this를 쓸 수 없다. (Activity가 아니니까~~~)
		// notification 생성
		//1. 인텐트를 만들고, 2.인텐트에 Flag정보를 별도로 설정하고, 3. PendingIntent로 인텐트 포장해준다(액티비티기 때문에 getActivity사용)
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

		//빌더를 만들고, 아이콘, 제목내용 등 설정해준다. 빌더는 NotificationCompat.Builder!
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.CHANNEL_ID))
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("기상 시간")
				.setContentText("일어나! 공부할 시간이야!")
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setContentIntent(pendingIntent)
				.addAction(R.drawable.ic_launcher, "Noti", pendingIntent)
				.setAutoCancel(true);

		//notificationManager를 만들고, 생성한 notification의 id를 전달 받아서 notify해서 알림을 실행해준다.
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

		//알림을 구분하기 위한 정수형 식별자 지정
		int notificationId =100;
		notificationManager.notify(notificationId, builder.build());

	}
}
