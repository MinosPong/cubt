package edu.cuhk.cubt.classifier;

import edu.cuhk.cubt.state.SpeedState;

public class SpeedClassifier extends AbstractClassifier<SpeedState> {
	
	private float speed = 0;
	
	public SpeedClassifier() {
		super(SpeedState.UNKNOWN);
	}

	public float getSpeed() {
		return speed;
	}

	@Override
	protected void processClassification() {
		// TODO Auto-generated method stub
		
	};
}
