package ispy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import edu.hendrix.ev3webcam.YUYVImage;
import edu.hendrix.img.IntImage;

abstract public class ImageMatcher<T> {
	private ArrayList<ImageData<T>> database;
	
	public ImageMatcher(YUYVImageList images) {
		database = new ArrayList<ImageData<T>>();
		for (int i = 0; i < images.size(); ++i) {
			database.add(makeImageData(images.getImage(i), images.getDistance(i)));
		}
	}
	
	abstract public ImageData<T> makeImageData(YUYVImage img, double where);
	
	public ImageMatch getBestMatch(YUYVImage input) {
		ImageMatch best = null;
		for (ImageData<T> data: database) {
			double sim = data.distanceTo(input);
			if (best == null || best.getDistance() > sim) {
				best = new ImageMatch(data.getSourceImage(), data.getDistance(), sim);
			}
		}
		return best;
	}

	public ImageMatch getBestMatch(YUYVImage input, double at, int numCandidates) {
		ImageMatch best = null;
		for (ImageData<T> data: topNClosestTo(at, numCandidates)) {
			double sim = data.distanceTo(input);
			if (best == null || best.getDistance() > sim) {
				best = new ImageMatch(data.getSourceImage(), data.getDistance(), sim);
			}
		}
		return best;
	}
	
	public ArrayList<ImageData<T>> topNClosestTo(double at, int n) {
		Set<Integer> bestSet = new HashSet<Integer>();
		ArrayList<ImageData<T>> result = new ArrayList<ImageData<T>>();
		for (int i = 0; i < n; ++i) {
			int best = 0;
			double bestDist = Math.abs(database.get(0).getDistance() - at);
			for (int j = 1; j < database.size(); ++j) {
				if (!bestSet.contains(j)) {
					double dist = Math.abs(database.get(0).getDistance() - at);
					if (dist < bestDist) {
						bestDist = dist;
						best = j;
					}
				}
			}
			result.add(database.get(best));
			bestSet.add(best);
		}
		return result;
	}

	/*
	public ImageMatch getBestMatchWithin(YUYVImage input, Location at, double maxDistance) {
		ImageMatch best = null;
		for (ImageData<T> data: database) {
			if (data.getLocation().distanceTo(at) <= maxDistance) {
				double sim = data.distanceTo(input);
				if (best == null || best.getDistance() > sim) {
					best = new ImageMatch(data.getSourceImage(), data.getLocation(), sim);
				}
			}
		}
		return best;
	}
	*/
}
