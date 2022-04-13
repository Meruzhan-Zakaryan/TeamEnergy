package com.teamenergy.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.teamenergy.R
import com.teamenergy.databinding.FragmentMapBinding
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.base.utils.openQRScanner
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
import com.yandex.runtime.image.ImageProvider
import org.koin.android.ext.android.inject


class MapFragment : Fragment(), UserLocationObjectListener, ClusterListener, ClusterTapListener, DrivingSession.DrivingRouteListener, MapObjectTapListener {

    private var binding: FragmentMapBinding? = null
    private var userLocationLayer: UserLocationLayer? = null
    private val viewModel by inject<BaseEnergyViewModel>()
    private var scannerContents: String? = null
    private var pointList = mutableListOf<Point>()
    private lateinit var map: Map
    private lateinit var mapView: MapView
    private lateinit var iconFactory: YandexIconFactory
    private var myLocationPoint = Point(0.0, 0.0)
    private val point1 = Point(40.2094360679, 44.529635332)
    private val point2 = Point(40.2094360679, 44.529635332)
    private val clausterCenters = mutableListOf(point1, point2)
    private lateinit var myLocation: Location
    private var drivingSession: DrivingSession? = null
    private var drivingRouter: DrivingRouter? = null
    private var mapObjects: MapObjectCollection? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  viewModel.getAllChargers(JsonObject().apply { addProperty("type", "all") })
        MapKitFactory.setApiKey("aee2cba6-f718-44e5-9f7c-f07540bd2bd0")
        MapKitFactory.initialize(requireActivity())
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
        map.mapObjects.addPlacemark(Point(40.2094360679, 44.529635332), iconFactory.clusterImageProvider(1)).addTapListener(this)
        map.mapObjects.addPlacemark(Point(40.2094360679, 44.529635332), iconFactory.clusterImageProvider(1)).addTapListener(this)
        map.mapObjects.addPlacemark(point1)
        map.mapObjects.addPlacemark(point2)
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        mapObjects = binding?.map?.map?.mapObjects?.addCollection()
        submitRequest()
        val mapKit = MapKitFactory.getInstance()
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
        userLocationLayer = binding?.map?.mapWindow?.let { mapKit.createUserLocationLayer(it) }
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

    }

    private fun setupListeners() {
        binding?.moveMyLocationButton?.setOnClickListener {
            val mapKit = MapKitFactory.getInstance()
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
        }

    }

    private fun observeLiveData() {

        viewModel.startTransactionLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Charger connected", Toast.LENGTH_SHORT).show()
        }
        viewModel.getAllChargersErrorLiveData.observe(viewLifecycleOwner) { error ->
            error ?: return@observe
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            viewModel.resetGetAllChargersErrorLivedata()
        }
        viewModel.getAllChargersLiveData.observe(viewLifecycleOwner) { chargerDto ->
            chargerDto?.data ?: return@observe
            val list = chargerDto.data
            for (connector in list) {
                connector.latitude ?: return@observe
                connector.longitude ?: return@observe
                pointList.add(Point(connector.latitude, connector.longitude))
            }
            map.mapObjects.addListener(object :MapObjectCollectionListener{
                override fun onMapObjectAdded(p0: MapObject) {

                }

                override fun onMapObjectRemoved(p0: MapObject) {

                }

                override fun onMapObjectUpdated(p0: MapObject) {
                    map.mapObjects.addPlacemark(pointList[0], ImageProvider.fromResource(requireContext(), R.drawable.ic_my_location))
                    map.mapObjects.addPlacemark(pointList[1], ImageProvider.fromResource(requireContext(), R.drawable.ic_my_location))
                }

            })
        }
        }

    override fun onClusterAdded(p0: Cluster) {
        TODO("Not yet implemented")
    }

    override fun onClusterTap(p0: Cluster): Boolean {
        TODO("Not yet implemented")
    }

    override fun onDrivingRoutes(p0: MutableList<DrivingRoute>) {
        mapObjects!!.addPolyline(p0[0].geometry)
//        for (route in p0) {
//        }
    }

    override fun onDrivingRoutesError(p0: Error) {
        Toast.makeText(requireContext(), p0.isValid.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
        p0.userData = point1
        submitRequest()
        return true
    }

    private fun submitRequest() {
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
                point1,
                RequestPointType.WAYPOINT,
                null
            )
        )
        drivingSession = drivingRouter?.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this)
    }
}