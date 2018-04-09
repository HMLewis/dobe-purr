package trash;

import ispy.FeatureImageMatcher;
import ispy.YUYVImageList;
import edu.hendrix.img.features.FeatureSet;

public class RunTrainer extends Trainer<FeatureSet,FeatureImageMatcher>{

	@Override
	public FeatureImageMatcher makeMatcherFrom(YUYVImageList images) {
		return new FeatureImageMatcher(images);
	}

	public static void main(String[] args) {
		new RunTrainer().run();
	}
}