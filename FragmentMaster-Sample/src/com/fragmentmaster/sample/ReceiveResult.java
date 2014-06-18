package com.fragmentmaster.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fragmentmaster.app.MasterFragment;
import com.fragmentmaster.app.Request;
import com.fragmentmaster.sample.R;

public class ReceiveResult extends MasterFragment {
	private static final int REQUEST_CODE = 0;
	private TextView mResultView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.receive_result_fragment,
				container, false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mResultView = (TextView) view.findViewById(R.id.resultView);
		view.findViewById(R.id.button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						startFragmentForResult(NumbersList.class, REQUEST_CODE);
					}
				});
		FragmentManager fragmentManager = getChildFragmentManager();
		if (fragmentManager.findFragmentByTag("TAG_CHILD") == null) {
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.add(R.id.childContainer, new Child(), "TAG_CHILD");
			ft.commitAllowingStateLoss();
			fragmentManager.executePendingTransactions();
		}
	}

	@Override
	protected void onFragmentResult(int requestCode, int resultCode,
			Request data) {
		super.onFragmentResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			mResultView.setText(data
					.getStringExtra(NumbersList.EXTRA_KEY_RESULT));
		} else {
			mResultView.setText("[Canceled]");
		}
	}

	/**
	 * Child fragment
	 */
	public static class Child extends MasterFragment {
		private static final int REQUEST_CODE = 0;

		private TextView mResultView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.receive_result_child_fragment, container, false);
			return rootView;
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			mResultView = (TextView) view.findViewById(R.id.resultView);
			view.findViewById(R.id.button).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							startFragmentForResult(NumbersList.class,
									REQUEST_CODE);
						}
					});
		}

		@Override
		protected void onFragmentResult(int requestCode, int resultCode,
				Request data) {
			super.onFragmentResult(requestCode, resultCode, data);
			if (resultCode == RESULT_OK) {
				mResultView.setText(data
						.getStringExtra(NumbersList.EXTRA_KEY_RESULT));
			} else {
				mResultView.setText("[Canceled]");
			}
		}
	}

	/**
	 * Numbers list
	 * 
	 * TODO Add MasterListFragment and User it here as super class.
	 */
	public static class NumbersList extends MasterFragment {

		public static final String EXTRA_KEY_RESULT = "result";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.list_fragment, container, false);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			final String[] numbers = getResources().getStringArray(
					R.array.numbers);
			ListView listView = (ListView) view.findViewById(R.id.listView);
			listView.setAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, numbers));
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// deliver result
					Request result = new Request();
					result.putExtra(EXTRA_KEY_RESULT, numbers[arg2]);
					setResult(RESULT_OK, result);
					finish();
				}
			});
		}
	}
}