package com.example.myamaptestdemo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.Polyline;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviTTSType;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.enums.TravelStrategy;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviStep;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.model.NaviPoi;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiItemExtension;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.example.myamaptestdemo.databinding.ActivityMainBinding;
import com.example.myamaptestdemo.ui.login.LoginResult;
import com.example.myamaptestdemo.ui.login.LoginViewModel;
import com.example.myamaptestdemo.ui.login.LoginViewModelFactory;
import com.example.myamaptestdemo.ui.settings.SettingsFragmentExViewModel;
import com.example.myamaptestdemo.util.AMapUtil;
import com.example.myamaptestdemo.util.DensityUtils;
import com.example.myamaptestdemo.util.TTSController;
import com.example.myamaptestdemo.util.ToastUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import overlay.BusRouteOverlay;
import overlay.PoiOverlay;

public class MainActivity extends AppCompatActivity implements AMap.OnPOIClickListener,
        AMap.OnMarkerClickListener, AMap.OnMapTouchListener, AMap.OnMapClickListener,
        AMap.OnMapLongClickListener {
    private MapView mMapView;
    private AMap mAMap;
    private Context mContext;
    private static final int STROKE_COLOR = Color.argb(0, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private boolean mTouchevent = false;
    private boolean floatBtClkFlag = false;
    private LatLng myLocation;
    private AMapLocation mAMplocation;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private TextView textView = null;

    private androidx.appcompat.widget.SearchView mSV = null;
    private ListView mLV = null;

    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类

    private Marker mlastMarker;

    private PoiSearch poiSearch; // POI搜索
    private PoiOverlay poiOverlay;// poi图层

    private PoiSearch mPoiSearch;
    private ConstraintLayout mPoiDetail, routeDetail, search_tool;
    private Button routeDetailClose;
    private TextView mPoiName, mPoiAddress, mPoiInfo, routeInfo, routeInfo2;
    private MarkerOptions myMarkOptiopns;
    private GeocodeSearch mGeocoderSearch;
    private LinearLayout mLinearLyout;
    private ConstraintLayout routemap_header;
    private LatLonPoint mStartPoint = null;
    private LatLonPoint mEndPoint = null;
    private String mCurrentCityName = "北京";
    private RouteSearch mRouteSearch;
    private ImageView mDrive, mBus, mWalk, mRide;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;
    private final int ROUTE_TYPE_RIDE = 4;

    private LinearLayout busResult;
    private ListView busResultList, busSegmentLis;
    private boolean busSegmentLisFlag = false;

    private boolean mSearchEvent = false;

    private AMapNaviView mAMapNaviView;
    private AMapNavi mAMapNavi;
    private AMapNaviViewOptions mapNaviViewOptions;
    protected TTSController mTtsManager;

    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>(); // 保存当前算好的路线层
    private HashMap<Integer, AMapNaviPath> paths; // 保存当前算好的路线

    private SparseArray<Tip> mSearchTips = new SparseArray<Tip>(); // 暂存当前的搜索提示

    private LatLng cameraCenterPosition = null; // 视觉居中坐标点

    private ActivityMainBinding binding;

    private FloatingActionButton dragfABUser;
    private FloatingActionButton dragfABLocation;

    private FrameLayout loginFragment;

    private LoginViewModel loginViewModel;
    private SettingsFragmentExViewModel settingsFragmentExViewModel;
    private MainActivityViewModel mainActivityViewModel;

    private FrameLayout settingsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);
        settingsFragmentExViewModel = new ViewModelProvider(this).get(SettingsFragmentExViewModel.class);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = this.getApplicationContext();

        mMapView = binding.map;
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        init();

        settingsFragment = binding.settingFragment;
        settingsFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 设置点击监听事件，避免该 view 被点击时穿透到底层
//                Log.e("settingsFragment", "settingsFragment onClick");
            }
        });
        loginFragment = binding.loginFragment;
        loginFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 设置点击监听事件，避免该 view 被点击时穿透到底层
//                Log.e("loginFrament", "loginFrament onClick");
            }
        });

        dragfABUser = binding.dragFbUser;
        dragfABLocation = binding.dragFbLocation;


        settingsFragmentExViewModel.getFrameVisibility().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == View.GONE) {
                    // 从设置页面返回主界面
                    if (settingsFragment.getVisibility() == View.VISIBLE) {
                        addAnimation(settingsFragment, View.GONE, 1);
                        dragfABLocation.show(); // 显示悬浮按钮

                        settingsFragment.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                settingsFragment.setVisibility(View.GONE);
                                mainActivityViewModel.getGoHome().postValue(true); // 请求返回主页
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    }
                }

            }
        });


        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(LoginResult loginResult) {
                if (loginResult != null) {

                    if (loginResult.getError() != null) {
                        dragfABUser.setImageResource(R.drawable.btn_rating_star_on_normal_holo_light);

                        if (settingsFragment.getVisibility() == View.VISIBLE) {
                            // 退出登录时隐藏设置页面
                            addAnimation(settingsFragment, View.GONE, 1);
                            dragfABLocation.show(); // 显示悬浮按钮

                            settingsFragment.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    settingsFragment.setVisibility(View.GONE);
                                    mainActivityViewModel.getGoHome().postValue(true); // 请求返回主页
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                        }

//                        Log.e("loginResult.getError()", getString(loginResult.getError()));
                    }

                    if (loginResult.getSuccess() != null) {
                        dragfABUser.setImageResource(R.drawable.btn_rating_star_on_pressed_holo_dark);
                        addAnimation(loginFragment, View.GONE, 1);
                        dragfABLocation.show(); // 显示悬浮按钮
                    }

                }
            }
        });


        dragfABUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (loginViewModel.isLoggedIn()) {
                    // 已登录,只显示或隐藏 设置页面

                    if (settingsFragment.getVisibility() != View.VISIBLE) {
                        // 设置页面未显示
                        addAnimation(settingsFragment, View.VISIBLE, 1); // 显示设置页面
                        dragfABLocation.hide(); // 隐藏悬浮按钮
                    } else {
                        // 设置页面已显示

                        // 隐藏软键盘
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                        addAnimation(settingsFragment, View.GONE, 1); // 隐藏设置页面
                        dragfABLocation.show(); // 显示悬浮按钮

                        settingsFragment.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                settingsFragment.setVisibility(View.GONE);
                                mainActivityViewModel.getGoHome().postValue(true); // 请求返回主页
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }

                } else {
                    // 未登录,只显示或隐藏 登录页面

                    if (loginFragment.getVisibility() != View.VISIBLE) {
                        // 登录页面未显示
                        addAnimation(loginFragment, View.VISIBLE, 1); // 显示登录页面
                        dragfABLocation.hide(); // 隐藏悬浮按钮
                    } else {
                        // 登录页面已显示
                        addAnimation(loginFragment, View.GONE, 1); // 隐藏登录页面
                        dragfABLocation.show(); // 显示悬浮按钮

                        // 隐藏软键盘
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                        // 清除登录页面密码
                        EditText passwordEditText = loginFragment.findViewById(R.id.password);
                        passwordEditText.setText("");
                    }

                }

            }
        });

        loginViewModel.getFrameVisibility().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == View.GONE) {
                    // 从 LoginFragment 返回
                    // 登录页面已显示
                    addAnimation(loginFragment, View.GONE, 1); // 隐藏登录页面
                    dragfABLocation.show(); // 显示悬浮按钮

                    // 隐藏软键盘
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                    // 清除登录页面密码
                    EditText passwordEditText = loginFragment.findViewById(R.id.password);
                    passwordEditText.setText("");
                }
            }
        });

        dragfABLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.e("dragfAB", "dragfAB onClick");

                if(!floatBtClkFlag) {
                    if (routemap_header.getVisibility() == View.GONE) {
                        mAMap.clear(true); // true|false 是|否保留myLocationOverlay(自定义定位小蓝点)
                    }

                    if (mPoiDetail.getVisibility() == View.VISIBLE) {
                        // 为mPoiDetail添加补间动画
                        addAnimation(mPoiDetail, View.GONE, 0);

                        if (mPoiDetail.getAnimation() != null) {
                            mPoiDetail.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    search_tool.setVisibility(View.VISIBLE);
                                    mPoiDetail.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                        }
                    }

                    // 设置当前定位位置于地图居中
                    setCameraCenter(myLocation);

                    setupMyLoctstyle(mAMap, 4);
                    floatBtClkFlag = true;
                    mTouchevent = false;
                }
            }
        });

        myMarkOptiopns = new MarkerOptions();

        mAMap.setOnPOIClickListener(this);
        mAMap.setOnMarkerClickListener(this);
        mAMap.setOnMapTouchListener(this);
        mAMap.setOnMapClickListener(this);
        mAMap.setOnMapLongClickListener(this);
        mAMap.setOnPolylineClickListener(new AMap.OnPolylineClickListener() {
            // 设置polyline点击事件监听接口
            @Override
            public void onPolylineClick(Polyline polyline) {

                if(!mTouchevent) {
                    setupMyLoctstyle(mAMap, 5);
                    mTouchevent = true;
                    floatBtClkFlag = false;
                }

                try {
                    String clickedPolylineId = polyline.getId();
                    for (int i = 0; i < routeOverlays.size(); i++) {
                        int routeId = routeOverlays.keyAt(i);
                        String matchId = routeOverlays.get(routeId).getPolylineIdList().get(0);
                        if (clickedPolylineId.equals(matchId)) {
                            // 突出选中的路径
                            routeOverlays.get(routeId).setTransparency(1);
                            routeOverlays.get(routeId).setZindex(1);
                            routeOverlays.get(routeId).zoomToSpan();

                            // 告诉AMapNavi 选择的哪条路
                            mAMapNavi.selectRouteId(routeId);

                            AMapNaviPath mAMapNaviPath = paths.get(routeId);
                            String routeLabel = mAMapNaviPath.getLabels();
                            if (!routeLabel.contains("方案")) {
                                routeLabel += "方案";
                            }
                            routeInfo.setText(routeLabel);
                            routeInfo.setVisibility(View.VISIBLE);

                            routeInfo2.setText("距离: " + convertMeters2KM(mAMapNaviPath.getAllLength()) + " | "
                                    + "耗时: " + DensityUtils.convertSec2MinSp(mAMapNaviPath.getAllTime()) + " | "
                                    + "交通灯数: " + getTrafficNumber(mAMapNaviPath));
                            routeInfo2.setVisibility(View.VISIBLE);
                        } else {
                            // 调暗未选中的路径
                            routeOverlays.get(routeId).setTransparency(0.3f);
                            routeOverlays.get(routeId).setZindex(0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        initLocation();
        startLocation();

        textView = binding.textView;
        textView.setTextColor(Color.RED);
        textView.setVisibility(View.GONE); // 隐藏textView

        search_tool = binding.searchTool;

        mLV = binding.mLV;
        mLV.setVisibility(View.GONE);

        mSV = binding.mSV;
        mSV.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // poi 搜索

                mSV.clearFocus(); // 清除焦点
                mSV.onActionViewCollapsed(); // 将当前状态切换为收缩状态
                mSV.setImeOptions(EditorInfo.IME_ACTION_SEARCH); // 改变软键盘的右下角的回车键值为搜索

                showProgressDialog();// 显示进度框
                currentPage = 0;

                // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
                query = new PoiSearch.Query(s, "", mCurrentCityName);
//                query = new PoiSearch.Query(s, "", "");

                query.setPageSize(10);// 设置每页最多返回多少条poiitem
                query.setPageNum(currentPage);// 设置查第一页

                poiSearch = new PoiSearch(mContext, query);

//                poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(myLocation.latitude, myLocation.longitude), 1000, true)); // 设置搜索区域为以lp点为圆心，其周围5000米范围
//                poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(22.514984, 113.369066), 1000, true));

                poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                    @Override
                    public void onPoiSearched(PoiResult result, int rCode) {

                        if(!mTouchevent) {
                            setupMyLoctstyle(mAMap, 5);
                            mTouchevent = true;
                            floatBtClkFlag = false;
                        }

                        dissmissProgressDialog(); // 隐藏对话框

                        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                            if (result != null && result.getQuery() != null) {
                                // 搜索poi的结果

                                if (result.getQuery().equals(query)) {
                                    // 比较两个查询条件是否相同（不包括查询第几页）

                                    poiResult = result;
                                    // 取得搜索到的poiitems有多少页
                                    List<PoiItem> poiItems = poiResult.getPois(); // 取得第一页的poiitem数据，页数从数字0开始
                                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys(); // 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                                    int poiSize = poiItems.size();
                                    String searchStr = result.getQuery().getQueryString();
                                    if (poiSize > 0) {
                                        // 结果筛选
                                        for (int i = 0 ; i < poiSize; i++) {
                                            if (!poiItems.get(i).getTitle().contains(searchStr)) {
                                                poiItems.remove(i);
                                                poiSize = poiItems.size();
                                                i--;
                                            }
                                        }

                                        if (poiSize < 1) {
                                            Toast.makeText(mContext, R.string.no_result, Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        // 清理之前搜索结果的marker
                                        if (poiOverlay != null) {
                                            poiOverlay.removeFromMap();
                                        }

                                        poiOverlay = new PoiOverlay(mAMap, poiItems);

                                        // 并还原点击marker样式
                                        if (mlastMarker != null) {
                                            resetlastmarker();
                                        }

                                        mAMap.clear(true); // 清理之前的图标

                                        poiOverlay.addToMap();
                                        poiOverlay.zoomToSpan();

                                        mSearchEvent = true;

                                    } else if (suggestionCities != null && suggestionCities.size() > 0) {
                                        showSuggestCity(suggestionCities);
                                    } else {
                                        ToastUtil.show(mContext, R.string.no_result);
                                    }
                                }
                            } else {
                                ToastUtil.show(mContext, R.string.no_result);
                            }
                        } else {
                            ToastUtil.showerror(mContext, rCode);
                        }
                    }

                    @Override
                    public void onPoiItemSearched(PoiItem poiItem, int i) {

                    }
                });
                poiSearch.searchPOIAsyn();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!TextUtils.isEmpty(s)) {

                    if(!mTouchevent) {
                        setupMyLoctstyle(mAMap, 5);
                        mTouchevent = true;
                        floatBtClkFlag = false;
                    }

                    InputtipsQuery inputquery = new InputtipsQuery(s.trim(), mCurrentCityName);
                    Inputtips inputTips = new Inputtips(mContext, inputquery);
                    inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
                        @Override
                        public void onGetInputtips(List<Tip> tipList, int rCode) {
                            if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                                // 正确返回

//                                List<String> listString = new ArrayList<String>();
//                                String adCode = mAMplocation.getAdCode(); // 获取当前定位的区域编码
//                                Log.e("onGetInputtips", "mAMplocation.getAdCode() = " + adCode);

                                List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                                mSearchTips.clear();

                                for (Tip tip : tipList) {
                                    if (!tip.getPoiID().isEmpty()) {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("Name", tip.getName());
                                        String address;

                                        // TypeCode - 类型编码 （POI类型请在网站“相关下载”处获取）
                                        if (tip.getTypeCode().equals("150700")) {
                                            // 如果是公交车站
                                            address = tip.getDistrict() + tip.getName();
                                        } else {
                                            address = tip.getDistrict() + tip.getAddress() + tip.getName();
                                        }

                                        map.put("Address", address);
                                        data.add(map);

                                        tip.setAddress(address);
                                        mSearchTips.put(tipList.indexOf(tip), tip);
                                    }
                                }

                                SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, data,
                                        R.layout.my_simple_list_item_2,
                                        new String[]{"Name", "Address"},
                                        new int[]{R.id.text1, R.id.text2});

                                mLV.setAdapter(simpleAdapter);
                                simpleAdapter.notifyDataSetChanged();

                                mLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                        int key = mSearchTips.keyAt(i);

                                        /**
                                         * public PoiItem(java.lang.String id,
                                         *                LatLonPoint point,
                                         *                java.lang.String title,
                                         *                java.lang.String snippet)
                                         * 根据给定的参数构造一个PoiItem 的新对象。
                                         *
                                         * java.lang.String id - POI 的标识
                                         * LatLonPoint point - 该POI的位置
                                         * java.lang.String title - 该POI的名称
                                         * java.lang.String snippet - POI的地址
                                         * */
                                        PoiItem mCurrentPoi = new PoiItem(mSearchTips.get(key).getPoiID(),
                                                mSearchTips.get(key).getPoint(),
                                                mSearchTips.get(key).getName(),
                                                mSearchTips.get(key).getAddress());
                                        mCurrentPoi.setPoiExtension(new PoiItemExtension("", "")); // PoiItemExtension (POI的评分, POI的营业时间)
                                        mCurrentPoi.setTypeCode(mSearchTips.get(key).getTypeCode());
//                                        mCurrentPoi.setCityName(mSearchTips.get(key).getDistrict());

                                        List<PoiItem> poiItems = new ArrayList<>();
                                        poiItems.add(mCurrentPoi);

                                        // 清理之前搜索结果的marker
                                        if (poiOverlay != null) {
                                            poiOverlay.removeFromMap();
                                        }

                                        poiOverlay = new PoiOverlay(mAMap, poiItems);

                                        // 并还原点击marker样式
                                        if (mlastMarker != null) {
                                            resetlastmarker();
                                        }

                                        mAMap.clear(true); // 清理之前的图标

                                        poiOverlay.addToMap();
                                        poiOverlay.zoomToSpan();

                                        mSearchEvent = true;

                                        mSV.clearFocus(); //清除焦点，收软键盘
                                        mSV.onActionViewCollapsed();
                                        mSV.setImeOptions(EditorInfo.IME_ACTION_SEARCH); // 改变软键盘的右下角的回车键值为搜索

                                    }
                                });

                            } else {
                                ToastUtil.showerror(getApplicationContext(), rCode);
                            }
                        }
                    });
                    inputTips.requestInputtipsAsyn();


                    mLV.setVisibility(View.VISIBLE);
                } else {
                    mLV.clearTextFilter();
                    mLV.setVisibility(View.GONE);
                }
                return false;
            }
        });


        mLinearLyout = binding.linearLayout;
        mLinearLyout.setVisibility(View.GONE);

        mPoiDetail = binding.poiDetail;
        mPoiDetail.setVisibility(View.GONE); // 设置初始状态
        mPoiDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtil.show(mContext, "init: PoiDetail Click event!");
            }
        });


        routeSearchInit();


        mPoiName = binding.poiName;
        mPoiAddress = binding.poiAddress;
        mPoiInfo = binding.poiInfo;

        mPoiSearch = new PoiSearch(this, null);
        mPoiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                // 返回POI搜索异步处理的结果
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
                // poi id搜索的结果回调
                if (i == AMapException.CODE_AMAP_SUCCESS) {
                    if (poiItem != null) {
                        setPoiItemDisplayContent(poiItem);
                        mLinearLyout.setVisibility(View.VISIBLE);
                        mPoiDetail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                ToastUtil.show(mContext, "onPoiItemSearched: Online PoiDetail Click event!");

                                if (textView.getVisibility() == View.VISIBLE) {
                                    textView.setVisibility(View.GONE);
                                } else {
                                    textView.setVisibility(View.VISIBLE);
                                }

                                Log.e("onPoiItemSearched", "onPoiItemSearched: Online PoiDetail Click event!");
                            }
                        });
                    }
                } else {
                    ToastUtil.showerror(mContext, i);

                    add_myMarker(myMarkOptiopns);
                    show_mPoiDetail_OffLine();
                }

                // 为mPoiDetail添加补间动画
                addAnimation(mPoiDetail, View.VISIBLE, 0);

                search_tool.setVisibility(View.GONE);
                busResult.setVisibility(View.VISIBLE);
                busResultList.setVisibility(View.GONE);
                busSegmentLis.setVisibility(View.GONE);

                // 设置 POI 点地图居中
                setCameraCenter(myMarkOptiopns.getPosition());
            }
        });


        mGeocoderSearch = new GeocodeSearch(this);
        mGeocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (i == AMapException.CODE_AMAP_SUCCESS) {
                    if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null) {
                        mPoiName.setText(myMarkOptiopns.getTitle());
                        if (regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
//                            mPoiAddress.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress()
//                                    .replaceAll(regeocodeResult.getRegeocodeAddress().getProvince() + regeocodeResult.getRegeocodeAddress().getCity(), "") + "附近");
                            mPoiAddress.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress()
                                    .replaceAll(regeocodeResult.getRegeocodeAddress().getProvince(), "") + "附近");
                        } else {
                            mPoiAddress.setText("");
                        }

                        myMarkOptiopns.snippet(mPoiAddress.getText().toString());
                        add_myMarker(myMarkOptiopns);
                        mPoiInfo.setVisibility(View.GONE);

                        mLinearLyout.setVisibility(View.VISIBLE);
                        mPoiDetail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                ToastUtil.show(mContext, "onRegeocodeSearched: PoiDetail Click event!");

                                if (textView.getVisibility() == View.VISIBLE) {
                                    textView.setVisibility(View.GONE);
                                } else {
                                    textView.setVisibility(View.VISIBLE);
                                }


                                Log.e("onRegeocodeSearched", "onRegeocodeSearched: PoiDetail Click event!");
                            }
                        });
                    }
                } else {
                    ToastUtil.showerror(mContext, i);

                    add_myMarker(myMarkOptiopns);
                    show_mPoiDetail_OffLine();
                }

                // 为mPoiDetail添加补间动画
                addAnimation(mPoiDetail, View.VISIBLE, 0);

                search_tool.setVisibility(View.GONE);
                busResult.setVisibility(View.VISIBLE);
                busResultList.setVisibility(View.GONE);
                busSegmentLis.setVisibility(View.GONE);

//                // 设置 长按 点地图居中
//                setCameraCenter(myMarkOptiopns.getPosition());
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });

        mAMapNaviView = binding.naviView;
        mAMapNaviView.onCreate(savedInstanceState);
//        mAMapNaviView.setNaviMode(AMapNaviView.NORTH_UP_MODE); // 北方向上模式
        mAMapNaviView.setNaviMode(AMapNaviView.CAR_UP_MODE); // 车头向上模式

        mapNaviViewOptions = new AMapNaviViewOptions();
//        AMapNaviViewOptions mapNaviViewOptions = new AMapNaviViewOptions();
        mapNaviViewOptions.setAfterRouteAutoGray(true); // 通过路线是否自动置灰 可以使用RouteOverlayOptions.setPassRoute(Bitmap)改变纹理
        mapNaviViewOptions.setAutoChangeZoom(true); // 设置是否开启动态比例尺 (锁车态下自动进行地图缩放变化)
        mapNaviViewOptions.setAutoDisplayOverview(true); // 设置是否自动全览模式，即在算路成功后自动进入全览模式
        mapNaviViewOptions.setAutoDrawRoute(true); // 设置是否自动画路
        mapNaviViewOptions.setAutoLockCar(true); // 设置6秒后是否自动锁车
//        mapNaviViewOptions.setLockMapDelayed(3000); // 设置锁定地图延迟毫秒数
        mapNaviViewOptions.setLaneInfoShow(true); // 设置是否显示道路信息view
        mapNaviViewOptions.setLeaderLineEnabled(Color.argb(255, 255, 0, 0)); // 设置是否绘制牵引线（当前位置到目的地的指引线）颜色格式ARGB
        mapNaviViewOptions.setLayoutVisible(true); // 设置导航界面UI是否显示
        mapNaviViewOptions.setModeCrossDisplayShow(true); // 设置是否显示路口放大图(路口模型图)
        mapNaviViewOptions.setNaviArrowVisible(true); // 设置路线转向箭头隐藏和显示
//        mapNaviViewOptions.setRealCrossDisplayShow(true); // 设置是否显示路口放大图(实景图)
//        mapNaviViewOptions.setRouteListButtonShow(true); //设置导航界面是否显示路线全览按钮
//        mapNaviViewOptions.setSecondActionVisible(false); //设置是否显示下下个路口的转向引导，默认不显示 注意：该接口仅驾车模式有效
//        mapNaviViewOptions.setSettingMenuEnabled(true); // 设置菜单按钮是否在导航界面显示
        mapNaviViewOptions.setSensorEnable(true); // 自车图标是否使用陀螺仪方向（只在骑行导航和步行导航下有效）
//        mapNaviViewOptions.setTrafficBarEnabled(false); // 设置路况光柱条是否显示（只适用于驾车导航，需要联网）
//        mapNaviViewOptions.setTrafficLayerEnabled(true); // 设置[实时交通图层开关按钮]是否显示（只适用于驾车导航，需要联网）
        mapNaviViewOptions.setTrafficLine(true); // 设置地图上是否显示交通路况（彩虹线）(拥堵-红色，畅通-绿色，缓慢-黄色，未知-蓝色)
        mapNaviViewOptions.setAutoNaviViewNightMode(true); // 设置是否开启自动黑夜模式切换，默认为false，不自动切换

//        mAMapNaviView.setViewOptions(mapNaviViewOptions);

        mAMapNaviView.setAMapNaviViewListener(new AMapNaviViewListener() {
            @Override
            public void onNaviSetting() {
                // 界面右下角设置按钮的点击回调
                Toast.makeText(getApplicationContext(), "Button Clicked!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNaviCancel() {
                // 导航页面左下角返回按钮点击后弹出的『退出导航』对话框中选择『确定』后的回调接口

                // 为AMapNaviView添加补间动画
                addAnimation(mAMapNaviView, View.GONE, 1);
                dragfABLocation.show();
                dragfABUser.show();

                mAMapNavi.stopNavi();
                mAMapNavi.stopSpeak();
                mTtsManager.stopSpeaking();
            }

            @Override
            public boolean onNaviBackClick() {
                // 导航页面左下角"退出"按钮的点击回调

                return false;
            }

            @Override
            public void onNaviMapMode(int i) {
                // 导航视角变化回调

            }

            @Override
            public void onNaviTurnClick() {
                // 界面左上角转向操作的点击回调 (已过时)

            }

            @Override
            public void onNextRoadClick() {
                // 界面下一道路名称的点击回调 (已过时)

            }

            @Override
            public void onScanViewButtonClick() {
                // 界面全览按钮的点击回调

            }

            @Override
            public void onLockMap(boolean b) {
                // 已过时。 请使用 AMapNaviViewListener.onNaviViewShowMode(int)
                //是否锁定地图的回调。
                //参数:
                //isLock - true代表锁车状态，地图未锁定。false代表非锁车状态，地图锁定。

            }

            @Override
            public void onNaviViewLoaded() {
                // 导航view加载完成回调

            }

            @Override
            public void onMapTypeChanged(int i) {
                // AMapNaviView地图白天黑夜模式切换回调
                //参数:
                //mapType - 枚举值参考AMap类 3-夜间模式 4-白天模式

            }

            @Override
            public void onNaviViewShowMode(int i) {
                // 导航视图展示模式变化回调
                //参数:
                //showMode - 展示模式，具体类型可参考AMapNaviViewShowMode
                //普通状态 - SHOW_MODE_DEFAULT
                //全览状态 - SHOW_MODE_DISPLAY_OVERVIEW
                //锁车状态 - SHOW_MODE_LOCK_CAR

            }
        });
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        // 设置使用内部语音播报 isUseInnerVoice设置为true以后，AMapNaviListener.onGetNavigationText(int, java.lang.String)接口会继续返回文字，默认为false
        //                Log.e("onGetNavigationText", "NaviTTSType = " + i);
        //                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        // 打断当前播报 注：收到该类型时，表明后续会有较高优先级的播报，建议立即打断当前播报，清空播报队列
        // 清理地图上的所有覆盖物
        //                mAMap.setMapType(AMap.MAP_TYPE_NIGHT); // 夜景模式
        //                mAMap.setMapType(AMap.MAP_TYPE_BUS); // 公交模式
        // 导航模式
        //                routeOverlays.clear(); //清空上次计算的路径列表
        //                routeInfo.setText("路线标签: " + mAMapNaviPath.getLabels());
        // 设置routeInfo图标
        // 启动模拟导航
        //                        mAMapNavi.startNavi(NaviType.EMULATOR);
        // 启动导航
        // 为AMapNaviView添加补间动画
        // 清理地图上的所有覆盖物
        // 设置routeInfo图标
        //                Log.e("onCalculateRouteFailure", "errorCode = " + aMapCalcRouteResult.getErrorCode());
        AMapNaviListener mapNaviListener = new AMapNaviListener() {
            @Override
            public void onInitNaviFailure() {

            }

            @Override
            public void onInitNaviSuccess() {

            }

            @Override
            public void onStartNavi(int i) {

            }

            @Override
            public void onTrafficStatusUpdate() {

            }

            @Override
            public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

            }

            @Override
            public void onGetNavigationText(int i, String s) {
                // 设置使用内部语音播报 isUseInnerVoice设置为true以后，AMapNaviListener.onGetNavigationText(int, java.lang.String)接口会继续返回文字，默认为false
//                Log.e("onGetNavigationText", "NaviTTSType = " + i);
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                // 打断当前播报 注：收到该类型时，表明后续会有较高优先级的播报，建议立即打断当前播报，清空播报队列
                if (i == NaviTTSType.INTERRUPT_CURRENT) {
                    mTtsManager.stopSpeaking();
                    mAMapNavi.stopSpeak();
                }
            }

            @Override
            public void onGetNavigationText(String s) {

            }

            @Override
            public void onEndEmulatorNavi() {

            }

            @Override
            public void onArriveDestination() {

            }

            @Override
            public void onCalculateRouteFailure(int i) {

            }

            @Override
            public void onReCalculateRouteForYaw() {

            }

            @Override
            public void onReCalculateRouteForTrafficJam() {

            }

            @Override
            public void onArrivedWayPoint(int i) {

            }

            @Override
            public void onGpsOpenStatus(boolean b) {

            }

            @Override
            public void onNaviInfoUpdate(NaviInfo naviInfo) {

            }

            @Override
            public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

            }

            @Override
            public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

            }

            @Override
            public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

            }

            @Override
            public void showCross(AMapNaviCross aMapNaviCross) {

            }

            @Override
            public void hideCross() {

            }

            @Override
            public void showModeCross(AMapModelCross aMapModelCross) {

            }

            @Override
            public void hideModeCross() {

            }

            @Override
            public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

            }

            @Override
            public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

            }

            @Override
            public void hideLaneInfo() {

            }

            @Override
            public void onCalculateRouteSuccess(int[] ints) {

            }

            @Override
            public void notifyParallelRoad(int i) {

            }

            @Override
            public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

            }

            @Override
            public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

            }

            @Override
            public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

            }

            @Override
            public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

            }

            @Override
            public void onPlayRing(int i) {

            }

            @Override
            public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

                if (!mTouchevent) {
                    setupMyLoctstyle(mAMap, 5);
                    mTouchevent = true;
                    floatBtClkFlag = false;
                }

                dissmissProgressDialog();

                mAMap.clear(true);// 清理地图上的所有覆盖物

//                mAMap.setMapType(AMap.MAP_TYPE_NIGHT); // 夜景模式
//                mAMap.setMapType(AMap.MAP_TYPE_BUS); // 公交模式
                mAMap.setMapType(AMap.MAP_TYPE_NAVI); // 导航模式

//                routeOverlays.clear(); //清空上次计算的路径列表
                clearRoute();
                int[] routeIds = aMapCalcRouteResult.getRouteid();

                paths = mAMapNavi.getNaviPaths();

                for (int id : routeIds) {
                    AMapNaviPath path = paths.get(id);
                    if (path != null) {
                        drawRoutes(id, path);
                    }
                }

                int routeId = routeOverlays.keyAt(0);
                routeOverlays.get(routeId).setTransparency(1);
                routeOverlays.get(routeId).setZindex(1);
                routeOverlays.get(routeId).zoomToSpan();


                AMapNaviPath mAMapNaviPath = paths.get(routeId);
//                routeInfo.setText("路线标签: " + mAMapNaviPath.getLabels());

                String routeLabel = mAMapNaviPath.getLabels();
                if (!routeLabel.contains("方案")) {
                    routeLabel += "方案";
                }
                routeInfo.setText(routeLabel);

                // 设置routeInfo图标
                Drawable drawable = getResources().getDrawable(R.drawable.route);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                routeInfo.setCompoundDrawables(drawable, null, null, null);
                routeInfo.setVisibility(View.VISIBLE);

                routeInfo2.setText("距离: " + convertMeters2KM(mAMapNaviPath.getAllLength()) + " | "
                        + "耗时: " + DensityUtils.convertSec2MinSp(mAMapNaviPath.getAllTime()) + " | "
                        + "交通灯数: " + getTrafficNumber(mAMapNaviPath));
                routeInfo2.setVisibility(View.VISIBLE);
                routeDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 启动模拟导航
//                        mAMapNavi.startNavi(NaviType.EMULATOR);
                        // 启动导航
                        mAMapNavi.startNavi(NaviType.GPS);

                        // 为AMapNaviView添加补间动画
                        addAnimation(mAMapNaviView, View.VISIBLE, 1);
                        dragfABLocation.hide();
                        dragfABUser.hide();
                    }
                });
            }

            @Override
            public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {
                dissmissProgressDialog();
                mAMap.clear(true);// 清理地图上的所有覆盖物
                setfromandtoMarker(new LatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()),
                        new LatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude()));

                routeInfo.setText(myMarkOptiopns.getSnippet());
                // 设置routeInfo图标
                Drawable drawable = getResources().getDrawable(R.drawable.end);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                routeInfo.setCompoundDrawables(drawable, null, null, null);

                routeInfo2.setText("         " + "Oh No! 出错啦! (" + aMapCalcRouteResult.getErrorDescription() + ")");
//                Log.e("onCalculateRouteFailure", "errorCode = " + aMapCalcRouteResult.getErrorCode());
                routeDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                mTtsManager.stopSpeaking();
                mAMapNavi.stopSpeak();
            }

            @Override
            public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

            }

            @Override
            public void onGpsSignalWeak(boolean b) {

            }
        };

        mAMapNavi.addAMapNaviListener(mapNaviListener);
        // 设置使用内部语音播报
        // sUseInnerVoice - 是否使用内部语音播报， 默认为false
        // isCallBackText - isUseInnerVoice设置为true以后，AMapNaviListener.onGetNavigationText(int, java.lang.String)接口会继续返回文字，默认为false
//        mAMapNavi.setUseInnerVoice(true, true);
//        mAMapNavi.setUseInnerVoice(true); // 旧版本


        // 使用科大讯飞语音引擎播报语音(某些系统版本较低的设备可能不支持高德的内部语音，此时需要自定义一个语音播报引擎)
        // 实例化语音引擎
        mTtsManager = TTSController.getInstance(mContext);
        mTtsManager.init();
        // 添加导航监听
        mAMapNavi.addAMapNaviListener(mTtsManager);
//        mAMapNavi.setUseInnerVoice(false, false);
        mAMapNavi.setUseInnerVoice(false); // 旧版本


        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
//                Log.e("onCameraChange", cameraPosition.toString());

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

                cameraCenterPosition = cameraPosition.target;
//                Log.e("onCameraChangeFinish", cameraPosition.toString());
            }
        });

        mainActivityViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * 添加自定义marker
     */
    private void add_myMarker(MarkerOptions mMarkerOptions) {
        TextView textView = new TextView(mContext);
        textView.setText(mMarkerOptions.getTitle());
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(R.mipmap.custom_info_bubble);

        myMarkOptiopns.icon(BitmapDescriptorFactory.fromView(textView));
//        mMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//        mAMap.addMarker(mMarkerOptions).showInfoWindow();
        mAMap.addMarker(mMarkerOptions);
    }

    /**
     * 显示离线POI点信息
     */
    @SuppressLint("SetTextI18n")
    private void show_mPoiDetail_OffLine() {
        mPoiName.setText(myMarkOptiopns.getTitle());
        mPoiAddress.setText("坐标 ( 经度, 纬度 ): ");
        mPoiInfo.setText(myMarkOptiopns.getPosition().longitude + ", " + myMarkOptiopns.getPosition().latitude);
        mPoiInfo.setVisibility(View.VISIBLE);
        mLinearLyout.setVisibility(View.GONE);
        mPoiDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtil.show(mContext, "show_mPoiDetail_OffLine: Offline PoiDetail Click event!");
            }
        });
    }

    /**
     * 地图初始化
     */
    private void init() {
        mAMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                setUp(mAMap, 4);

                mAMap.setTrafficEnabled(true);// 显示实时交通状况
                //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT,MAP_TYPE_BUS
//                mAMap.setMapType(AMap.MAP_TYPE_NORMAL); // 普通地图模式
//                mAMap.setMapType(AMap.MAP_TYPE_SATELLITE); // 卫星地图模式
//                mAMap.setMapType(AMap.MAP_TYPE_NIGHT); // 夜景模式
//                mAMap.setMapType(AMap.MAP_TYPE_NAVI); // 导航模式
//                mAMap.setMapType(AMap.MAP_TYPE_BUS); // 公交模式


                mAMap.moveCamera(CameraUpdateFactory.zoomTo(17)); //设置地图缩放级别
            }
        });
    }

    /**
     * 设置 AMap
     * */
    private void setUp(AMap amap, int myLocSty) {
        UiSettings uiSettings = amap.getUiSettings();
        amap.showIndoorMap(true); //设置是否显示室内地图，默认不显示
        uiSettings.setCompassEnabled(true); //设置地图默认的指南针是否显示
        uiSettings.setScaleControlsEnabled(true); //设置地图默认的比例尺是否显示
        uiSettings.setZoomControlsEnabled(false); //设置地图默认的缩放按钮不显示(默认显示)
//        //设置地图默认的缩放按钮位置右中(ZOOM_POSITION_RIGHT_CENTER), 右下(ZOOM_POSITION_RIGHT_BUTTOM)(默认)
//        uiSettings.setZoomPosition(ZOOM_POSITION_RIGHT_CENTER);
//        uiSettings.setMyLocationButtonEnabled(true); // 设置默认定位按钮是否显示,非必需设置
        amap.setMyLocationEnabled(true); // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        amap.setMapTextZIndex(2); // 设置地图底图文字标注的层级指数，默认为0，用来比较覆盖物（polyline、polygon、circle等）的zIndex

        setupMyLoctstyle(amap, myLocSty);
    }

    /**
     * 设置 AMap 样式
     * */
    private void setupMyLoctstyle(AMap amap, int mstyle) {
        MyLocationStyle myLocationStyle = new MyLocationStyle(); //初始化定位蓝点样式类

        switch (mstyle) {
            case 0:
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW); //只定位一次
                break;
            case 1:
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE); //定位一次，且将视角移动到地图中心点
                break;
            case 2:
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW); //连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动（1秒1次定位）
                break;
            case 3:
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE); //连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动（1秒1次定位）
                break;
            case 4:
//                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE); //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动（1秒1次定位）默认执行此种模式
                break;
            case 5:
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER); //连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动
                break;
            case 6:
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER); //连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动
                break;
            case 7:
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER); //连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动
                break;
            default:
                break;
        }

//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point)); // 自定义定位蓝点图标
        myLocationStyle.strokeColor(STROKE_COLOR); //自定义精度范围的圆形边框颜色
        myLocationStyle.strokeWidth(0); //自定义精度范围的圆形边框宽度
        myLocationStyle.radiusFillColor(FILL_COLOR); //设置圆形的填充颜色

        amap.setMyLocationStyle(myLocationStyle); //设置定位蓝点的Style
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPOIClick(Poi poi) {
        // 从地图上删除所有的覆盖物（marker，circle，polyline 等对象），但myLocationOverlay（内置定位覆盖物）除外
        mAMap.clear(true); // true|false 是|否保留myLocationOverlay(自定义定位小蓝点)

//        Log.e("onPOIClick", poi.getPoiId() + " | " + poi.getName() + " | " + poi.getCoordinate());
        myMarkOptiopns.position(poi.getCoordinate());
        myMarkOptiopns.title(poi.getName());
        myMarkOptiopns.snippet(null);

        mPoiSearch.searchPOIIdAsyn(poi.getPoiId());// POI异步搜索

        mSearchEvent = false;
    }


    /**
     * POI Marker 被点击时的逻辑
     * */
    private void poiMarkerClick(Marker marker) {
        if ((marker.getTitle() != null) && (marker.getPosition() != null)) {
            if (marker.getSnippet() != null) {
                mAMap.clear(true); // true|false 是|否保留myLocationOverlay(自定义定位小蓝点)
                mPoiDetail.setVisibility(View.GONE);

                // 添加补间动画
                addAnimation(routemap_header, View.VISIBLE, 0);

//                routeInfo.setText(marker.getSnippet());
                routeInfo.setText(myMarkOptiopns.getSnippet());
//                routeInfo.setTextColor(Color.argb(255, 85, 102, 184));
                routeInfo2.setText("");

                // 添加补间动画
                addAnimation(routeDetail, View.VISIBLE, 0);

                routeDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        ToastUtil.show(mContext, "onMarkerClick: routeDetail init");
                    }
                });

                busResult.setVisibility(View.VISIBLE);
                busResultList.setVisibility(View.GONE);
                busSegmentLis.setVisibility(View.GONE);

                mAMap.setTouchPoiEnable(false);
                mAMap.removeOnMapLongClickListener(this);
                mAMap.removeOnMapClickListener(this);
                mAMap.removeOnMarkerClickListener(this);

//                floatingActionButton.setVisibility(View.GONE);

                search_tool.setVisibility(View.GONE);

//                LatLng TLatLng = marker.getPosition();
                LatLng TLatLng = myMarkOptiopns.getPosition();
                mStartPoint = new LatLonPoint(myLocation.latitude, myLocation.longitude);
                mEndPoint = new LatLonPoint(TLatLng.latitude, TLatLng.longitude);
                setfromandtoMarker(myLocation, TLatLng);

                routeDetailClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 为 routemap_header 添加补间动画
                        addAnimation(routemap_header, View.GONE, 0);
                        routemap_header.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                search_tool.setVisibility(View.VISIBLE);
                                routemap_header.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        // 为 routeDetail 添加补间动画
                        addAnimation(routeDetail, View.GONE, 0);

                        if (busResult.getVisibility() != View.GONE) {
                            // 为 busResult 添加补间动画
                            addAnimation(busResult, View.GONE, 0);
                        }

                        mAMap.setTouchPoiEnable(true);
                        mAMap.addOnMapLongClickListener(MainActivity.this);
                        mAMap.addOnMapClickListener(MainActivity.this);
                        mAMap.addOnMarkerClickListener(MainActivity.this);

                        mAMap.clear(true);
                        mDrive.setImageResource(R.drawable.route_drive_normal);
                        mBus.setImageResource(R.drawable.route_bus_normal);
                        mWalk.setImageResource(R.drawable.route_walk_normal);
                        mRide.setImageResource(R.drawable.route_ride_normal);

                        mSearchEvent = false;

                        mAMap.setMapType(AMap.MAP_TYPE_NORMAL); // 普通地图模式
                    }
                });
            } else {
                ToastUtil.show(mContext,"路线规划需接入互联网，请检查您的网络设置");
            }

        } else {
            // 定位 Marker 被点击

            mAMap.clear(true); // true|false 是|否保留myLocationOverlay(自定义定位小蓝点)
            // 为mPoiDetail添加补间动画
            addAnimation(mPoiDetail, View.GONE, 0);
            search_tool.setVisibility(View.VISIBLE);
            mSearchEvent = false;

//            Log.e("poiMarkerClick", "定位 Marker 被点击");
        }
    }

    /**
     * 搜索栏结果显示的 Markers 被点击时的逻辑
     * */
    private void searchResultMarkerClick(Marker marker) {
        Marker detailMarker;
        if (marker.getObject() != null) {
            PoiItem mCurrentPoi = (PoiItem) marker.getObject();

            // 将之前被点击的marker置为原来的状态
            if (mlastMarker != null) {
                resetlastmarker();
            }

            mlastMarker = marker;
            detailMarker = marker;

            TextView textView = new TextView(getApplicationContext());
            textView.setText(marker.getTitle());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundResource(R.mipmap.custom_info_bubble);
            detailMarker.setIcon(BitmapDescriptorFactory.fromView(textView));

            // 设置终点Marker的参数
            myMarkOptiopns.title(mCurrentPoi.getTitle());
            if (mCurrentPoi.getSnippet().length() > 0) {

                // TypeCode - 类型编码 （POI类型请在网站“相关下载”处获取）
                if (mCurrentPoi.getTypeCode().equals("150700")) {
                    // 如果是公交车站
                    if (mCurrentPoi.getCityName() != null) {
                        myMarkOptiopns.snippet(mCurrentPoi.getCityName() + mCurrentPoi.getTitle());
                    } else {
                        myMarkOptiopns.snippet(mCurrentPoi.getSnippet());
                    }
                } else {
                    if (mCurrentPoi.getCityName() != null) {
                        myMarkOptiopns.snippet(mCurrentPoi.getCityName() + mCurrentPoi.getSnippet());
                    } else {
                        myMarkOptiopns.snippet(mCurrentPoi.getSnippet());
                    }
                }

            } else {
                if (mCurrentPoi.getDirection() != null && mCurrentPoi.getDirection().length() > 0) {
                    myMarkOptiopns.snippet(mCurrentPoi.getDirection() + mCurrentPoi.getTitle());
                } else if (mCurrentPoi.getCityName() != null && mCurrentPoi.getCityName().length() > 0) {
                    myMarkOptiopns.snippet(mCurrentPoi.getCityName() + mCurrentPoi.getTitle());
                } else {
                    myMarkOptiopns.snippet(mCurrentPoi.getTitle());
                }
            }
            myMarkOptiopns.position(marker.getPosition());

            mPoiName.setText(myMarkOptiopns.getTitle());
            mPoiAddress.setText(myMarkOptiopns.getSnippet());
            mPoiInfo.setText("营业时间：" + mCurrentPoi.getPoiExtension().getOpentime()
                    + "     评分：" + mCurrentPoi.getPoiExtension().getmRating()
            );
            mPoiInfo.setVisibility(View.VISIBLE);

            mLinearLyout.setVisibility(View.VISIBLE);
            mPoiDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.show(mContext, "onPoiItemSearched: Online PoiDetail Click event!");
                }
            });

            // 为mPoiDetail添加补间动画
            addAnimation(mPoiDetail, View.VISIBLE, 0);

            search_tool.setVisibility(View.GONE);
            busResult.setVisibility(View.VISIBLE);
            busResultList.setVisibility(View.GONE);
            busSegmentLis.setVisibility(View.GONE);

        } else {
            resetlastmarker();
            mlastMarker = null;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public boolean onMarkerClick(Marker marker) {
        // 设置routeInfo图标
        Drawable drawable = getResources().getDrawable(R.drawable.end);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        routeInfo.setCompoundDrawables(drawable, null, null, null);

        LatLng latLng = marker.getPosition();

        if (mSearchEvent) {
            if ((marker.getTitle() != null) && (marker.getPosition() != null)) {
                if (mlastMarker != null) {
                    if (mlastMarker.getId().equals(marker.getId())) {
                        poiMarkerClick(marker);
                    } else {
                        searchResultMarkerClick(marker);
                    }
                } else {
                    searchResultMarkerClick(marker);
                }
            } else {
                // 定位 Marker 被点击

                mAMap.clear(true); // true|false 是|否保留myLocationOverlay(自定义定位小蓝点)
                // 为mPoiDetail添加补间动画
                addAnimation(mPoiDetail, View.GONE, 0);
                search_tool.setVisibility(View.VISIBLE);
                mSearchEvent = false;

//                Log.e("onMarkerClick", "search 结果中的 定位 Marker 被点击");
            }

        } else {
            poiMarkerClick(marker);
        }

        if (latLng != null) {
            // 设置被点击的 Marker 地图居中
            setCameraCenter(latLng);
        }

        // true 返回true表示该点击事件已被处理，不再往下传递（如底图点击不会被触发），返回false则继续往下传递。
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
        mMapView.onDestroy();

//        mAMapNavi.removeAMapNaviListener(this);
//        mAMapNavi.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {

//        Log.e("onTouch", "motionEvent.getAction()= " + motionEvent.getAction());

        if(!mTouchevent) {
            setupMyLoctstyle(mAMap, 5);
            mTouchevent = true;
            floatBtClkFlag = false;
        }

    }


    @Override
    public void onMapClick(LatLng latLng) {
        mAMap.clear(true); // true|false 是|否保留myLocationOverlay(自定义定位小蓝点)
        // 为mPoiDetail添加补间动画
        addAnimation(mPoiDetail, View.GONE, 0);

        if (mPoiDetail.getAnimation() != null) {
            mPoiDetail.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    search_tool.setVisibility(View.VISIBLE);
                    mPoiDetail.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        mSearchEvent = false;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMapLongClick(LatLng latLng) {
        mAMap.clear(true); // true|false 是|否保留myLocationOverlay(自定义定位小蓝点)

        myMarkOptiopns.position(latLng);
        myMarkOptiopns.title("地图上的点");
        myMarkOptiopns.snippet(null);

        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 200, GeocodeSearch.AMAP);
        mGeocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求

        mSearchEvent = false;
    }


    /**
     * 初始化定位
     * */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认定位参数设置
     * */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。小于1000毫秒时，按照1000毫秒计算。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     * */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mCurrentCityName = location.getCity();
                mAMplocation = location;

                StringBuilder sb = new StringBuilder();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    sb.append("定位成功\n");
                    sb.append("定位类型: ").append(location.getLocationType()).append("\n");
                    sb.append("经    度    : ").append(location.getLongitude()).append("\n");
                    sb.append("纬    度    : ").append(location.getLatitude()).append("\n");
                    sb.append("海拔高度: ").append(location.getAltitude()).append("\n");
                    sb.append("精    度    : ").append(location.getAccuracy()).append("米\n");
                    sb.append("提供者    : ").append(location.getProvider()).append("\n");

                    sb.append("速    度    : ").append(location.getSpeed()).append("米/秒\n");
                    sb.append("角    度    : ").append(location.getBearing()).append("\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : ").append(location.getSatellites()).append("\n");
                    sb.append("国    家    : ").append(location.getCountry()).append("\n");
                    sb.append("省            : ").append(location.getProvince()).append("\n");
                    sb.append("市            : ").append(location.getCity()).append("\n");
                    sb.append("城市编码 : ").append(location.getCityCode()).append("\n");
                    sb.append("区            : ").append(location.getDistrict()).append("\n");
                    sb.append("区域 码   : ").append(location.getAdCode()).append("\n");
                    sb.append("地    址    : ").append(location.getAddress()).append("\n");
                    sb.append("地    址    : ").append(location.getDescription()).append("\n");
                    sb.append("兴趣点    : ").append(location.getPoiName()).append("\n");
                    //定位完成的时间
                    sb.append("定位时间: ").append(Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss")).append("\n");
                } else {
                    //定位失败
                    sb.append("定位失败\n");
                    sb.append("错误码:").append(location.getErrorCode()).append("\n");
                    sb.append("错误信息:").append(location.getErrorInfo()).append("\n");
                    sb.append("错误描述:").append(location.getLocationDetail()).append("\n");
                }
                sb.append("***定位质量报告***\n");
                sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启":"关闭\n");
                sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
                sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
                sb.append("****************\n");
                //定位之后的回调时间
                sb.append("回调时间: ").append(Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss")).append("\n");

                //解析定位结果，
                String result = sb.toString();
                textView.setText(result);

            } else {
                textView.setText("定位失败，loc is null");
            }
        }
    };


    /**
     * 获取GPS状态的字符串
     * */
    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode){
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    /**
     * 开始定位
     * */
    private void startLocation() {
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     * */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     * */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 设置 POI 相关的显示内容
     * */
    private void setPoiItemDisplayContent(final PoiItem mCurrentPoi) {
        if (mCurrentPoi != null) {
            myMarkOptiopns.title(mCurrentPoi.getTitle());
            if (mCurrentPoi.getSnippet().length() > 0) {

                // TypeCode - 类型编码 （POI类型请在网站“相关下载”处获取）
                if (mCurrentPoi.getTypeCode().equals("150700")) {
                    // 如果是公交车站
                    myMarkOptiopns.snippet(mCurrentPoi.getCityName() + mCurrentPoi.getTitle());
                } else {
                    myMarkOptiopns.snippet(mCurrentPoi.getCityName() + mCurrentPoi.getSnippet());
                }

            } else {
                if (mCurrentPoi.getDirection() != null && mCurrentPoi.getDirection().length() > 0) {
                    myMarkOptiopns.snippet(mCurrentPoi.getDirection() + mCurrentPoi.getTitle());
                } else if (mCurrentPoi.getCityName() != null && mCurrentPoi.getCityName().length() > 0) {
                    myMarkOptiopns.snippet(mCurrentPoi.getCityName() + mCurrentPoi.getTitle());
                } else {
                    myMarkOptiopns.snippet(mCurrentPoi.getTitle());
                }
            }
            add_myMarker(myMarkOptiopns);

            mPoiName.setText(myMarkOptiopns.getTitle());
            mPoiAddress.setText(myMarkOptiopns.getSnippet());
            mPoiInfo.setText("营业时间：" + mCurrentPoi.getPoiExtension().getOpentime()
                    + "     评分：" + mCurrentPoi.getPoiExtension().getmRating()
            );
            mPoiInfo.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化路径搜索
     * */
    private void routeSearchInit() {
        routemap_header = binding.routemapHeader;
        routemap_header.setVisibility(View.GONE);

        routeDetail = binding.routeDetail;
        routeDetail.setVisibility(View.GONE);

        routeDetailClose = binding.btRouteDetailClose;
        routeInfo = binding.routeInfo;
        routeInfo2 = binding.routeInfo2;
        mDrive = binding.routeDrive;
        mBus = binding.routeBus;
        mWalk = binding.routeWalk;
        mRide = binding.routeRide;


        busResult = binding.BusResultLl;
        busResult.setVisibility(View.GONE);
        busResultList = binding.busResultList;
        busResultList.setVisibility(View.GONE);
        busSegmentLis = binding.busSegmentLis;
        busSegmentLis.setVisibility(View.GONE);
        busSegmentLisFlag = false;

        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int errorCode) {

                if(!mTouchevent) {
                    setupMyLoctstyle(mAMap, 5);
                    mTouchevent = true;
                    floatBtClkFlag = false;
                }

                dissmissProgressDialog();
                mAMap.clear(true);// 清理地图上的所有覆盖物

                setfromandtoMarker(new LatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()),
                        new LatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude()));

                routeInfo.setText(myMarkOptiopns.getSnippet());
                // 设置routeInfo图标
                Drawable drawable = getResources().getDrawable(R.drawable.end);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                routeInfo.setCompoundDrawables(drawable, null, null, null);

                routeInfo2.setText("");

                routeDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        ToastUtil.show(mContext, "onBusRouteSearched: routeDetail click event");
                    }
                });

                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (busRouteResult != null && busRouteResult.getPaths() != null) {
                        if (busRouteResult.getPaths().size() > 0) {
//                            Log.e("BusRouteSearch", "error: busRouteResult.getPaths().size() = " + busRouteResult.getPaths().size());

                            BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(mContext, busRouteResult);
                            busResultList.setAdapter(mBusResultListAdapter);

                            // 添加补间动画
                            addAnimation(busResultList, View.VISIBLE, 1);
//                            busResultList.setVisibility(View.VISIBLE);

                            routeInfo2.setText("         打车约 " + busRouteResult.getTaxiCost() + " 元");

                            busResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                                    ToastUtil.show(mContext, "点击项目position = " + position);
                                    mAMap.clear(true);// 清理地图上的所有覆盖物
                                    final BusPath buspath = busRouteResult.getPaths().get(position);

                                    BusRouteOverlay busrouteOverlay = new BusRouteOverlay(
                                            mContext, mAMap, buspath,
                                            busRouteResult.getStartPos(),
                                            busRouteResult.getTargetPos());
                                    busrouteOverlay.removeFromMap();
                                    busrouteOverlay.addToMap();
                                    busrouteOverlay.zoomToSpan();

                                    routeInfo.setText(AMapUtil.getBusPathTitle(buspath));
                                    routeInfo2.setText("         " + AMapUtil.getBusPathDes(buspath));

                                    routeDetail.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (busSegmentLisFlag) {
                                                busSegmentLisFlag = false;
                                                // 添加补间动画
//                                                addAnimation(busSegmentLis, View.GONE, 0);
                                                busSegmentLis.setVisibility(View.GONE);

                                            } else {
                                                busSegmentLisFlag = true;
                                                BusSegmentListAdapter busSegmentListAdapter = new BusSegmentListAdapter(
                                                        mContext, buspath.getSteps());
                                                busSegmentLis.setAdapter(busSegmentListAdapter);
                                                // 添加补间动画
                                                addAnimation(busSegmentLis, View.VISIBLE, 1);
//                                                busSegmentLis.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });

                                    busResult.setVisibility(View.VISIBLE);
                                    // 添加补间动画
//                                    addAnimation(busResultList, View.GONE, 0);
                                    busResultList.setVisibility(View.GONE);
                                    busSegmentLis.setVisibility(View.GONE);
                                }

                            });

                        } else {
                            ToastUtil.show(mContext, R.string.no_result);

                            busResult.setVisibility(View.VISIBLE);
                            busResultList.setVisibility(View.GONE);
                            busSegmentLis.setVisibility(View.GONE);

                            routeInfo2.setText("         " + "没搜索到相关的公交路线，建议更换出行方式");
                        }
                    } else {
                        ToastUtil.show(mContext, R.string.no_result);

                        busResult.setVisibility(View.VISIBLE);
                        busResultList.setVisibility(View.GONE);
                        busSegmentLis.setVisibility(View.GONE);

                        routeInfo2.setText("         " + "没搜索到相关的公交路线，建议更换出行方式");
                    }
                } else {
                    ToastUtil.showerror(mContext, errorCode);

                    busResult.setVisibility(View.VISIBLE);
                    busResultList.setVisibility(View.GONE);
                    busSegmentLis.setVisibility(View.GONE);

                    routeInfo2.setText("         " + "Oh No! 出错啦! (" + errorCode + ")");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {
                dissmissProgressDialog();
                mAMap.clear(true);// 清理地图上的所有覆盖物

                setfromandtoMarker(new LatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()),
                        new LatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude()));

                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                        if (driveRouteResult.getPaths().size() > 0) {
                            final DrivePath drivePath = driveRouteResult.getPaths().get(0);
                            if(drivePath == null) {
                                return;
                            }

                            routeDetail.setVisibility(View.VISIBLE);
                            int dis = (int) drivePath.getDistance();
                            int dur = (int) drivePath.getDuration();
//                            int taxiCost = (int) driveRouteResult.getTaxiCost();
                            String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                            routeInfo.setText(myMarkOptiopns.getSnippet());
//                            routeInfo2.setText("驾车耗时：约" + des + " | 打车约" + taxiCost + "元");
                            routeInfo2.setText("         驾车耗时：约" + des);

                            routeDetail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        } else {
                            ToastUtil.show(mContext, R.string.no_result);
                            routeInfo2.setText("         " + "没搜索到相关的驾车路线，建议更换出行方式");
                        }
                    } else {
                        ToastUtil.show(mContext, R.string.no_result);
                        routeInfo2.setText("         " + "没搜索到相关的驾车路线，建议更换出行方式");
                    }
                } else {
                    ToastUtil.showerror(mContext, errorCode);
                    routeInfo2.setText("         " + "Oh No! 出错啦! (" + errorCode + ")");
                    routeDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int errorCode) {
                dissmissProgressDialog();
                mAMap.clear(true);// 清理地图上的所有覆盖物

                setfromandtoMarker(new LatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()),
                        new LatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude()));

                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (walkRouteResult != null && walkRouteResult.getPaths() != null) {
                        if (walkRouteResult.getPaths().size() > 0) {
                            final WalkPath walkPath = walkRouteResult.getPaths().get(0);
                            if(walkPath == null) {
                                return;
                            }

                            routeDetail.setVisibility(View.VISIBLE);
                            int dis = (int) walkPath.getDistance();
                            int dur = (int) walkPath.getDuration();
                            String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                            routeInfo.setText(myMarkOptiopns.getSnippet());
                            routeInfo2.setText("         步行耗时：约" + des);

                            routeDetail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        } else {
                            ToastUtil.show(mContext, R.string.no_result);
                            routeInfo2.setText("         " + "没搜索到相关的步行路线，建议更换出行方式");
                        }
                    } else {
                        ToastUtil.show(mContext, R.string.no_result);
                        routeInfo2.setText("         " + "没搜索到相关的步行路线，建议更换出行方式");
                    }
                } else {
                    ToastUtil.showerror(mContext, errorCode);
                    routeInfo2.setText("         " + "Oh No! 出错啦! (" + errorCode + ")");
                    routeDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int errorCode) {
                dissmissProgressDialog();
                mAMap.clear(true);// 清理地图上的所有覆盖物

                setfromandtoMarker(new LatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()),
                        new LatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude()));

                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (rideRouteResult != null && rideRouteResult.getPaths() != null) {
                        if (rideRouteResult.getPaths().size() > 0) {
                            final RidePath ridePath = rideRouteResult.getPaths().get(0);
                            if(ridePath == null) {
                                return;
                            }

                            routeDetail.setVisibility(View.VISIBLE);
                            int dis = (int) ridePath.getDistance();
                            int dur = (int) ridePath.getDuration();
                            String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                            routeInfo.setText(myMarkOptiopns.getSnippet());
                            routeInfo2.setText("         骑行耗时：约" + des);

                            routeDetail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        } else {
                            ToastUtil.show(mContext, R.string.no_result);
                            routeInfo2.setText("         " + "没搜索到相关的骑行路线，建议更换出行方式");
                        }
                    } else {
                        ToastUtil.show(mContext, R.string.no_result);
                        routeInfo2.setText("         " + "没搜索到相关的骑行路线，建议更换出行方式");
                    }
                } else {
                    ToastUtil.showerror(mContext, errorCode);
                    routeInfo2.setText("         " + "Oh No! 出错啦! (" + errorCode + ")");
                    routeDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
            }
        });

        mDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
                mDrive.setImageResource(R.drawable.route_drive_select);
                mBus.setImageResource(R.drawable.route_bus_normal);
                mWalk.setImageResource(R.drawable.route_walk_normal);
                mRide.setImageResource(R.drawable.route_ride_normal);

                busResult.setVisibility(View.VISIBLE);
                busResultList.setVisibility(View.GONE);
                busSegmentLis.setVisibility(View.GONE);
                busSegmentLisFlag = false;
            }
        });

        mBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
                mDrive.setImageResource(R.drawable.route_drive_normal);
                mBus.setImageResource(R.drawable.route_bus_select);
                mWalk.setImageResource(R.drawable.route_walk_normal);
                mRide.setImageResource(R.drawable.route_ride_normal);

                busResult.setVisibility(View.VISIBLE);
                busResultList.setVisibility(View.GONE);
                busSegmentLis.setVisibility(View.GONE);
                busSegmentLisFlag = false;

                mAMap.setMapType(AMap.MAP_TYPE_BUS); // 公交模式
            }
        });

        mWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
                mDrive.setImageResource(R.drawable.route_drive_normal);
                mBus.setImageResource(R.drawable.route_bus_normal);
                mWalk.setImageResource(R.drawable.route_walk_select);
                mRide.setImageResource(R.drawable.route_ride_normal);

                busResult.setVisibility(View.VISIBLE);
                busResultList.setVisibility(View.GONE);
                busSegmentLis.setVisibility(View.GONE);
                busSegmentLisFlag = false;
            }
        });

        mRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRouteResult(ROUTE_TYPE_RIDE, RouteSearch.RidingDefault);
                mDrive.setImageResource(R.drawable.route_drive_normal);
                mBus.setImageResource(R.drawable.route_bus_normal);
                mWalk.setImageResource(R.drawable.route_walk_normal);
                mRide.setImageResource(R.drawable.route_ride_select);

                busResult.setVisibility(View.VISIBLE);
                busResultList.setVisibility(View.GONE);
                busSegmentLis.setVisibility(View.GONE);
                busSegmentLisFlag = false;
            }
        });


    }

    /**
     * 设置起点终点Marker
     */
    private void setfromandtoMarker(LatLng mLcation, LatLng tLocation) {

        mAMap.addMarker(new MarkerOptions()
                        .position(mLcation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_start))
                        .title(mAMplocation.getPoiName())
                        .snippet(mAMplocation.getAddress())
//                .infoWindowEnable(false)
        );

        mAMap.addMarker(new MarkerOptions()
                        .position(tLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_end))
                        .title(myMarkOptiopns.getTitle())
                        .snippet(myMarkOptiopns.getSnippet())
//                .infoWindowEnable(false)
        );
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null) {
            progDialog = new ProgressDialog(this);
        }
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            Toast.makeText(mContext, "起点未设置", Toast.LENGTH_LONG).show();
            return;
        }

        if (mEndPoint == null) {
            Toast.makeText(mContext, "终点未设置", Toast.LENGTH_LONG).show();
        }

        showProgressDialog();

        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);

        if (routeType == ROUTE_TYPE_BUS) {
            // 公交路径规划

            // RouteSearch.BusRouteQuery 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode, mCurrentCityName, 0);
            mRouteSearch.calculateBusRouteAsyn(query); // 异步路径规划公交模式查询

        } else if (routeType == ROUTE_TYPE_DRIVE) {
            // 驾车路径规划
            List<NaviLatLng> startList = new ArrayList<NaviLatLng>(); // 起点坐标集合
            List<NaviLatLng> wayList = new ArrayList<NaviLatLng>(); // 途径点坐标集合
            List<NaviLatLng> endList = new ArrayList<NaviLatLng>(); // 终点坐标集合 [建议就一个终点]

            startList.add(new NaviLatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()));
            endList.add(new NaviLatLng(myMarkOptiopns.getPosition().latitude, myMarkOptiopns.getPosition().longitude));

            /**
             * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
             *
             * @congestion 躲避拥堵
             * @avoidhightspeed 不走高速
             * @cost 避免收费
             * @hightspeed 高速优先
             * @multipleroute 多路径
             *
             *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
             *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
             */
            int strategyFlag = 0;
            try {
                //再次强调，最后一个参数为true时代表多路径，否则代表单路径
                strategyFlag = mAMapNavi.strategyConvert(true, false, false, false, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strategyFlag >= 0) {
                mapNaviViewOptions.setTrafficBarEnabled(true); // 设置路况光柱条是否显示（只适用于驾车导航，需要联网）
                mapNaviViewOptions.setSettingMenuEnabled(true); // 设置菜单按钮是否在导航界面显示
                mAMapNaviView.setViewOptions(mapNaviViewOptions);

                // 设置模拟导航的速度。
                //驾车默认速度为60km/h,设置的模拟值区间应该在10-120之间.
                //步行默认速度为20km/h,设置的模拟值区间应该在10-30之间.
                //骑行默认速度为35km/h,设置的模拟值区间应该在10-50之间.
                mAMapNavi.setEmulatorNaviSpeed(60);

                mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
            }

        } else if (routeType == ROUTE_TYPE_WALK) {
            // 步行路径规划
            mapNaviViewOptions.setTrafficBarEnabled(false); // 设置路况光柱条是否显示（只适用于驾车导航，需要联网）
            mapNaviViewOptions.setSettingMenuEnabled(false); // 设置菜单按钮是否在导航界面显示
            mAMapNaviView.setViewOptions(mapNaviViewOptions);

            // 设置模拟导航的速度。
            //驾车默认速度为60km/h,设置的模拟值区间应该在10-120之间.
            //步行默认速度为20km/h,设置的模拟值区间应该在10-30之间.
            //骑行默认速度为35km/h,设置的模拟值区间应该在10-50之间.
            mAMapNavi.setEmulatorNaviSpeed(10);

            // 构造起点POI
            NaviPoi start = new NaviPoi("我的位置", new LatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()), "");
            // 构造终点POI
            NaviPoi end = new NaviPoi(myMarkOptiopns.getTitle(), myMarkOptiopns.getPosition(), "");

            // 进行步行单路径算路
//            mAMapNavi.calculateWalkRoute(start, end, TravelStrategy.SINGLE);
            // 进行步行多路径算路
            mAMapNavi.calculateWalkRoute(start, end, TravelStrategy.MULTIPLE);

        } else if (routeType == ROUTE_TYPE_RIDE) {
            // 异步路径规划骑行模式查询
            mapNaviViewOptions.setTrafficBarEnabled(false); // 设置路况光柱条是否显示（只适用于驾车导航，需要联网）
            mapNaviViewOptions.setSettingMenuEnabled(false); // 设置菜单按钮是否在导航界面显示
            mAMapNaviView.setViewOptions(mapNaviViewOptions);

            // 设置模拟导航的速度。
            //驾车默认速度为60km/h,设置的模拟值区间应该在10-120之间.
            //步行默认速度为20km/h,设置的模拟值区间应该在10-30之间.
            //骑行默认速度为35km/h,设置的模拟值区间应该在10-50之间.
            mAMapNavi.setEmulatorNaviSpeed(30);

            // 构造起点POI
            NaviPoi start = new NaviPoi("我的位置", new LatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude()), "");
            // 构造终点POI
            NaviPoi end = new NaviPoi(myMarkOptiopns.getTitle(), myMarkOptiopns.getPosition(), "");

            // 进行骑行单路径算路
//            mAMapNavi.calculateRideRoute(start, end, TravelStrategy.SINGLE);
            // 进行骑行多路径算路
            mAMapNavi.calculateRideRoute(start, end, TravelStrategy.MULTIPLE);
        }
    }

    /**
     * http post 请求
     * */
//    public void httpPostReq() throws IOException {
//        String strData = "{\"service\":\"s.home\"}"; // {"service":"s.home"}
//        byte[] requestBody = strData.getBytes(StandardCharsets.UTF_8);
//        URL url = new URL("http://192.168.2.125:8081/userservice");
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        httpURLConnection.setConnectTimeout(3000); //设置连接超时时间
//        httpURLConnection.setDoInput(true); //打开输入流，以便从服务器获取数据
//        httpURLConnection.setDoOutput(true); //打开输出流，以便向服务器提交数据
//        httpURLConnection.setRequestMethod("POST"); //设置以Post方式提交数据
//        httpURLConnection.setUseCaches(false); //使用Post方式不能使用缓存
//        httpURLConnection.setRequestProperty("Content-Type", "application/json"); //设置请求体的类型是文本类型
//        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(requestBody.length)); //设置请求体的长度
//        OutputStream outputStream = httpURLConnection.getOutputStream();
//        outputStream.write(requestBody);
//        outputStream.flush();
//
//        int response = httpURLConnection.getResponseCode(); //获得服务器的响应码
//        if (response == HttpURLConnection.HTTP_OK) {
//            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            StringBuilder strBuilder = new StringBuilder();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                strBuilder.append(line);
//            }
//            String result = strBuilder.toString();
//            Log.e("httppost", result);
//            inputStreamReader.close();
//            httpURLConnection.disconnect();
//        }
//    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        StringBuilder information = new StringBuilder("推荐城市\n");
        for (SuggestionCity suggestionCity : cities) {
            information.append("城市名称:")
                    .append(suggestionCity.getCityName())
                    .append("城市区号:")
                    .append(suggestionCity.getCityCode())
                    .append("城市编码:")
                    .append(suggestionCity.getAdCode())
                    .append("\n");
        }

        ToastUtil.show(mContext, information.toString());

    }


    /**
     * 将之前被点击的marker置为原来的状态
     * */
    private void resetlastmarker() {
        mlastMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mlastMarker = null;
    }

    /**
     * 在 Amap上绘制导航线路
     * */
    private void drawRoutes(int routeId, AMapNaviPath path) {
        mAMap.moveCamera(CameraUpdateFactory.changeTilt(0)); //设置地图倾斜度
        RouteOverLay routeOverLay = new RouteOverLay(mAMap, path, mContext);
        routeOverLay.setTrafficLine(true); // 设置实时路况显示状态
        routeOverLay.setTransparency(0.3f); // 设置该routeOverlay的透明度
        routeOverLay.setZindex(0);
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);
    }

    /**
     * 清除当前地图上算好的路线
     */
    private void clearRoute() {
        for (int i = 0; i < routeOverlays.size(); i++) {
            RouteOverLay routeOverlay = routeOverlays.valueAt(i);
            routeOverlay.removeFromMap();
        }
        routeOverlays.clear();

        if (paths != null) {
            paths.clear();
        }
    }

    /**
     * 路线的红绿灯数的计算
     * */
    public static int getTrafficNumber(AMapNaviPath path) {
        int trafficLightNumber = 0;
        if (path == null) {
            return trafficLightNumber;
        }
        List<AMapNaviStep> steps = path.getSteps();
        for (AMapNaviStep step : steps) {
            trafficLightNumber += step.getTrafficLightNumber();
        }
        return trafficLightNumber;
    }

    /**
     * 为frame添加补间动画
     *
     * @param view View
     * @param visibility View.VISIBLE
     * @param animationType int : 0:activity up or down | 1: activity left or right
     * */
    private void addAnimation(View view, int visibility, int animationType) {
        int mVisibility = view.getVisibility();

        if (visibility == mVisibility) {
            return;
        }

        if (mVisibility != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }

        switch (animationType) {
            case 0:
                if (visibility == View.VISIBLE) {
                    view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.activity_down_to_up_enter));
                } else {
                    view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.activity_up_to_down_exit));
                }
                break;

            case 1:
                if (visibility == View.VISIBLE) {
                    view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.activity_right_to_left_enter));
                } else {
                    view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.activity_left_to_right_exit));
                }
                break;
        }

        if (visibility != View.VISIBLE) {
            view.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    /**
     * 设置坐标视觉于地图居中
     *
     * @param latLng 需要居中的坐标点
     * */
    private void setCameraCenter(LatLng latLng) {

        if (!latLng.equals(cameraCenterPosition)) {
            mAMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

//            Log.e("setCameraCenter", latLng.toString());
        }

    }

    /**
     * 将距离的米数转换成相应的公里数
     * (采用四舍五入法进位，精确到0.1公里)
     *
     * @param meters 距离多少米
     * */
    private static String convertMeters2KM(int meters) {
        if(meters >= 1000) {
            double pathLength =  meters;
            pathLength /= 1000;
            BigDecimal bDPathLength = new BigDecimal(pathLength);
//            pathLength = bDPathLength.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
//            return pathLength + "公里";

            return bDPathLength.setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "公里";
        }
        else {
            return meters + "米";
        }
    }


//    static final CameraPosition myDefaultCenter = new CameraPosition.Builder()
//            .target(new LatLng(22.514984, 113.369066)).zoom(18).bearing(0).tilt(30).build();
//    /**
//     * 自定义设置地图的默认中心点，比如：使用当前定位、或者其他城市某个位置来作为中心点
//     * */
//    private void setDefaultCenter() {
//        AMapOptions aOptions = new AMapOptions();
////        aOptions.zoomGesturesEnabled(false);// 禁止通过手势缩放地图
////        aOptions.scrollGesturesEnabled(false);// 禁止通过手势移动地图
////        aOptions.tiltGesturesEnabled(false);// 禁止通过手势倾斜地图
//        aOptions.camera(myDefaultCenter);
//
//        mMapView = new MapView(this, aOptions);
//    }


    private long time =0;
    /**
     * 双击返回键退出应用
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - time > 1000)) {
//                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            } else {
                //如果添加了actiyity的生命周期控制类，可需要进行操作
                //如果有后台服务下载，就不能退出了。可以设置状态判断处理。
                finish();
                System.exit(0);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
