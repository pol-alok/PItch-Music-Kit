package com.yurisuzuki.ar;

import com.yurisuzuki.CameraActivity;

public class PianoMarker extends Marker {
	void checkPlaySound(long now, CameraActivity activity) {
		if (isTracked()) {
			lastTrackedTime = now;
		} else {
			if (lastTrackedTime > 0 && (now - lastTrackedTime) < 1000) {
				lastTrackedTime = -1;
				lastPlayTime = now;
				activity.playSound(soundId);
			}
		}
	}
}
