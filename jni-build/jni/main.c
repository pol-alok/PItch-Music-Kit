/*===============================================================================================
 PlaySound Example
 Copyright (c), Firelight Technologies Pty, Ltd 2004-2011.

 This example shows how to simply load and play multiple sounds.  This is about the simplest
 use of FMOD.
 This makes FMOD decode the into memory when it loads.  If the sounds are big and possibly take
 up a lot of ram, then it would be better to use the FMOD_CREATESTREAM flag so that it is
 streamed in realtime as it plays.
===============================================================================================*/

#include <jni.h>
#include <android/log.h>
#include <stdlib.h>
#include "fmod.h"
#include "fmod_errors.h"

#define MAX_NUM_SOUNDS 50

FMOD_SYSTEM  *gSystem  = 0;
FMOD_CHANNEL *gChannel = 0;
FMOD_DSP     *gDSPDistortion = 0;
FMOD_SOUND	 *gSound[MAX_NUM_SOUNDS];

int numOfSounds;
int isMonoPhonic;

#define CHECK_RESULT(x) \
{ \
	FMOD_RESULT _result = x; \
	if (_result != FMOD_OK) \
	{ \
		__android_log_print(ANDROID_LOG_ERROR, "fmod", "FMOD error! (%d) %s\n%s:%d", _result, FMOD_ErrorString(_result), __FILE__, __LINE__); \
		exit(-1); \
	} \
}


void Java_com_yurisuzuki_CameraActivity_cBegin(JNIEnv *env,
		jobject thiz, jobjectArray pathStringArray, int isMonoPhone) {
	FMOD_RESULT result = FMOD_OK;

	isMonoPhonic = isMonoPhone;

	result = FMOD_System_Create(&gSystem);
	CHECK_RESULT(result);

	int numBuffers = 0;
	int size = 0;
	result = FMOD_System_GetDSPBufferSize(gSystem, &size, &numBuffers);
	CHECK_RESULT(result);
	__android_log_print(ANDROID_LOG_ERROR, "fmod",
			"(Before)DSP buffer size is %d, num of buff is %d", size,
			numBuffers);

	result = FMOD_System_SetDSPBufferSize(gSystem, 512, numBuffers);
	CHECK_RESULT(result);
	__android_log_print(ANDROID_LOG_ERROR, "fmod", "Set DSP buffer size to %d",
			512);

	result = FMOD_System_GetDSPBufferSize(gSystem, &size, &numBuffers);
	CHECK_RESULT(result);
	__android_log_print(ANDROID_LOG_ERROR, "fmod",
			"(After)DSP buffer size is %d, num of buff is %d", size,
			numBuffers);

	result = FMOD_System_Init(gSystem, 32, FMOD_INIT_NORMAL, 0);
	CHECK_RESULT(result);


	numOfSounds = (*env)->GetArrayLength(env, pathStringArray);
	unsigned int i = 0;
    for (i=0; i<numOfSounds; i++) {
        jstring string = (jstring) (*env)->GetObjectArrayElement(env, pathStringArray, i);
        const char *path = (char *)(*env)->GetStringUTFChars(env, string, 0);
        // Don't forget to call `ReleaseStringUTFChars` when you're done.

    	result = FMOD_System_CreateSound(gSystem, path,
    			FMOD_DEFAULT | FMOD_LOOP_OFF, 0, &gSound[i]);
    	CHECK_RESULT(result);
    }
    
    result = FMOD_System_CreateDSPByType(gSystem, FMOD_DSP_TYPE_DISTORTION, &gDSPDistortion);
    CHECK_RESULT(result);

}

void Java_com_yurisuzuki_CameraActivity_cUpdate(JNIEnv *env, jobject thiz)
{
	FMOD_RESULT	result = FMOD_OK;

	result = FMOD_System_Update(gSystem);
	CHECK_RESULT(result);
}

void Java_com_yurisuzuki_CameraActivity_cEnd(JNIEnv *env, jobject thiz)
{
	FMOD_RESULT result = FMOD_OK;
    
    result = FMOD_DSP_Release(gDSPDistortion);
    CHECK_RESULT(result);
    
	unsigned int i = 0;

	for (i = 0; i < numOfSounds; i++)
	{
		result = FMOD_Sound_Release(gSound[i]);
		CHECK_RESULT(result);
	}

	result = FMOD_System_Release(gSystem);
	CHECK_RESULT(result);
}

void Java_com_yurisuzuki_CameraActivity_cPlaySound(JNIEnv *env, jobject thiz, int id)
{
	FMOD_RESULT result = FMOD_OK;

	// stop current sound if playing
	if ((isMonoPhonic == 1 ) && (gChannel))
	{
		FMOD_Channel_Stop(gChannel);
	}


	result = FMOD_System_PlaySound(gSystem, FMOD_CHANNEL_FREE, gSound[id], 0, &gChannel);

	CHECK_RESULT(result);
}

jint Java_com_yurisuzuki_CameraActivity_cGetChannelsPlaying(JNIEnv *env, jobject thiz)
{
	FMOD_RESULT result = FMOD_OK;
	int chans = 0;

	result = FMOD_System_GetChannelsPlaying(gSystem, &chans);
	CHECK_RESULT(result);

	return chans;
}

jboolean Java_com_yurisuzuki_CameraActivity_cGetPlaying(JNIEnv *env, jobject thiz)
{
	FMOD_RESULT result = FMOD_OK;
	FMOD_BOOL playing = 0;

	if (gChannel)
	{
		result = FMOD_Channel_IsPlaying(gChannel, &playing);
		if (result != FMOD_ERR_INVALID_HANDLE && result != FMOD_ERR_CHANNEL_STOLEN)
		{
			CHECK_RESULT(result);
		}
	}

	return playing;
}

jint Java_com_yurisuzuki_CameraActivity_cGetPosition(JNIEnv *env, jobject thiz)
{
	FMOD_RESULT result = FMOD_OK;
	int position = 0;

	if (gChannel)
	{
		result = FMOD_Channel_GetPosition(gChannel, &position, FMOD_TIMEUNIT_MS);
		if (result != FMOD_ERR_INVALID_HANDLE && result != FMOD_ERR_CHANNEL_STOLEN)
		{
			CHECK_RESULT(result);
		}
	}

	return position;
}

jint Java_com_yurisuzuki_CameraActivity_cGetLength(JNIEnv *env, jobject thiz)
{
	FMOD_RESULT result = FMOD_OK;
	FMOD_SOUND *sound = 0;
	int length = 0;

	if (gChannel)
	{
		result = FMOD_Channel_GetCurrentSound(gChannel, &sound);
		if (result != FMOD_ERR_INVALID_HANDLE && result != FMOD_ERR_CHANNEL_STOLEN)
		{
			CHECK_RESULT(result);
		}
	}

	if (sound)
	{
		result = FMOD_Sound_GetLength(sound, &length, FMOD_TIMEUNIT_MS);
		CHECK_RESULT(result);
	}

	return length;
}

void Java_com_yurisuzuki_CameraActivity_cDistortionToggle(JNIEnv *env, jobject thiz)
{
    FMOD_RESULT result = FMOD_OK;
    FMOD_BOOL active = 0;
    
    result = FMOD_DSP_GetActive(gDSPDistortion, &active);
    CHECK_RESULT(result);
    
    if (active)
    {
        result = FMOD_DSP_Remove(gDSPDistortion);
        CHECK_RESULT(result);
    }
    else
    {
        result = FMOD_System_AddDSP(gSystem, gDSPDistortion, 0);
        CHECK_RESULT(result);
        
        result = FMOD_DSP_SetParameter(gDSPDistortion, FMOD_DSP_DISTORTION_LEVEL, 0.8f);
        CHECK_RESULT(result);
    }
}


