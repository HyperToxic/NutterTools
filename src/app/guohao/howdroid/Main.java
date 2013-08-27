package app.guohao.howdroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import app.guohao.api.AndroidCommand;
import app.guohao.util.AppUtil;

public class Main extends FragmentActivity {

	Button reboot;

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	// �˵�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);

		MenuItem about = menu.add("��ʾ");
		about.setIntent(new Intent(this, Help.class));

		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);

		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// ����ҳ����4.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {

			switch (position) {
			case 0:
				return "����";
			case 1:
				return "�߼�";
			case 2:
				return "���";
			case 3:
				return "����";
			}
			return null;
		}

	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {

		// ��ʼ��btns
		Button reboot, recovery, shutdown, bootloader;
		TextView text;
		String tip = "����ˣ�mt65xx����bootloaderģʽ�ѽ���";
		View rootView = null;

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			int tab_num = (getArguments().getInt(ARG_SECTION_NUMBER));
			switch (tab_num) {
			case 1:
				rootView = inflater.inflate(R.layout.reboot, container, false);
				buttonAdp();
				break;
			case 2:
				rootView = inflater.inflate(R.layout.spare, container, false);
				spareAdp();
				break;
			case 3:
				rootView = inflater.inflate(R.layout.lesson, container, false);
				getWebContent();
				break;
			case 4:
				rootView = inflater.inflate(R.layout.about, container, false);
				break;
			}

			return rootView;

		}

		// reboot�������������ť
		public void buttonAdp() {

			boolean hasBtl = true;

			recovery = (Button) rootView.findViewById(R.id.recovery);
			bootloader = (Button) rootView.findViewById(R.id.bootloader);
			shutdown = (Button) rootView.findViewById(R.id.powerOff);
			reboot = (Button) rootView.findViewById(R.id.reboot);
			text = (TextView) rootView.findViewById(R.id.control);

			// ��ȡ�������ͺ�
			if (android.os.Build.HARDWARE.startsWith("mt")) {

				hasBtl = false;

			}

			// ��������btn
			if (shutdown != null) {
				shutdown.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AndroidCommand.execRooted("reboot -p");
					}
				});
			}
			if (bootloader != null && hasBtl) {
				bootloader.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AndroidCommand.execRooted("reboot bootloader");
					}
				});
			} else {
				bootloader.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						text.setText(tip);

					}
				});

			}

			if (recovery != null) {
				recovery.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AndroidCommand.execRooted("reboot recovery");
					}
				});
			}
			if (reboot != null) {
				reboot.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AndroidCommand.execRooted("reboot");
					}
				});
			}

		}

		// spareҳ��ļ���������
		public void spareAdp() {

			final Switch cameraMute = (Switch) rootView
					.findViewById(R.id.cameraMute_switch);
			final TextView tip2 = (TextView) rootView.findViewById(R.id.tip2);
			TextView deviceInfoMa = (TextView) rootView.findViewById(R.id.dev);
			final TextView tip3 = (TextView) rootView.findViewById(R.id.tip3);

			final String deviceInfoBoard;
			final String deviceInfoManufacter;
			final String device;
			final String cpuMin;
			final String cpuMax;
			final String cpuCurr;
			final String scr;
			final String sRoot;
			final boolean isRooted;
			deviceInfoBoard = android.os.Build.BOARD;
			deviceInfoManufacter = android.os.Build.MANUFACTURER;
			device = android.os.Build.HARDWARE;
			cpuMin = AppUtil.getMinCpuFreq();
			cpuMax = AppUtil.getMaxCpuFreq();
			cpuCurr = AppUtil.getCurCpuFreq();
			scr = android.os.Build.DISPLAY;
			isRooted = AndroidCommand.isRooted();

			cameraMute
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {

							String sPath = "/system/media/audio/ui/camera_click.ogg";
							AppUtil.deleteFile(sPath);
							if (AppUtil.flag) {
								// ����ɹ�ɾ��
								tip2.setText("�Ѿ��ɹ����þ���");
							} else {
								tip2.setText("����ʧ�ܣ������豸����û��root");
								cameraMute.setEnabled(false);

							}
						}

					});

			// �Ƿ�ROOT
			if (isRooted) {
				sRoot = "��root";
			} else {
				sRoot = "δroot";
			}

			// ����textView
			deviceInfoMa.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// �����Сtextview�ĵ������

					tip3.setText("���̣�" + deviceInfoManufacter + "\n" + "�ͺţ�"
							+ deviceInfoBoard + "\n" + "��������" + device + "\n"
							+ "���Ƶ�ʣ�" + cpuMin + "\n" + "���Ƶ�ʣ�" + cpuMax + "\n"
							+ "��ǰƵ�ʣ�" + cpuCurr + "\n" + "����汾��" + scr + "\n"
							+ "����Ȩ�ޣ�" + sRoot);

				}
			});

		}

		// webView����Ӧ
		@SuppressLint("SetJavaScriptEnabled")
		public void getWebContent() {

			String url;
			WebView show;
			// ��ȡ���
			show = (WebView) rootView.findViewById(R.id.webView1);
			url = "http://adroidhow.blog.163.com/";
			// url="file:///android_asset/blog.html";
			// ����WebView���ԣ��ܹ�ִ��Javascript�ű�
			show.getSettings().setJavaScriptEnabled(true);
			WebSettings ws = show.getSettings();

			ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

			// ��������ʾ����СֵΪ�ض�
			// show.setInitialScale(99);
			// ����֧������
			ws.setSupportZoom(true);

			// �������Ź��ߵ���ʾ
			ws.setBuiltInZoomControls(true);
			// ����ҳ��
			show.loadUrl(url);

		}
	}

}
