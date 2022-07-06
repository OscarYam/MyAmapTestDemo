package com.example.myamaptestdemo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RailwayStationItem;
import com.example.myamaptestdemo.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class BusSegmentListAdapter extends BaseAdapter {
	private Context mContext;
	private List<SchemeBusStep> mBusStepList = new ArrayList<SchemeBusStep>();

	public BusSegmentListAdapter(Context context, List<BusStep> list) {
		this.mContext = context;
		SchemeBusStep start = new SchemeBusStep(null);
		start.setStart(true);
		mBusStepList.add(start);
		for (BusStep busStep : list) {
			if (busStep.getWalk() != null && busStep.getWalk().getDistance() > 0) {
				SchemeBusStep walk = new SchemeBusStep(busStep);
				walk.setWalk(true);
				mBusStepList.add(walk);
			}

			if (busStep.getBusLine() != null) {
				SchemeBusStep bus = new SchemeBusStep(busStep);
				bus.setBus(true);
				mBusStepList.add(bus);
			}

			if (busStep.getRailway() != null) {
				SchemeBusStep railway = new SchemeBusStep(busStep);
				railway.setRailway(true);
				mBusStepList.add(railway);
			}
			
			if (busStep.getTaxi() != null) {
				SchemeBusStep taxi = new SchemeBusStep(busStep);
				taxi.setTaxi(true);
			mBusStepList.add(taxi);
			}
		}
		SchemeBusStep end = new SchemeBusStep(null);
		end.setEnd(true);
		mBusStepList.add(end);
	}

	@Override
	public int getCount() {
		return mBusStepList.size();
	}

	@Override
	public Object getItem(int position) {
		return mBusStepList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_bus_segment, null);
			holder.parent = (RelativeLayout) convertView
					.findViewById(R.id.bus_item);
			holder.busLineName = (TextView) convertView
					.findViewById(R.id.bus_line_name);
			holder.busDirIcon = (ImageView) convertView
					.findViewById(R.id.bus_dir_icon);
			holder.busStationNum = (TextView) convertView
					.findViewById(R.id.bus_station_num);
			holder.busExpandImage = (ImageView) convertView
					.findViewById(R.id.bus_expand_image);
			holder.busDirUp = (ImageView) convertView
					.findViewById(R.id.bus_dir_icon_up);
			holder.busDirDown = (ImageView) convertView
					.findViewById(R.id.bus_dir_icon_down);
			holder.splitLine = (ImageView) convertView
					.findViewById(R.id.bus_seg_split_line);
			holder.expandContent = (LinearLayout) convertView
					.findViewById(R.id.expand_content);


			holder.busStationDep = (RelativeLayout) convertView
					.findViewById(R.id.bus_station_Dep);
			holder.busStationDepTvRl = (RelativeLayout) convertView
					.findViewById(R.id.bus_station_Dep_Text_Rl);
			holder.busStationDepIcon = (ImageView) convertView
					.findViewById(R.id.bus_station_Dep_icon);
			holder.busStationDepIconUp = (ImageView) convertView
					.findViewById(R.id.bus_station_Dep_icon_up);
			holder.busStationDepIconDown = (ImageView) convertView
					.findViewById(R.id.bus_station_Dep_icon_down);
			holder.busStationDepText = (TextView) convertView
					.findViewById(R.id.bus_station_Dep_text_tv);


			holder.busStationArr = (RelativeLayout) convertView
					.findViewById(R.id.bus_station_Arr);
			holder.busStationArrTvRl = (RelativeLayout) convertView
					.findViewById(R.id.bus_station_Arr_Text_Rl);
			holder.busStationArrIcon = (ImageView) convertView
					.findViewById(R.id.bus_station_Arr_icon);
			holder.busStationArrIconUp = (ImageView) convertView
					.findViewById(R.id.bus_station_Arr_icon_up);
			holder.busStationArrIconDown = (ImageView) convertView
					.findViewById(R.id.bus_station_Arr_icon_down);
			holder.busStationArrText = (TextView) convertView
					.findViewById(R.id.bus_station_Arr_text_tv);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final SchemeBusStep item = mBusStepList.get(position);
		if (position == 0) {
			holder.busDirIcon.setImageResource(R.drawable.dir_start);
			holder.busLineName.setText("出发");
			holder.busDirUp.setVisibility(View.INVISIBLE);
			holder.busDirDown.setVisibility(View.VISIBLE);


			holder.busStationDep.setVisibility(View.GONE);
			holder.busStationDepTvRl.setVisibility(View.GONE);
			holder.busStationArr.setVisibility(View.GONE);
			holder.busStationArrTvRl.setVisibility(View.GONE);


			holder.splitLine.setVisibility(View.GONE);
			holder.busStationNum.setVisibility(View.GONE);
			holder.busExpandImage.setVisibility(View.GONE);
			holder.parent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {

				}
			});
			holder.expandContent.setVisibility(View.GONE);
			return convertView;
		} else if (position == mBusStepList.size() - 1) {
			holder.busDirIcon.setImageResource(R.drawable.dir_end);
			holder.busLineName.setText("到达终点");
			holder.busDirUp.setVisibility(View.VISIBLE);
			holder.busDirDown.setVisibility(View.INVISIBLE);


			holder.busStationDep.setVisibility(View.GONE);
			holder.busStationDepTvRl.setVisibility(View.GONE);
			holder.busStationArr.setVisibility(View.GONE);
			holder.busStationArrTvRl.setVisibility(View.GONE);


			holder.busStationNum.setVisibility(View.GONE);
			holder.busExpandImage.setVisibility(View.GONE);
			holder.parent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {

				}
			});
			holder.expandContent.setVisibility(View.GONE);
			return convertView;
		} else {
			if (item.isWalk() && item.getWalk() != null && item.getWalk().getDistance() > 0) {
				holder.busDirIcon.setImageResource(R.drawable.dir13);
				holder.busDirUp.setVisibility(View.VISIBLE);
				holder.busDirDown.setVisibility(View.VISIBLE);

				holder.busStationDep.setVisibility(View.GONE);
				holder.busStationDepTvRl.setVisibility(View.GONE);
				holder.busStationArr.setVisibility(View.GONE);
				holder.busStationArrTvRl.setVisibility(View.GONE);

				holder.busLineName.setText("步行" + (int) item.getWalk().getDistance() + "米");
				holder.busStationNum.setVisibility(View.GONE);
				holder.busExpandImage.setVisibility(View.GONE);
				holder.parent.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {

					}
				});
				holder.expandContent.setVisibility(View.GONE);
				return convertView;
			} else if (item.isBus() && item.getBusLines().size() > 0) {
				holder.busDirIcon.setImageResource(R.drawable.dir14);
				holder.busDirUp.setVisibility(View.VISIBLE);
				holder.busDirDown.setVisibility(View.VISIBLE);
				holder.busLineName.setText(item.getBusLines().get(0).getBusLineName());
				holder.busStationNum.setVisibility(View.VISIBLE);

				int busStationNum = item.getBusLines().get(0).getPassStationNum() + 1;
				holder.busStationNum.setText(busStationNum + "站");

				if (busStationNum > 1) {
					holder.busExpandImage.setVisibility(View.VISIBLE);
				} else {
					holder.busExpandImage.setVisibility(View.INVISIBLE);
				}

				holder.busStationDep.setVisibility(View.VISIBLE);
				holder.busStationDepTvRl.setVisibility(View.VISIBLE);
				holder.busStationDepIconUp.setVisibility(View.VISIBLE);
				holder.busStationDepIconDown.setVisibility(View.VISIBLE);
				holder.busStationDepIcon.setImageResource(R.drawable.amap_bus);
				holder.busStationDepText.setText(item.getBusLine().getDepartureBusStation().getBusStationName());

				holder.busStationArr.setVisibility(View.VISIBLE);
				holder.busStationArrTvRl.setVisibility(View.VISIBLE);
				holder.busStationArrIconUp.setVisibility(View.VISIBLE);
				holder.busStationArrIconDown.setVisibility(View.VISIBLE);
				holder.busStationArrIcon.setImageResource(R.drawable.amap_bus);
				holder.busStationArrText.setText(item.getBusLine().getArrivalBusStation().getBusStationName());



				holder.arrowExpend = false;
				holder.busExpandImage.setImageResource(R.drawable.down);
//				Log.e("removeAllView: ", "holder.expandContent.removeAllViews()");
				holder.expandContent.removeAllViews();
				ArrowClick arrowClick = new ArrowClick(holder, item);

				holder.parent.setTag(position);
				holder.parent.setOnClickListener(arrowClick);

//				holder.busExpandImage.setTag(position);
//				holder.busExpandImage.setOnClickListener(arrowClick);


//				holder.expandContent.setVisibility(View.VISIBLE);
				return convertView;
			} else if (item.isRailway() && item.getRailway() != null) {
				holder.busDirIcon.setImageResource(R.drawable.dir16);
				holder.busDirUp.setVisibility(View.VISIBLE);
				holder.busDirDown.setVisibility(View.VISIBLE);
				holder.busLineName.setText(item.getRailway().getName());
//				holder.busStationNum.setVisibility(View.VISIBLE);
//				holder.busStationNum.setText((item.getRailway().getViastops().size() + 1) + "站");
//				holder.busExpandImage.setVisibility(View.VISIBLE);
				holder.busStationNum.setVisibility(View.GONE);
				holder.busExpandImage.setVisibility(View.INVISIBLE);

				holder.busStationDep.setVisibility(View.VISIBLE);
				holder.busStationDepTvRl.setVisibility(View.VISIBLE);
				holder.busStationDepIconUp.setVisibility(View.VISIBLE);
				holder.busStationDepIconDown.setVisibility(View.VISIBLE);
				holder.busStationDepIcon.setImageResource(R.drawable.amap_train);
				holder.busStationDepText.setText(item.getRailway().getDeparturestop().getName());

				holder.busStationArr.setVisibility(View.VISIBLE);
				holder.busStationArrTvRl.setVisibility(View.VISIBLE);
				holder.busStationArrIconUp.setVisibility(View.VISIBLE);
				holder.busStationArrIconDown.setVisibility(View.VISIBLE);
				holder.busStationArrIcon.setImageResource(R.drawable.amap_train);
				holder.busStationArrText.setText(item.getRailway().getArrivalstop().getName());

//				ArrowClick arrowClick = new ArrowClick(holder, item);
//
//				holder.parent.setTag(position);
//				holder.parent.setOnClickListener(arrowClick);

//				holder.busExpandImage.setTag(position);
//				holder.busExpandImage.setOnClickListener(arrowClick);

//				holder.expandContent.setVisibility(View.VISIBLE);
				holder.expandContent.setVisibility(View.GONE);
				return convertView;
			} else if (item.isTaxi() && item.getTaxi() != null) {
				holder.busDirIcon.setImageResource(R.drawable.dir14);
				holder.busDirUp.setVisibility(View.VISIBLE);
				holder.busDirDown.setVisibility(View.VISIBLE);

				holder.busStationDep.setVisibility(View.GONE);
				holder.busStationDepTvRl.setVisibility(View.GONE);
				holder.busStationArr.setVisibility(View.GONE);
				holder.busStationArrTvRl.setVisibility(View.GONE);

				holder.busLineName.setText("打车到终点");
				holder.busStationNum.setVisibility(View.GONE);
				holder.busExpandImage.setVisibility(View.GONE);
				holder.parent.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {

					}
				});
				holder.expandContent.setVisibility(View.GONE);
				return convertView;
			}

			return convertView;
		}
	}

	private class ViewHolder {
		public RelativeLayout parent;
		TextView busLineName;
		ImageView busDirIcon;
		ImageView busDirUp;
		ImageView busDirDown;
		TextView busStationNum;
		ImageView busExpandImage;
		ImageView splitLine;
		LinearLayout expandContent;
		boolean arrowExpend = false;
		RelativeLayout busStationDep;
		RelativeLayout busStationDepTvRl;
		ImageView busStationDepIcon;
		ImageView busStationDepIconUp;
		ImageView busStationDepIconDown;
		TextView busStationDepText;
		RelativeLayout busStationArr;
		RelativeLayout busStationArrTvRl;
		ImageView busStationArrIcon;
		ImageView busStationArrIconUp;
		ImageView busStationArrIconDown;
		TextView busStationArrText;
	}
	
	
	private class ArrowClick implements OnClickListener {
		private ViewHolder mHolder;
		private SchemeBusStep mItem;

		public ArrowClick(final ViewHolder holder, final SchemeBusStep item) {
			mHolder = holder;
			mItem = item;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = Integer.parseInt(String.valueOf(v.getTag()));

//			ToastUtil.show(mContext, "Position_: " + position + "_mHolder.arrowExpend : " + mHolder.arrowExpend);

			mHolder.expandContent.removeAllViews();


			mItem = mBusStepList.get(position);
			if (mItem.isBus()) {
				if (mHolder.arrowExpend == false) {
					mHolder.arrowExpend = true;
					mHolder.busExpandImage.setImageResource(R.drawable.up);

					for (BusStationItem station : mItem.getBusLine().getPassStations()) {
						addBusStation(station, 2);
					}

					mHolder.expandContent.setVisibility(View.VISIBLE);

				} else {
					mHolder.arrowExpend = false;
					mHolder.busExpandImage.setImageResource(R.drawable.down);
					mHolder.expandContent.setVisibility(View.GONE);
				}
			} else if (mItem.isRailway()) {
				if (mHolder.arrowExpend == false) {
					mHolder.arrowExpend = true;
					mHolder.busExpandImage.setImageResource(R.drawable.up);

					for (RailwayStationItem station : mItem.getRailway().getViastops()) {
						addRailwayStation(station, 2);
					}

					mHolder.expandContent.setVisibility(View.VISIBLE);
				} else {
					mHolder.arrowExpend = false;
					mHolder.busExpandImage.setImageResource(R.drawable.down);
					mHolder.expandContent.setVisibility(View.GONE);
				}
			}
		}

		private void addBusStation(BusStationItem station, int type) {
			LinearLayout ll = (LinearLayout) View.inflate(mContext,
					R.layout.item_bus_segment_ex, null);
			TextView tv = (TextView) ll.findViewById(R.id.bus_line_station_name);
			tv.setText(station.getBusStationName());
			ImageView iv = (ImageView) ll.findViewById(R.id.bus_dir_icon);
			switch (type) {
				case 0:
				case 1:
					iv.setImageResource(R.drawable.amap_bus);
					break;

//				case 1:
//					iv.setImageResource(R.drawable.amap_bus);
//					break;

				default:
					iv.setImageResource(R.drawable.dir_station);
					break;
			}

			mHolder.expandContent.addView(ll);
		}
		
		private void addRailwayStation(RailwayStationItem station, int type) {
			LinearLayout ll = (LinearLayout) View.inflate(mContext,
					R.layout.item_bus_segment_ex, null);
			TextView tv = (TextView) ll.findViewById(R.id.bus_line_station_name);
			tv.setText(station.getName()+ " "+getRailwayTime(station.getTime()));
			ImageView iv = (ImageView) ll.findViewById(R.id.bus_dir_icon);
			switch (type) {
				case 0:
				case 1:
					iv.setImageResource(R.drawable.amap_train);
					break;

//				case 1:
//					iv.setImageResource(R.drawable.amap_train);
//					break;

				default:
					iv.setImageResource(R.drawable.dir_station);
					break;
			}

			mHolder.expandContent.addView(ll);
		}
	}

	public static String getRailwayTime(String time) {
		return time.substring(0, 2) + ":" + time.substring(2, time.length());
	}

}
