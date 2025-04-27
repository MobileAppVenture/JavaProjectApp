package com.example.mobileapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector.ObjectDetectorOptions;

import java.io.IOException;
import java.util.List;

public class ObjectDetectorHelper {
    private ObjectDetector objectDetector;

    public ObjectDetectorHelper(Context context) {
        try {
            ObjectDetectorOptions options =
                    ObjectDetectorOptions.builder()
                            .setMaxResults(5)
                            .setScoreThreshold(0.3f)
                            .build();

            objectDetector = ObjectDetector.createFromFileAndOptions(
                    context,
                    "efficientdet_lite0.tflite",
                    options
            );

        } catch (IOException e) {
            Log.e("ObjectDetectorHelper", "Error loading the model", e);
        }
    }

    public List<Detection> detect(Bitmap bitmap) {
        if (objectDetector == null) {
            Log.e("ObjectDetectorHelper", "The model has not been initialized");
            return null;
        }

        TensorImage image = TensorImage.fromBitmap(bitmap);
        return objectDetector.detect(image);
    }
}
