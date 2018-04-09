package edu.hendrix.img.database;

import ispy.ImageData;
import ispy.ImageMatcher;
import ispy.YUYVImageList;
import edu.hendrix.ev3webcam.YUYVImage;
import edu.hendrix.img.IntImage;

public class IntImageMatcher extends ImageMatcher<IntImage> {

	public IntImageMatcher(YUYVImageList images) {
		super(images);
	}

	@Override
	public ImageData<IntImage> makeImageData(YUYVImage img, double where) {
		return new IntImageData(img, where);
	}

}
