package weka.filters.timeseries.shapelet_transforms.idp;

import java.util.Comparator;


public class LineComparatorIDP implements Comparator {
	public final int compare(Object pFirst, Object pSecond) {
		double a = ((Line) pFirst).getWeight();
		double b = ((Line) pSecond).getWeight();

		//½µĞòÅÅÁĞ
		if (a>b)
			return -1;
		if (a< b)
			return 1;
		else
			return 0;
	}

}
