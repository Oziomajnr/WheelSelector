package ogbe.ozioma.com.wheelselectorsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ogbe.ozioma.com.wheelselector.WheelSelectorItem

class MainActivity : AppCompatActivity() {
    private val items: List<WheelSelectorItem> = listOf(WheelSelectorItem(0.5f, true),
        WheelSelectorItem(0.6f),
        WheelSelectorItem(0.7f),
        WheelSelectorItem(0.8f),
        WheelSelectorItem(0.9f),
        WheelSelectorItem(1.0f, true),
        WheelSelectorItem(1.1f),
        WheelSelectorItem(1.2f),
        WheelSelectorItem(1.3f),
        WheelSelectorItem(1.4f),
        WheelSelectorItem(1.5f, true),
        WheelSelectorItem(1.6f),
        WheelSelectorItem(1.7f),
        WheelSelectorItem(1.8f),
        WheelSelectorItem(1.9f),
        WheelSelectorItem(2.0f, true))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
