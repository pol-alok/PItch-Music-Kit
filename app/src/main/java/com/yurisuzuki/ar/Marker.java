package com.yurisuzuki.ar;

import android.content.Context;
import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.camera.CameraRotationInfo;

import javax.microedition.khronos.opengles.GL10;

public class Marker {
	protected int markerId;
	protected int soundId;
	protected long lastTrackedTime = -1L;
	protected long lastPlayTime = -1L;

	protected float[] cachedMarkerMatrix = null;
	protected boolean markerMatrixCached;

	protected boolean suppressMarkerPlaneWhenActionShown = false;

	protected Plane markerPlane = new Plane(64.0f);

	protected Plane actionPlane = new Plane(64.0f * 1.3f, 1.0f);

	protected float adjustedMarkerMatrix[] = new float[16];

	Marker() {
	}

	boolean init(String markerParam, int soundId) {
		markerId = ARToolKit.getInstance().addMarker(markerParam);
		this.soundId = soundId;
		return markerId >= 0;
	}

	boolean loadMarkerTexture(GL10 gl, Context context, String textureAssetPath) {
		return markerPlane.loadGLTexture(gl, context, textureAssetPath);
	}

	boolean loadActionTexture(GL10 gl, Context context, String textureAssetPath) {
		return actionPlane.loadGLTexture(gl, context, textureAssetPath);
	}

	protected boolean isTracked() {
		ARToolKit ar = ARToolKit.getInstance();
		return ar.queryMarkerVisible(markerId);
	}

	protected void cacheMarkerMatrix(float markerMatrix[]) {
		if (cachedMarkerMatrix == null || cachedMarkerMatrix.length != markerMatrix.length) {
			cachedMarkerMatrix = new float[markerMatrix.length];
		}
		System.arraycopy(markerMatrix, 0, cachedMarkerMatrix, 0, markerMatrix.length);
		markerMatrixCached = true;
	}

	protected void adjustMarkerMatrix(float[] matrix, float[] targetMatrix, CameraRotationInfo cameraRotationInfo) {
		System.arraycopy(matrix, 0, targetMatrix, 0, 16);

		if (cameraRotationInfo.rotation == 180) {
			targetMatrix[0] = -targetMatrix[0];
			targetMatrix[4] = -targetMatrix[4];
			targetMatrix[8] = -targetMatrix[8];
			targetMatrix[12] = -targetMatrix[12];

			if (!cameraRotationInfo.mirror) {
				targetMatrix[1] = -targetMatrix[1];
				targetMatrix[5] = -targetMatrix[5];
				targetMatrix[9] = -targetMatrix[9];
				targetMatrix[13] = -targetMatrix[13];
			}
		} else {

			if (cameraRotationInfo.mirror) {
				targetMatrix[1] = -targetMatrix[1];
				targetMatrix[5] = -targetMatrix[5];
				targetMatrix[9] = -targetMatrix[9];
				targetMatrix[13] = -targetMatrix[13];
			}
		}
	}

	/**
	 * @return whether action plane was drawn or not.
	 */
	boolean draw(GL10 gl, long now, CameraRotationInfo cameraRotationInfo) {
		boolean actionPlaneDrawn = false;

		if (lastPlayTime > 0) {
			if (now - lastPlayTime < 200 & markerMatrixCached) {
				if (actionPlane.hasTexture()) {
					gl.glLoadMatrixf(cachedMarkerMatrix, 0);
					actionPlane.draw(gl);

					actionPlaneDrawn = true;
				}
			} else {
				lastPlayTime = -1L;
			}
		}

		if (!isTracked()) {
			return actionPlaneDrawn;
		}

		float markerMatrix[] = ARToolKit.getInstance().queryMarkerTransformation(markerId);
		if (markerMatrix == null) {
			return actionPlaneDrawn;
		}

		adjustMarkerMatrix(markerMatrix, adjustedMarkerMatrix, cameraRotationInfo);

		cacheMarkerMatrix(adjustedMarkerMatrix);

		if (markerPlane.hasTexture()) {
			if( !actionPlaneDrawn || !suppressMarkerPlaneWhenActionShown ) {
				gl.glLoadMatrixf(adjustedMarkerMatrix, 0);
				markerPlane.draw(gl);
			}
		}

		return actionPlaneDrawn;
	}
}
