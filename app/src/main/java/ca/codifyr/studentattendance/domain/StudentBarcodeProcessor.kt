package ca.codifyr.studentattendance.domain

import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode

class StudentBarcodeProcessor internal constructor (private val mBarcodeField: TextView): Detector.Processor<Barcode> {
    override fun release() {
        mBarcodeField.text = ""
    }

    override fun receiveDetections(detector: Detector.Detections<Barcode>?) {
        val items = detector!!.detectedItems
        if (items.size() > 0) {
            // only set barcode text if it's not currently set
            if (mBarcodeField.text.isEmpty()) {
                Log.d("Processor", "Barcode detected")
                val item = items.valueAt(0)
                var barcode = item.rawValue
                //  Student card contains "A" character at either end for some reason?
                // only concerned with numeric portion
                val re = Regex("[A-Za-z]")
                barcode = re.replace(barcode, "")
                mBarcodeField.text = barcode
            }
        }
    }
}