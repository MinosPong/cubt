package edu.cuhk.cubt.ui;

import android.content.Context;
import edu.cuhk.cubt.sccm.SCCMEngine;

public class CubtService {

	private static SCCMEngine mSccmEngine;
	public static SCCMEngine sccmEngine(Context context){
		if(mSccmEngine == null){
			mSccmEngine = new SCCMEngine(context);
			mSccmEngine.startEngine();
		}
		return mSccmEngine;
	}
	
}
