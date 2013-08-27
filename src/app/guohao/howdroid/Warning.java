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

					bw.write("--�����¼��rebooter�Զ�����--" + "\n�����ͺţ�"
							+ android.os.Build.MODEL + "\n�����ߣ�"
							+ android.os.Build.MANUFACTURER + "\n"
							+ "\n�ü�¼���ڱ������Ѿ�ͬ�Ȿ�������������"
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