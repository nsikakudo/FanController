package com.example.remotecontroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.example.remotecontroller.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var fanController: FanControllerView
    private lateinit var switchButton: SwitchCompat
    private lateinit var fanStateText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fanController = binding.fanRemoteView
        switchButton = binding.btnSwitch
        fanStateText = binding.fanStateText

        fanStateText.text = getString(
            R.string.fan_state,
            resources.getString(fanController.getFanSpeed().labelNumber)
        )

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            fanController.setTextMode(isChecked)
            updateFanStateText()
        }

        fanController.setOnClickListener {
            fanController.toggleFanSpeed()
            updateFanStateText()
        }
    }

    private fun updateFanStateText() {
        val fanSpeed = fanController.getFanSpeed()
        val label = if (switchButton.isChecked) {
            resources.getString(fanSpeed.labelWord)
        } else {
            resources.getString(fanSpeed.labelNumber)
        }
        fanStateText.text = getString(R.string.fan_state, label)
    }
}