package ispy;

import edu.hendrix.img.features.FeatureSet;

public class SplitFeatureImageMatchLocalizer extends SplitImageMatchLocalizer<FeatureSet,FeatureImageMatcher>{

	@Override
	public FeatureImageMatcher makeMatcherFrom(YUYVImageList images) {
		return new FeatureImageMatcher(images);
	}

	public static void main(String[] args) {
		new SplitFeatureImageMatchLocalizer().run();
	}
}
