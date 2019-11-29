package com.yurisuzuki.ar;

import com.yurisuzuki.geom.Matrix4f;
import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;

import javax.microedition.khronos.opengles.GL10;

public class InstrumentsRenderer extends ARRenderer {
	private Matrix4f projectionMatrix = new Matrix4f();

	protected void setProjectionMatrix(GL10 gl) {
		gl.glMatrixMode(GL10.GL_PROJECTION);
		projectionMatrix.set(ARToolKit.getInstance().getProjectionMatrix());
		gl.glLoadMatrixf(ARToolKit.getInstance().getProjectionMatrix(), 0);
	}

	Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
