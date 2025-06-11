package com.example.fitcore.services

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.example.fitcore.R
import com.example.fitcore.TipoDaltonismo

class ColorFilterService : Service() {

    private lateinit var windowManager: WindowManager
    private var overlayView: View? = null // Nullable, as it can be removed
    private lateinit var params: WindowManager.LayoutParams

    companion object {
        const val ACTION_APPLY_FILTER = "com.example.fitcore.APPLY_FILTER"
        const val ACTION_REMOVE_FILTER = "com.example.fitcore.REMOVE_FILTER"
        const val EXTRA_FILTER_TYPE_ORDINAL = "com.example.fitcore.FILTER_TYPE_ORDINAL"
        private const val PREFS_NAME = "FilterPrefs"
        private const val PREF_CURRENT_FILTER_ORDINAL = "currentFilterOrdinal"
        const val OVERLAY_PERMISSION_REQUEST_CODE = 1235 // Use a unique code

        fun setFilter(context: Context, filterType: TipoDaltonismo) {
            val intent = Intent(context, ColorFilterService::class.java)
            if (filterType == TipoDaltonismo.NORMAL) {
                intent.action = ACTION_REMOVE_FILTER
            } else {
                intent.action = ACTION_APPLY_FILTER
                intent.putExtra(EXTRA_FILTER_TYPE_ORDINAL, filterType.ordinal)
            }
            context.startService(intent)
        }

        fun checkAndRequestOverlayPermission(activity: Activity): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(activity)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + activity.packageName)
                    )
                    activity.startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
                    return false
                }
            }
            return true
        }
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        overlayView = inflater.inflate(R.layout.fragment_filtro_cor, null)

        val layoutParamsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE // Consider TYPE_SYSTEM_ALERT for wider compatibility on older OS if TYPE_PHONE fails
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutParamsType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_APPLY_FILTER -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                        stopSelf()
                        return START_NOT_STICKY
                    }
                    val filterOrdinal = it.getIntExtra(EXTRA_FILTER_TYPE_ORDINAL, TipoDaltonismo.NORMAL.ordinal)
                    val filterType = TipoDaltonismo.values()[filterOrdinal]
                    applyFilterToOverlay(filterType)
                    saveFilterPreference(filterOrdinal)
                }
                ACTION_REMOVE_FILTER -> {
                    removeFilterFromOverlay()
                    saveFilterPreference(TipoDaltonismo.NORMAL.ordinal)
                }
            }
        } ?: run { // If intent is null (e.g., service restarted)
            val savedFilterOrdinal = getSavedFilterPreference()
            if (savedFilterOrdinal != TipoDaltonismo.NORMAL.ordinal) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
                    val filterType = TipoDaltonismo.values()[savedFilterOrdinal]
                    applyFilterToOverlay(filterType)
                } else {
                    stopSelf()
                }
            }
        }
        return START_STICKY
    }

    private fun applyFilterToOverlay(filterType: TipoDaltonismo) {
        overlayView?.let { view ->
            val rootOverlay = view.findViewById<FrameLayout>(R.id.filtroContainerRaiz)
            rootOverlay.setBackgroundColor(filterType.overlayColor)

            if (view.windowToken == null && filterType != TipoDaltonismo.NORMAL) {
                try {
                    windowManager.addView(view, params)
                } catch (e: Exception) {
                    e.printStackTrace() // Log error
                }
            } else if (view.windowToken != null && filterType == TipoDaltonismo.NORMAL) {
                removeFilterInternal()
            }
            // If view already added and filterType is not NORMAL, color is updated by setBackgroundColor
        }
    }

    private fun removeFilterFromOverlay() {
        removeFilterInternal()
    }

    private fun removeFilterInternal() {
        overlayView?.let {
            if (it.windowToken != null) {
                try {
                    windowManager.removeView(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun saveFilterPreference(filterOrdinal: Int) {
        val prefs: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(PREF_CURRENT_FILTER_ORDINAL, filterOrdinal).apply()
    }

    private fun getSavedFilterPreference(): Int {
        val prefs: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(PREF_CURRENT_FILTER_ORDINAL, TipoDaltonismo.NORMAL.ordinal)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeFilterInternal()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // Not using binding
    }
}