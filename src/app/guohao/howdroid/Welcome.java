package app.guohao.howdroid;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;

public class Welcome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		String appHome = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/bpok_Soft";
		final String subPath = appHome + "/rebooter";
		final File file = new File(subPath);
		if (file.exists()) {
			final Intent intent = new Intent();

			intent.setClass(Welcome.this, Main.class);
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					startActivity(intent);
					Welcome.this.finish();

				}
			};
			timer.schedule(task, 1000 * 0);

		} else {

			final Intent intent = new Intent();

			intent.setClass(Welcome.this, Warning.class);

			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					startActivity(intent);
					Welcome.this.finish();

				}
			};
			timer.schedule(task, 1000 * 2);

		}
	}

}
