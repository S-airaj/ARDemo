package com.example.myfirarapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ardemo.R
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.math.Position
import io.github.sceneview.node.Node

class MainActivity : AppCompatActivity() {

    private lateinit var sceneView: ARSceneView
    private lateinit var placeButton: Button
    private lateinit var infoText: TextView
    private var modelNode: ArModelNode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        sceneView = findViewById(R.id.sceneView)
        placeButton = findViewById(R.id.placeButton)
        infoText = findViewById(R.id.infoText)

        // Initially hide the place button
        placeButton.visibility = View.GONE

        // Set up AR and model loading
        setupAR()
    }

    private fun setupAR() {
        // Create a new model node
        modelNode = ArModelNode().apply {
            // Load the 3D model from assets (adjust file path as needed)
            loadModelGlbAsync(
                glbFileLocation = "models/testmodel.glb",  // Path to your GLB model in assets folder
                scaleToUnits = 1f,
                centerOrigin = Position(x = 0f, y = 0f, z = 0f)
            )

            // Set anchor change listener
            onAnchorChanged = { anchor ->
                // Show the place button if the model is not anchored
                placeButton.visibility = if (anchor == null) View.VISIBLE else View.GONE
            }
        }

        // Add the model node to the scene
        sceneView.addChild(modelNode!!)

        // Enable plane renderer to detect surfaces
        sceneView.planeRenderer.isVisible = true

        // Set a listener for the place button
        placeButton.setOnClickListener {
            placeObject()
        }
    }

    private fun placeObject() {
        modelNode?.let { node ->
            // If the node is not anchored, anchor it and hide the place button
            if (!node.isAnchored) {
                node.anchor()
                placeButton.visibility = View.GONE
                infoText.text = "Object placed!"
                Toast.makeText(this, "Object placed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            sceneView.resume()  // Resume AR session
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        sceneView.pause()  // Pause AR session
    }
}
