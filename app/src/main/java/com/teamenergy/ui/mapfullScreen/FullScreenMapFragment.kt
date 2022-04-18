package com.teamenergy.ui.mapfullScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamenergy.R
import com.teamenergy.databinding.FragmentFullScreenMapBinding
import com.teamenergy.databinding.FragmentMapBinding
import com.teamenergy.proxy.domain.ChargerItem
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.chargerInfo.ChargerInfoFragmentArgs
import com.teamenergy.ui.home.HomeActivity
import com.teamenergy.ui.home.YandexIconFactory
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
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import org.koin.android.ext.android.inject

class FullScreenMapFragment : Fragment(), UserLocationObjectListener, ClusterListener, ClusterTapListener, DrivingSession.DrivingRouteListener, MapObjectTapListener {

    private var binding: FragmentFullScreenMapBinding? = null
    private var userLocationLayer: UserLocationLayer? = null
    private var mapKit = MapKitFactory.getInstance()
    private val viewModel by inject<BaseEnergyViewModel>()
    private val args by navArgs<FullScreenMapFragmentArgs>()
    private lateinit var map: Map
    private lateinit var iconFactory: YandexIconFactory
    private var drivingSession: DrivingSession? = null
    private var drivingRouter: DrivingRouter? = null
    private var mapObjects: MapObjectCollection? = null
    private var myLocationPoint = Point(0.0, 0.0)
    private lateinit var myLocation: Location
    private lateinit var chargerItem: ChargerItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chargerItem = args.charger
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFullScreenMapBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@FullScreenMapFragment
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iconFactory = YandexIconFactory(requireActivity())
        map = binding?.map?.map!!
        map.isNightModeEnabled = false
        map.isRotateGesturesEnabled = false
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        mapObjects = binding?.map?.map?.mapObjects?.addCollection()
        userLocationLayer = binding?.map?.mapWindow?.let { MapKitFactory.getInstance().createUserLocationLayer(it) }
        userLocationLayer?.isVisible = true
        moveMyLocation()
        userLocationLayer?.setObjectListener(this)
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        (requireActivity() as HomeActivity).setBottomVisibility(false)
//        val placemark = chargerItem.point?.let { map.mapObjects.addPlacemark(it, iconFactory.clusterImageProvider(1)) }
//        chargerItem.point?.let { submitRequest(it) }
    }

    private fun setupListeners() {
        binding?.backToMapButton?.setOnClickListener {
            findNavController().navigate(R.id.nav_graph_map)
        }
    }

    private fun submitRequest(point: Point, myLocation: Point) {
        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()
        val requestPoints: ArrayList<RequestPoint> = ArrayList()
        requestPoints.add(
            RequestPoint(
                myLocation,
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

    private fun moveMyLocation() {
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
                chargerItem.point?.let {
                    map.mapObjects.addPlacemark(it, iconFactory.clusterImageProvider(1))
                    submitRequest(it, myLocationPoint)
                }
            }

        })
    }

    override fun onObjectAdded(p0: UserLocationView) {
        print(p0)
    }

    override fun onObjectRemoved(p0: UserLocationView) {

    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {

    }

    override fun onClusterAdded(p0: Cluster) {
    }

    override fun onClusterTap(p0: Cluster): Boolean {
        TODO("Not yet implemented")
    }

    override fun onDrivingRoutes(p0: MutableList<DrivingRoute>) {
        if (!p0.isNullOrEmpty()) {
            mapObjects?.let { it.addPolyline(p0[0].geometry) }
        }
    }

    override fun onDrivingRoutesError(p0: Error) {
        TODO("Not yet implemented")
    }

    override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
        TODO("Not yet implemented")
    }
}