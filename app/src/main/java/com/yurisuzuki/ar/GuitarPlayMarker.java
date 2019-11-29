package com.yurisuzuki.ar;

import com.yurisuzuki.CameraActivity;
import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.camera.CameraRotationInfo;

import javax.microedition.khronos.opengles.GL10;

public class GuitarPlayMarker extends Marker {
	private long lastOutlintDrawnWithTracked = -1L;

	void checkPlaySound(long now, CameraActivity activity) {
		if (isTracked()) {
			lastTrackedTime = now;
		} else {
			if (lastTrackedTime > 0 && (now - lastTrackedTime) < 1000) {
				lastTrackedTime = -1;
				lastPlayTime = now;
				activity.playCurrentSound();
			}
		}
	}


	void drawOutline(GL10 gl, Plane outlinePlane, long now, CameraRotationInfo cameraRotationInfo) {
		if (isTracked()) {
			float markerMatrix[] = ARToolKit.getInstance().queryMarkerTransformation(markerId);
			if (markerMatrix == null) {
				return;
			}

			adjustMarkerMatrix(markerMatrix, adjustedMarkerMatrix, cameraRotationInfo);
			gl.glLoadMatrixf(adjustedMarkerMatrix, 0);

			outlinePlane.draw(gl);
			lastOutlintDrawnWithTracked = now;
		} else if( markerMatrixCached && (now - lastOutlintDrawnWithTracked) < 1000 ) {
			gl.glLoadMatrixf(cachedMarkerMatrix, 0);
			outlinePlane.draw(gl);
		}
	}
}
