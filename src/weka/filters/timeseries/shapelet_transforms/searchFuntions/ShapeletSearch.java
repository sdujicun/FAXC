/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.filters.timeseries.shapelet_transforms.searchFuntions;

import java.io.Serializable;
import java.util.ArrayList;

import weka.core.Instance;
import weka.core.shapelet.Shapelet;
import weka.filters.timeseries.shapelet_transforms.idp.PLR_IDP;

/**
 *
 * @author raj09hxu
 */
public class ShapeletSearch implements Serializable {

	public interface ProcessCandidate {
		public Shapelet process(double[] candidate, int start, int length);
	}

	protected int minShapeletLength;
	protected int maxShapeletLength;

	protected int lengthIncrement = 1;
	protected int positionIncrement = 1;

	public ShapeletSearch(int min, int max) {
		minShapeletLength = min;
		maxShapeletLength = max;
	}

	public ShapeletSearch(int min, int max, int lengthInc, int posInc) {
		this(min, max);
		lengthIncrement = lengthInc;
		positionIncrement = posInc;
	}

	public void setMinAndMax(int min, int max) {
		minShapeletLength = min;
		maxShapeletLength = max;
	}

	public ArrayList<Shapelet> SearchForShapeletsInSeries(Instance timeSeries, ProcessCandidate checkCandidate) {
		ArrayList<Shapelet> seriesShapelets = new ArrayList<>();

		double[] series = timeSeries.toDoubleArray();

		for (int length = minShapeletLength; length <= maxShapeletLength; length += lengthIncrement) {
			// for all possible starting positions of that length. -1 to remove
			// classValue
			for (int start = 0; start <= timeSeries.numAttributes() - length - 1; start += positionIncrement) {
				Shapelet shapelet = checkCandidate.process(series, start, length);

				if (shapelet != null) {
					seriesShapelets.add(shapelet);
				}
			}
		}

		return seriesShapelets;
	}
	
	public ArrayList<Shapelet> SearchForShapeletsInSeriesBasedIDP(Instance timeSeries, ProcessCandidate checkCandidate) {
		ArrayList<Shapelet> seriesShapelets = new ArrayList<>();

		double[] series = timeSeries.toDoubleArray();
		double[] tempSeries = new double[series.length - 1];
		for (int i = 0; i < tempSeries.length; i++) {
			tempSeries[i] = series[i];
		}
		int[] IDP = new PLR_IDP(tempSeries).getIDPIndexByNumber((int) Math.ceil(0.05*tempSeries.length)+2);
		
	for (int start = 0; start < IDP.length - 2; start++) {
			for (int end = start + 2; end < IDP.length; end++) {
				Shapelet shapelet = checkCandidate.process(series, IDP[start], IDP[end] - IDP[start]);
				if (shapelet != null) {
					seriesShapelets.add(shapelet);
				}
			}
		}

		return seriesShapelets;
	}
	
	public ArrayList<Shapelet> SearchForShapeletsInSeriesBasedIDP(Instance timeSeries, ProcessCandidate checkCandidate, double IDPrate) {
		ArrayList<Shapelet> seriesShapelets = new ArrayList<>();

		double[] series = timeSeries.toDoubleArray();
		double[] tempSeries = new double[series.length - 1];
		for (int i = 0; i < tempSeries.length; i++) {
			tempSeries[i] = series[i];
		}
		int[] IDP = new PLR_IDP(tempSeries).getIDPIndexByNumber((int) Math.ceil(IDPrate*tempSeries.length)+2);
			
		for (int start = 0; start < IDP.length - 2; start++) {
			for (int end = start + 2; end < IDP.length; end++) {
				Shapelet shapelet = checkCandidate.process(series, IDP[start], IDP[end] - IDP[start]);
				if (shapelet != null) {
					seriesShapelets.add(shapelet);
				}
			}
		}

		return seriesShapelets;
	}
	
	public ArrayList<Shapelet> SearchForShapeletsInSeriesBasedIDPWithNumberLimit(Instance timeSeries, ProcessCandidate checkCandidate, double IDPrate) {
		ArrayList<Shapelet> seriesShapelets = new ArrayList<>();

		double[] series = timeSeries.toDoubleArray();
		double[] tempSeries = new double[series.length - 1];
		for (int i = 0; i < tempSeries.length; i++) {
			tempSeries[i] = series[i];
		}
		int[] IDP = new PLR_IDP(tempSeries).getIDPIndexByNumber((int) Math.ceil(IDPrate*tempSeries.length)+2);		
		for (int start = 0; start < IDP.length - 2; start++) {
			for (int end = start + 2; end < IDP.length&&end<=start+6; end++) {
				Shapelet shapelet = checkCandidate.process(series, IDP[start], IDP[end] - IDP[start]);
				if (shapelet != null) {
					seriesShapelets.add(shapelet);
				}
			}
		}

		return seriesShapelets;
	}
}
