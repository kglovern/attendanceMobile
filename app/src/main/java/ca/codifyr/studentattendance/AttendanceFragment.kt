package ca.codifyr.studentattendance

import android.content.Context
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import ca.codifyr.studentattendance.domain.APIRequestFactory
import ca.codifyr.studentattendance.domain.StudentBarcodeProcessor
import ca.codifyr.studentattendance.domain.entities.Attend
import ca.codifyr.studentattendance.domain.repository.AttendRespository
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.fragment_attendance.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class AttendanceFragment : Fragment(), SurfaceHolder.Callback {
    private var mSurfaceHolder: SurfaceHolder? = null
    private var mSurfaceView: SurfaceView? = null
    private var mBarcodeDetector: BarcodeDetector? = null
    private var mCameraSource: CameraSource? = null
    private lateinit var mBarcodeValue: TextView
    private lateinit var mSubmitButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_attendance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Register barcode detector
        mBarcodeDetector = BarcodeDetector.Builder(view.context)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
        mCameraSource = CameraSource.Builder(view.context, mBarcodeDetector).setAutoFocusEnabled(true).build()
        // Handle UI elements we care about - listen to camera
        mSurfaceView = view.findViewById(R.id.barcodeCamera)
        mBarcodeValue = view.findViewById(R.id.barcodeValue)
        mSubmitButton = view.findViewById(R.id.btnCheckIn)
        mSurfaceHolder = mSurfaceView!!.holder
        mSurfaceHolder!!.addCallback(this)
        // Add listener for barcode detection
        mBarcodeDetector!!.setProcessor(StudentBarcodeProcessor(mBarcodeValue))

        // Add listener for reset button which does exactly what you'd think it does.
        val mResetButton = view.findViewById(R.id.btnReset) as Button
        mResetButton.setOnClickListener {
            resetFields()
        }

        // Add listener to text being changed.  This is terrible.  Don't do it this way
        // Yikes.
        // This would be better as a callback.
        mBarcodeValue.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                activity!!.runOnUiThread {
                    if (mBarcodeValue.text.isNotEmpty()) {
                        toast("Identified barcode")
                        mSubmitButton.isEnabled = true
                        mSubmitButton.isClickable = true
                        val request = APIRequestFactory.buildGetRequest("/students/UPC/${mBarcodeValue.text}")
                        doAsync {
                            val response = request.execute()
                            uiThread {
                                if (response.isSuccessful) {
                                    toast("Retrieved Student!")
                                    val jsonObject = JSONObject(response.body()!!.string())
                                    textStudentName.text = context!!.getString(
                                        R.string.text_full_name,
                                        jsonObject.getString("firstName"),
                                        jsonObject.getString("lastName"))
                                    textStudentNumber.text = context!!.getString(R.string.text_student_number,
                                        jsonObject.getString("userId"))
                                } else {
                                    toast("Unable to identify student - possibly not in system")
                                }
                            }
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // Add listener to send attendance
        mSubmitButton.setOnClickListener {
            val attendRepo = AttendRespository()
            val attend = Attend(mBarcodeValue.text.toString())
            doAsync {
                val successState = attendRepo.add(attend)
                uiThread {
                    if (successState) {
                        toast("Successfully checked in student!")
                        resetFields()
                    } else {
                        toast("Failed to check in student - try again")
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(): AttendanceFragment = AttendanceFragment()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mCameraSource!!.stop()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        try {
            mCameraSource!!.start(mSurfaceHolder)
        } catch (e: SecurityException) {
            Log.d("AF", "No permissions to start camera")
        }
    }

    fun resetFields() {
        mBarcodeValue.text = ""
        mSubmitButton.isEnabled = false
        mSubmitButton.isClickable = false
        textStudentNumber.text = ""
        textStudentName.text = ""
    }
}
