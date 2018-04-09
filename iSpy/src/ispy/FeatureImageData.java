package ispy;

import edu.hendrix.ev3webcam.YUYVImage;
import edu.hendrix.img.features.FeatureSet;

public class FeatureImageData extends ImageData<FeatureSet> {
	public FeatureImageData(YUYVImage src, double where) {
		super(src, where);
	}

	@Override
	public FeatureSet process(YUYVImage src) {
		return new FeatureSet(src);
	}

	@Override
	protected double distanceTo(FeatureSet srcProc, FeatureSet otherProc) {
		return srcProc.distanceTo(otherProc);
	}

}
