package com.nordef.snapchatui

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.nordef.snapchatui.adapter.ViewPagerAdapter
import com.nordef.snapchatui.view.CameraPreview
import com.nordef.snapchatui.view.SnapTabView


class MainActivity : AppCompatActivity() {

    lateinit var view: View
    lateinit var viewPager: ViewPager
    lateinit var snapTabView: SnapTabView

    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init view elements
        view = findViewById(R.id.view)
        viewPager = findViewById(R.id.view_pager)
        snapTabView = findViewById(R.id.snap_tabs_view)

        //viewpager custom adapter
        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        //syn viewPager with snapTapView
        snapTabView.SetUpWithViewPager(viewPager)

        //set default lunch view
        viewPager.currentItem = 1

        //color
        val colorBlue = ContextCompat.getColor(this, R.color.colorBlue)
        val colorPurple = ContextCompat.getColor(this, R.color.colorPurple)

        //listener to viewpager scroll page
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (position == 0) {
                    view.setBackgroundColor(colorBlue)
                    //while paage scroll to screen 1 positionoffset go up, so alpha go down
                    view.alpha = (1 - positionOffset)

                } else if (position == 1) {
                    view.setBackgroundColor(colorPurple)
                    //default posotionOfSet is 0, while page scroll to 2, she go up, so aplha up
                    view.alpha = positionOffset
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageSelected(position: Int) {
            }
        })

        checkPermissions()

        //log value getNavigationBarHeight
        //close camera onstop
        //pause it on change fragemnt
    }

    private fun LaunchCameraPreview() {
        // Create an instance of Camera
        mCamera = getCameraInstance()
        if (mCamera == null) {
            Log.d("tage", "no camera found")
            Toast.makeText(this, "no camera found", Toast.LENGTH_SHORT).show()
            return
        }

        // Create our Preview view and set it as the content of our activity.
        mPreview = CameraPreview(this, mCamera!!)
        val preview = findViewById(R.id.camera_preview) as FrameLayout
        preview.addView(mPreview)
    }

    /** A safe way to get an instance of the Camera object.  */
    fun getCameraInstance(): Camera? {
        var c: Camera? = null
        try {
            c = Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
        }

        return c // returns null if camera is unavailable
    }

    override fun onPause() {
        super.onPause()
        mCamera?.stopPreview()
    }

    override fun onResume() {
        super.onResume()
        mCamera?.startPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCamera?.release()
        mCamera = null
    }

    val MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACCESS_CODE -> {
                if (!(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "not authorized to use camera, please launch app again and accespt permission", Toast.LENGTH_SHORT).show()
                } else {
                    LaunchCameraPreview()
                }
            }
        }
    }

    /**
     * checking  permissions at Runtime.
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun checkPermissions() {
        val requiredPermissions = arrayOf(Manifest.permission.CAMERA)
        val neededPermissions = ArrayList<String>()
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this,
                            permission) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission)
            } else
                LaunchCameraPreview()
            if (!neededPermissions.isEmpty()) {
                requestPermissions(neededPermissions.toTypedArray(),
                        MY_PERMISSIONS_REQUEST_ACCESS_CODE)
            }
        }
    }
}
