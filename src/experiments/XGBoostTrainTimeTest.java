package experiments;

import xgboost.XGBoostWithST;

public class XGBoostTrainTimeTest {
	public static void main(String[] args) throws Exception {
		XGBoostWithST boost = new XGBoostWithST();
		boost.time();
	}
}
