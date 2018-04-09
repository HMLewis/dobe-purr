package ispy;

import edu.hendrix.img.features.FeatureSet;

public class RGBFeatureImageMatchLocalizer extends RGBImageMatchLocalizer<FeatureSet,FeatureImageMatcher>{

	@Override
	public FeatureImageMatcher makeMatcherFrom(YUYVImageList images) {
		return new FeatureImageMatcher(images);
	}

	public static void main(String[] args) {
		new RGBFeatureImageMatchLocalizer().run();
	}
}

