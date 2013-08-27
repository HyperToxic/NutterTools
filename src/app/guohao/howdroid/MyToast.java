package app.guohao.howdroid;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast extends Toast {

	public MyToast(Context context) {
		super(context);
	}

	public static Toast makeText(Context context, CharSequence text,
			int duration) {
		Toast result = new Toast(context);

		// ��ȡLayoutInflater����
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// ��layout�ļ�����һ��View����
		View layout = inflater.inflate(R.layout.toast, null);

		// ʵ����ImageView��TextView����

		TextView textView = (TextView) layout.findViewById(R.id.toast);

		textView.setText(text);

		result.setView(layout);
		result.setGravity(Gravity.BOTTOM, 0, 0);
		// result.setMargin(null, 20f);
		result.setDuration(duration);

		return result;
	}

}
