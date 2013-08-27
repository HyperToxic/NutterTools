package app.guohao.howdroid;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import app.guohao.util.*;

public class Warning extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.warning);

		final Button bn = (Button) findViewById(R.id.login);

		bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View source) {

				String appHome = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/bpok_Soft";
				String subPath = appHome + "/rebooter";

				AppUtil.createPath(appHome);
				AppUtil.createPath(subPath);

				File saveFile = new File(subPath, "rebooter_Log.md");
				try {
					FileWriter fw = new FileWriter(saveFile);
					BufferedWriter bw = new BufferedWriter(fw);

					bw.write("--软件记录由rebooter自动生成--" + "\n机器型号："
							+ android.os.Build.MODEL + "\n生产者："
							+ android.os.Build.MANUFACTURER + "\n"
							+ "\n该记录存在表明您已经同意本软件的声明警告"
							+ "\n--copyright@bpok.app.android--");
					bw.close();
				} catch (Exception e) {
				}

				Intent intent = new Intent();

				intent.setClass(Warning.this, Main.class);
				startActivity(intent);
				Warning.this.finish();

			}
		});
	}

}