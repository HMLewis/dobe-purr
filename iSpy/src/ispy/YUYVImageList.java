package ispy;

import java.util.ArrayList;

import edu.hendrix.ev3webcam.YUYVImage;

public class YUYVImageList {
	private ArrayList<YUYVImage> images;
	private ArrayList<Double> distances;
	
	public YUYVImageList() {
		images = new ArrayList<YUYVImage>();
		distances = new ArrayList<Double>();
	}
	
	public void add(YUYVImage img, Double where) {
		images.add(img);
		distances.add(where);
	}
	
	public YUYVImage getImage(int i) {
		return images.get(i);
	}
	
	public Double getDistance(int i) {
		return distances.get(i);
	}
	
	public Double getLastLocation() {
		return getDistance(size() - 1);
	}
	
	public int getBaseline(){
	    return 0;
	}
	
	public void addGood(Double distance){
	    this.add(images.get(0), distances.get(0));
	}
	
	public int size() {return images.size();}
	
	public static YUYVImageList fromString(String src) {
		YUYVImageList result = new YUYVImageList();
		for (String imgStr: src.split("\n")) {
			String[] parts = imgStr.split(";");
			//result.locations.add(new Location(parts[0]));
			result.distances.add(Double.valueOf(parts[0]));
			result.images.add(YUYVImage.fromString(parts[1]));
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < size(); ++i) {
			//result.append(locations.get(i).toString());
			result.append(distances.get(i).toString());
			result.append(";");
			result.append(images.get(i).toString());
			result.append("\n");
		}
		return result.toString();
	}
	
	@Override
	public int hashCode() {
		int sum = 0;
		for (YUYVImage img: images) {
			sum += img.hashCode();
		}
		return sum;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof YUYVImageList) {
			YUYVImageList that = (YUYVImageList)other;
			return this.images.equals(that.images);
		} else {
			return false;
		}
	}
}
