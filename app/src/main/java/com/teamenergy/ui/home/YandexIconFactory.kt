package com.teamenergy.ui.home

import android.content.Context
import android.graphics.*
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.teamenergy.R
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.Rect
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider

class YandexIconFactory(private val ctx: Context) {
    fun clusterImageProvider(clusterCount: Int): ImageProvider =
        TextImageProvider(clusterCount.toString())

    fun bubbledViewProvider(marker: MapMarker): StyledViewProvider =
        BubbledViewProvider(marker)

    abstract class StyledViewProvider {
        abstract fun getProvider(): ViewProvider
        abstract fun getIconStyle(): IconStyle
    }

    private inner class TextImageProvider(private val text: String) : ImageProvider() {
        private val clusterIcon: Bitmap =
            ContextCompat.getDrawable(ctx, R.drawable.ic_charger)!!.toBitmap()

        private val textPaint: Paint = Paint().apply {
            color = Color.WHITE
            textSize = 15 * ctx.resources.displayMetrics.density
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        override fun getId(): String {
            return "text_$text"
        }

        override fun getImage(): Bitmap {
            val finalIcon = Bitmap.createBitmap(
                clusterIcon.width,
                clusterIcon.height,
                clusterIcon.config
            )
            val iconCanvas = Canvas(finalIcon)
            iconCanvas.drawBitmap(clusterIcon, 0f, 0f, null)
            iconCanvas.drawText(
                text,
                0.5f * finalIcon.width,
                0.5f * finalIcon.height - (textPaint.descent() + textPaint.ascent()) / 2,
                textPaint
            )
            return finalIcon
        }
    }

    private inner class BubbledViewProvider(private val marker: MapMarker) : StyledViewProvider() {
        private val itemView: View = LayoutInflater.from(ctx)
            .inflate(R.layout.item_marker_pin, null, false)

        val pinIcon: Bitmap = AppCompatResources.getDrawable(
            ctx,
            if (marker.isClicked)
                R.drawable.ic_charger
            else R.drawable.ic_charger
        )!!.toBitmap()

        private val iconView = itemView.findViewById<ImageView>(R.id.markerIconView)
        private val indicatorView = itemView.findViewById<ImageView>(R.id.bubble_image)

        override fun getProvider() = ViewProvider(itemView)

        init {
            updateIconBackground(marker)
            updateInfoWindow(marker)
        }

        override fun getIconStyle(): IconStyle {
            // todo: some more optimal way of checking the bubble size rather than bitmaps
            val fullImage = buildBitmap()
            val pinOffsetFraction =
                (fullImage.width - pinIcon.width) / fullImage.width.toFloat()
            val pinHeightFraction = pinIcon.height / fullImage.height.toFloat()
            val tappableRect = Rect(
                PointF(pinOffsetFraction, 1f - pinHeightFraction),
                PointF(1f - pinOffsetFraction, 1f)
            )

            return IconStyle()
                .setFlat(true)
                .setTappableArea(tappableRect)
                .setAnchor(PointF(0.5f, 1f))
        }

        private fun updateIconBackground(marker: MapMarker) {
            iconView.setImageBitmap(pinIcon)
        }

        private fun updateInfoWindow(marker: MapMarker): View {
            val indicator = ContextCompat.getDrawable(ctx, R.drawable.ic_my_location)?.apply {
                setTint(
                    ResourcesCompat.getColor(
                        ctx.resources,
                        R.color.text_color,
                        ctx.resources.newTheme()
                    )
                )
            }
            indicatorView.setImageDrawable(indicator)

//            val cardTint = if (marker.isClicked) {
//                ContextCompat.getColorStateList(ctx, R.color.appPrimary)
//            } else ContextCompat.getColorStateList(ctx, R.color.surface)
//            val textColor = if (marker.isClicked)
//                ContextCompat.getColorStateList(ctx, R.color.contentOnColor)
//            else ContextCompat.getColorStateList(ctx, R.color.contentOnSurface)

//            markerCard.setCardBackgroundColor(cardTint)

            return itemView
        }

        private fun buildBitmap(): Bitmap {
            val specWidth = View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.AT_MOST)
            itemView.measure(specWidth, specWidth)
            val b = Bitmap.createBitmap(
                itemView.measuredWidth,
                itemView.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
            val c = Canvas(b)
            itemView.layout(itemView.left, itemView.top, itemView.right, itemView.bottom)
            itemView.draw(c)
            return b
        }
    }
}