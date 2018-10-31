package weka.filters.timeseries.shapelet_transforms.idp;

import java.util.ArrayList;
import java.util.List;

public class PLR_IDP {
	private int[] point;
	private double[] eui;
	private List<Line> lineList;
	private double data[];

	public PLR_IDP(double[] series) {
		int length = series.length;
		point = new int[length];
		eui = new double[length];
		data = new double[length];
		for (int i = 0; i < length; i++) {
			point[i] = 0;// 默认未选取
			eui[i] = Double.MAX_VALUE;
			data[i] = series[i];
		}
		lineList = new ArrayList<Line>();
	}

	/**
	 * 按阈值分段
	 * 
	 * @param threshold
	 *            分段误差
	 * @return
	 */
	public int[] choosePointIDPByThreshold(double threshold) {
		point[0] = 1;
		point[data.length - 1] = 1;
		updataInfo(0, data.length - 1);
		Line line;
		do  {
			lineList.sort(new LineComparatorIDP());
			line = lineList.get(0);
			point[line.getPmax()] = 1;
			updataInfo(line.getBegin(), line.getPmax());
			updataInfo(line.getPmax(), line.getEnd());
			lineList.remove(0);
			lineList.sort(new LineComparatorIDP());
			line = lineList.get(0);
		}while(line.getWeight() >= threshold);
		return point;

	}

	/**
	 * 按个数
	 * 
	 * @param number
	 *            分段个数
	 * @return
	 */
	public int[] choosePointIDPByNumber(int number) {
		point[0] = 1;
		point[data.length - 1] = 1;
		updataInfo(0, data.length - 1);
		lineList.sort(new LineComparatorIDP());
		int pointNumber = 2;
		while (pointNumber < number) {
			pointNumber++;
			Line line = lineList.get(0);
			point[line.getPmax()] = 1;
			//System.out.println(line.getPmax());
			updataInfo(line.getBegin(), line.getPmax());
			updataInfo(line.getPmax(), line.getEnd());
			lineList.remove(0);
			lineList.sort(new LineComparatorIDP());
		}
		return point;

	}

	// 更新信息
	public void updataInfo(int begin, int end) {
		int pmax = begin;
		double dist = 0;
		double distmax = 0;
		eui[begin] = 0;
		eui[end] = 0;
		for (int i = begin + 1; i < end; i++) {
			eui[i] = dist(begin + 1, data[begin], end + 1, data[end], i + 1,
					data[i]);
			dist += eui[i];
			if (eui[i] > distmax) {
				pmax = i;
				distmax = eui[i];
			}
		}
		// weight计算PLR_IDP和PLR_SIP不同
		double weight = 2 * distmax > dist ? 2 * distmax : dist;
		Line line = new Line(begin, end, dist, distmax, pmax, weight);
		lineList.add(line);

	}

	// 计算点到直线的距离
	public double dist(double x1, double y1, double x2, double y2, double x0,
			double y0) {
		return Math.abs((x0 - x1) * (y2 - y1) / (x2 - x1) + y1 - y0);
	}

	// 按阈值返回索引
	public int[] getIDPIndexByThreshold(double threshold) {
		int[] IDP = choosePointIDPByThreshold(threshold);
		List<Integer> list = new ArrayList<Integer>();
		int number = 0;
		for (int i = 0; i < IDP.length; i++) {
			if (IDP[i] == 1) {
				number++;
				list.add(i);
			}
		}
		int[] IDPindex = new int[number];
		for (int i = 0; i < list.size(); i++) {
			IDPindex[i] = list.get(i);
		}
		return IDPindex;
	}

	// 按个数返回索引
	public int[] getIDPIndexByNumber(int number) {
		int[] IDP = choosePointIDPByNumber(number);
		
		int[] IDPindex = new int[number];
		int index = 0;
		for (int i = 0; i < IDP.length; i++) {
			if (IDP[i] == 1) {
				IDPindex[index]=i;
				index++;
			}
		}
		return IDPindex;
	}
	public static void main(String[] args){
		double[] a={10.1,12.3,12.0,15.0,17.0,18.0,12.3,14.4,12.3,5.6,12.4,8.6};
		PLR_IDP p=new PLR_IDP(a);
		int[] b=p.getIDPIndexByNumber(5);
		int[] c=p.getIDPIndexByThreshold(3);
		System.out.print(1);
	}

}
