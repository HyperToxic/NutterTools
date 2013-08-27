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

	// 菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);

		MenuItem about = menu.add("提示");
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
			// 返回页面数4.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {

			switch (position) {
			case 0:
				return "重启";
			case 1:
				return "高级";
			case 2:
				return "提高";
			case 3:
				return "关于";
			}
			return null;
		}

	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {

		// 初始化btns
		Button reboot, recovery, shutdown, bootloader;
		TextView text;
		String tip = "别戳了：mt65xx机型bootloader模式已禁用";
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

		// reboot界面监听各个按钮
		public void buttonAdp() {

			boolean hasBtl = true;

			recovery = (Button) rootView.findViewById(R.id.recovery);
			bootloader = (Button) rootView.findViewById(R.id.bootloader);
			shutdown = (Button) rootView.findViewById(R.id.powerOff);
			reboot = (Button) rootView.findViewById(R.id.reboot);
			text = (TextView) rootView.findViewById(R.id.control);

			// 获取处理器型号
			if (android.os.Build.HARDWARE.startsWith("mt")) {

				hasBtl = false;

			}

			// 监听各个btn
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

		// spare页面的监听、管理
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
								// 如果成功删除
								tip2.setText("已经成功永久静音");
							} else {
								tip2.setText("静音失败，您的设备可能没有root");
								cameraMute.setEnabled(false);

							}
						}

					});

			// 是否ROOT
			if (isRooted) {
				sRoot = "已root";
			} else {
				sRoot = "未root";
			}

			// 监听textView
			deviceInfoMa.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// 字体大小textview的点击监听

					tip3.setText("厂商：" + deviceInfoManufacter + "\n" + "型号："
							+ deviceInfoBoard + "\n" + "处理器：" + device + "\n"
							+ "最低频率：" + cpuMin + "\n" + "最高频率：" + cpuMax + "\n"
							+ "当前频率：" + cpuCurr + "\n" + "软件版本：" + scr + "\n"
							+ "超级权限：" + sRoot);

				}
			});

		}

		// webView的响应
		@SuppressLint("SetJavaScriptEnabled")
		public void getWebContent() {

			String url;
			WebView show;
			// 获取组件
			show = (WebView) rootView.findViewById(R.id.webView1);
			url = "http://adroidhow.blog.163.com/";
			// url="file:///android_asset/blog.html";
			// 设置WebView属性，能够执行Javascript脚本
			show.getSettings().setJavaScriptEnabled(true);
			WebSettings ws = show.getSettings();

			ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

			// 让缩放显示的最小值为肇端
			// show.setInitialScale(99);
			// 设置支撑缩放
			ws.setSupportZoom(true);

			// 设置缩放工具的显示
			ws.setBuiltInZoomControls(true);
			// 加载页面
			show.loadUrl(url);

		}
	}

}
