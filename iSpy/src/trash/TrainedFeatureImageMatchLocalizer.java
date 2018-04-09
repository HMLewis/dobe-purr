package trash;

import ispy.FeatureImageMatcher;
import ispy.ImageMatchLocalizer;
import ispy.YUYVImageList;
import edu.hendrix.img.features.FeatureSet;

public class TrainedFeatureImageMatchLocalizer extends ImageMatchLocalizer<FeatureSet,FeatureImageMatcher>{

	@Override
	public FeatureImageMatcher makeMatcherFrom(YUYVImageList images) {
		return new FeatureImageMatcher(images);
	}

	public static void main(String[] args) {
		new TrainedFeatureImageMatchLocalizer().run();
	}
}
