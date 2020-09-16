package com.smile.vhaconsultancy.utilities

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.smile.vhaconsultancy.R
import java.lang.reflect.Field
import java.text.NumberFormat
import java.util.*

class Utils {
    companion object {

        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }

        fun pxToDp(px: Int): Int {
            return (px / Resources.getSystem().getDisplayMetrics().density) as Int
        }

        fun runAnimation(recyclerView: RecyclerView) {
            val controller =
                    AnimationUtils.loadLayoutAnimation(
                            recyclerView.context,
                            R.anim.layout_animation_left_to_right
                    )

            recyclerView.layoutAnimation = controller
            recyclerView.adapter?.notifyDataSetChanged()
            recyclerView.scheduleLayoutAnimation()
        }

        /**
         * Gets current locale.
         *
         * @param context the context
         * @return the current locale
         */
        /*  fun getCurrentLocale(context: Context): Locale? {
              var str_app_lang = SharedPref.getInstance(context).getSharedPref(context.getString(R.string.app_language))
              if (str_app_lang.isNullOrEmpty()) {
                  str_app_lang = "nl"
              }
              return Locale(str_app_lang)
          }*/

        /**
         * Gets string currency.
         *
         * @param context the context
         * @param amount  the amount
         * @return the string currency
         */
        fun getStringCurrency(
                context: Context?,
                amount: Double?
        ): String? {
            val format = NumberFormat.getCurrencyInstance(
                    Locale("nl")
            )
            format.currency = Currency.getInstance("EUR")
            format.minimumFractionDigits = 2
            return format.format(amount)
        }

        fun getStringNumber(
                context: Context?,
                amount: Double?
        ): String? {
            val format = NumberFormat.getNumberInstance(
                    //getCurrentLocale(context!!)
            )
            format.minimumFractionDigits = 2
            return format.format(amount)
        }

        fun getStringNumberOnePre(
                context: Context?,
                amount: Double?
        ): String? {
            val format = NumberFormat.getNumberInstance(
                    // getCurrentLocale(context!!)
            )
            format.minimumFractionDigits = 1
            return format.format(amount)
        }

        fun setCursorColor(view: EditText, @ColorInt color: Int) {
            try { // Get the cursor resource id
                var field: Field = TextView::class.java.getDeclaredField("mCursorDrawableRes")
                field.setAccessible(true)
                val drawableResId: Int = field.getInt(view)
                // Get the editor
                field = TextView::class.java.getDeclaredField("mEditor")
                field.setAccessible(true)
                val editor: Any = field.get(view)
                // Get the drawable and set a color filter
                val drawable = ContextCompat.getDrawable(view.context, drawableResId)
                drawable!!.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                val drawables = arrayOf<Drawable?>(drawable, drawable)
                // Set the drawables
                field = editor.javaClass.getDeclaredField("mCursorDrawable")
                field.setAccessible(true)
                field.set(editor, drawables)
            } catch (ignored: Exception) {
            }
        }

        fun lighten(color: Int, fraction: Double): Int {
            var red: Int = Color.red(color)
            var green: Int = Color.green(color)
            var blue: Int = Color.blue(color)
            red = lightenColor(red, fraction)
            green = lightenColor(green, fraction)
            blue = lightenColor(blue, fraction)
            val alpha: Int = Color.alpha(color)
            return Color.argb(alpha, red, green, blue)
        }

        private fun lightenColor(color: Int, fraction: Double): Int {
            return Math.min(color + color * fraction, 255.0).toInt()
        }

        fun getPressedColorSelector(normalColor: Int, pressedColor: Int): ColorStateList? {
            return ColorStateList(
                    arrayOf(
                            intArrayOf()
                    ), intArrayOf(
                    pressedColor
            )
            )
        }

        fun getColorDrawableFromColor(color: Int): ColorDrawable? {


            return ColorDrawable(color)
        }

        fun changeDrawableColor(
                drawableRes: Int,
                colorRes: Int,
                context: Context
        ): Drawable? { //Convert drawable res to bitmap
            val bitmap = BitmapFactory.decodeResource(context.resources, drawableRes)
            val resultBitmap = Bitmap.createBitmap(
                    bitmap, 0, 0,
                    bitmap.width - 1, bitmap.height - 1
            )
            val p = Paint()
            val canvas = Canvas(resultBitmap)
            canvas.drawBitmap(resultBitmap, 0f, 0f, p)
            //Create new drawable based on bitmap
            val drawable: Drawable = BitmapDrawable(context.resources, resultBitmap)
            drawable.colorFilter = PorterDuffColorFilter(
                    colorRes,
                    PorterDuff.Mode.MULTIPLY
            )
            return drawable
        }

        fun changeBitmapColor(sourceBitmap: Bitmap, color: Int): Bitmap {
            val resultBitmap = sourceBitmap.copy(sourceBitmap.config, true)
            val paint = Paint()
            val filter: ColorFilter = LightingColorFilter(color, 1)
            paint.setColorFilter(filter)
            val canvas = Canvas(resultBitmap)
            canvas.drawBitmap(resultBitmap, 0f, 0f, paint)
            return resultBitmap
        }
/*
        fun getThemeColor(context: Context): Int {
            return Color.parseColor(
                SharedPrefWine.getInstance(context).getSharedPref(
                    context.getString(
                        R.string.display_theme_color
                    )
                )
            )
        }*/

        fun setLocal(context: Context) {
            var str_app_lang = SharedPref.getInstance(context)?.getSharedPref(context.getString(R.string.app_language))
            if (str_app_lang.isNullOrEmpty()) {
                str_app_lang = "en"
                SharedPref.getInstance(context)?.putSharedPrefString(
                    context.getString(R.string.app_language),
                    str_app_lang
                )
            }
            val locale = Locale(
                    str_app_lang
            )
            Locale.setDefault(locale)
            val config: Configuration = context.resources.configuration
            config.setLocale(locale)
            context.resources.updateConfiguration(
                    config,
                    context.resources.displayMetrics
            )
        }
    }
}

