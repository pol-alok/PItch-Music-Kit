package com.yurisuzuki.ar;

import android.util.Log;
import com.yurisuzuki.CameraActivity;
import org.artoolkit.ar.base.camera.CameraRotationInfo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class PianoRenderer extends InstrumentsRenderer {
	private static final String TAG = "PianoRenderer";
	private CameraActivity activity;

	private static final String[] markerParams = {
			"single;Data/Do.pat;64",
			"single;Data/Re.pat;64",
			"single;Data/Mi.pat;64",
			"single;Data/Fa.pat;64",
			"single;Data/So.pat;64",
			"single;Data/La.pat;64",
			"single;Data/Ti.pat;64",
			"single;Data/Do-.pat;64",
	};

	private static final String[] markerTexturePaths = {
			"Texture/Do.png",
			"Texture/Re.png",
			"Texture/Mi.png",
			"Texture/Fa.png",
			"Texture/So.png",
			"Texture/La.png",
			"Texture/Si.png",
			"Texture/Do-.png",
	};

	private static final String[] actionTexturePaths = {
			"Texture/Action_purple.png",
			"Texture/Action_blue.png",
			"Texture/Action_green.png",
			"Texture/Action_yellow.png",
			"Texture/Action_orange.png",
			"Texture/Action_red.png",
			"Texture/Action_brown.png",
			"Texture/Action_black.png",
	};

	private PianoMarker[] markers = new PianoMarker[markerParams.length];

	public PianoRenderer(CameraActivity activity) {
		this.activity = activity;

		for (int i = 0; i < markers.length; ++i) {
			PianoMarker marker = new PianoMarker();
			markers[i] = marker;
		}
	}

	@Override
	public boolean configureARScene() {
		for (int i = 0; i < markers.length; ++i) {
			boolean ret = markers[i].init(markerParams[i], i);
			if (!ret) {
				Log.d(TAG, "marker load failed:" + markerParams[i]);
				return false;
			}
		}
		return true;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);

		for (int i = 0; i < markers.length; ++i) {
			boolean ret0 = markers[i].loadMarkerTexture(gl, activity, markerTexturePaths[i]);
			boolean ret1 = markers[i].loadActionTexture(gl, activity, actionTexturePaths[i]);
			if (!ret0) {
				Log.d(TAG, "marker texture failed:" + markerTexturePaths[i]);
			}
			if (!ret1) {
				Log.d(TAG, "action texture failed:" + actionTexturePaths[i]);
			}
		}

		// Enable Texture Mapping
		gl.glEnable(GL10.GL_TEXTURE_2D);

		gl.glAlphaFunc(GL10.GL_GEQUAL, 0.5f);
		gl.glEnable(GL10.GL_ALPHA_TEST);
	}

	@Override
	public void draw(GL10 gl) {
		long now = System.currentTimeMillis();

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		for (PianoMarker marker : markers) {
			marker.checkPlaySound(now, activity);
		}

		setProjectionMatrix(gl);

		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);

		gl.glMatrixMode(GL10.GL_MODELVIEW);

		//boolean usingFrontCamera = activity.isUsingFrontCamera();
		CameraRotationInfo cameraRotationInfo = activity.getCameraRotationInfo();

		for (Marker marker : markers) {
			marker.draw(gl, now, cameraRotationInfo);
		}
	}
}
