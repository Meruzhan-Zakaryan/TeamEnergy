package com.teamenergy.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.JsonObject
import com.teamenergy.NavGraphHomeDirections.Companion.actionGlobalChargerInfoFragment
import com.teamenergy.R
import com.teamenergy.databinding.FragmentMapBinding
import com.teamenergy.proxy.domain.ChargerItem
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.base.utils.openQRScanner
import com.teamenergy.ui.chargerInfo.ChargerInfoFragment.Companion.START_DIRECTION
import com.teamenergy.ui.selectCar.SelectCarFragmentArgs
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.*
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import org.koin.android.ext.android.inject


class MapFragment : Fragment(), UserLocationObjectListener, ClusterListener, ClusterTapListener, DrivingSession.DrivingRouteListener, MapObjectTapListener {

    private var binding: FragmentMapBinding? = null
    private var userLocationLayer: UserLocationLayer? = null
    private val viewModel by inject<BaseEnergyViewModel>()
    private val args by navArgs<MapFragmentArgs>()
    private var scannerContents: String? = null
    private lateinit var map: Map
    private lateinit var mapView: MapView
    private lateinit var iconFactory: YandexIconFactory
    private var myLocationPoint = Point(0.0, 0.0)
    private lateinit var myLocation: Location
    private var drivingSession: DrivingSession? = null
    private var drivingRouter: DrivingRouter? = null
    private var mapObjects: MapObjectCollection? = null
    private var lastSelectedPoint = Point(0.0, 0.0)
    private var chargerList: List<ChargerItem> = listOf()
    private lateinit var lastSelectedCharger: ChargerItem
    private var mapKit = MapKitFactory.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (args.canGetChargers){
            viewModel.getAllChargers(JsonObject().apply { addProperty("type", "all") })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@MapFragment
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
        observeLiveData()
        iconFactory = YandexIconFactory(requireActivity())
        map = binding?.map?.map!!
        map.isNightModeEnabled = false
        map.isRotateGesturesEnabled = false
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        moveMyLocation()
        mapObjects = binding?.map?.map?.mapObjects?.addCollection()
        userLocationLayer = binding?.map?.mapWindow?.let { MapKitFactory.getInstance().createUserLocationLayer(it) }
        userLocationLayer?.isVisible = true
        userLocationLayer?.setObjectListener(this)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding?.map?.onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
        binding?.map?.onStop()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {

    }

    override fun onObjectRemoved(p0: UserLocationView) {

    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {

    }

    private fun setupViews() {
        (requireActivity() as HomeActivity).setBottomVisibility(true)
    }

    private fun setupListeners() {
        binding?.infoButton?.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.layout_info, null)
            builder.setView(view)
            builder.setCanceledOnTouchOutside(true)
            builder.show()
        }
        binding?.moveMyLocationButton?.setOnClickListener {
            moveMyLocation()
        }
        binding?.filterButton?.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.actionGlobalFilterFragment())
        }

    }

    private fun observeLiveData() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ChargerItem?>(START_DIRECTION)?.observe(viewLifecycleOwner) { chargerData ->
            chargerData.point ?: return@observe
            lastSelectedCharger = chargerData
            map.mapObjects.clear()
            moveMyLocation()
            val placemark = map.mapObjects.addPlacemark(chargerData.point, iconFactory.clusterImageProvider(1))
            placemark.userData = chargerData
            placemark.addTapListener(this@MapFragment)
            submitRequest(chargerData.point)
        }

        viewModel.startTransactionLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Charger connected", Toast.LENGTH_SHORT).show()
        }
        viewModel.getAllChargersErrorLiveData.observe(viewLifecycleOwner) { error ->
            error ?: return@observe
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            viewModel.resetGetAllChargersErrorLivedata()
        }
        viewModel.getAllChargersLiveData.observe(viewLifecycleOwner) { chargerDto ->
            map.mapObjects.clear()
            chargerDto?.data ?: return@observe
            chargerList = chargerDto.data
            for (charger in chargerList) {
                charger.latitude ?: return@observe
                charger.longitude ?: return@observe
                charger.point ?: return@observe
                val placemark = map.mapObjects.addPlacemark(charger.point, iconFactory.clusterImageProvider(1))
                placemark.userData = charger
                placemark.addTapListener(this@MapFragment)
            }
        }
//            map.mapObjects.addListener(object : MapObjectCollectionListener {
//                override fun onMapObjectAdded(p0: MapObject) {
//
//                }
//
//                override fun onMapObjectRemoved(p0: MapObject) {
//
//                }
//
//                override fun onMapObjectUpdated(p0: MapObject) {
//
//                }
//
//            })
    }

    override fun onClusterAdded(p0: Cluster) {
        TODO("Not yet implemented")
    }

    override fun onClusterTap(p0: Cluster): Boolean {
        TODO("Not yet implemented")
    }

    override fun onDrivingRoutes(p0: MutableList<DrivingRoute>) {
        if (!p0.isNullOrEmpty()) {
            mapObjects?.let { it.addPolyline(p0[0].geometry) }
        }
//        for (route in p0) {
//        }
    }

    override fun onDrivingRoutesError(p0: Error) {
        Toast.makeText(requireContext(), p0.isValid.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
        //p0.userData = p1
        lastSelectedCharger = p0.userData as ChargerItem
        findNavController().navigate(MapFragmentDirections.actionGlobalChargerInfoFragment(lastSelectedCharger))
        //submitRequest()
        return true
    }

    private fun submitRequest(point: Point) {
        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()
        val requestPoints: ArrayList<RequestPoint> = ArrayList()
        requestPoints.add(
            RequestPoint(
                myLocationPoint,
                RequestPointType.WAYPOINT,
                null
            )
        )
        requestPoints.add(
            RequestPoint(
                point,
                RequestPointType.WAYPOINT,
                null
            )
        )
        drivingSession = drivingRouter?.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this)
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                moveMyLocation()
            } else {
                Log.i("Permission: ", "Denied")
//                findNavController().navigate(ScannerFragmentDirections.actionGlobalMapFragment())
                //setFragment(mapFragment)
            }
        }

    private fun moveMyLocation() {
        if (hasCameraPermission() == PermissionChecker.PERMISSION_GRANTED) {
            mapKit = MapKitFactory.getInstance()
            mapKit.createLocationManager().requestSingleUpdate(object : LocationListener {
                override fun onLocationUpdated(p0: Location) {
                    myLocationPoint = Point(p0.position.latitude, p0.position.longitude)
                    myLocation = p0
                    map.move(
                        CameraPosition(p0.position, 15f, 0.0f, 0.0f),
                        Animation(Animation.Type.SMOOTH, 0f),
                        null
                    )
                }

                override fun onLocationStatusUpdated(p0: LocationStatus) {

                }

            })
        }else{
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}