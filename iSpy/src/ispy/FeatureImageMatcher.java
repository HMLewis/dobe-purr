package ispy;

import edu.hendrix.ev3webcam.YUYVImage;
import edu.hendrix.img.features.FeatureSet;

public class FeatureImageMatcher extends ImageMatcher<FeatureSet> {

	public FeatureImageMatcher(YUYVImageList images) {
		super(images);
	}

	@Override
	public ImageData<FeatureSet> makeImageData(YUYVImage img, double where) {
		return new FeatureImageData(img, where);
	}

}
