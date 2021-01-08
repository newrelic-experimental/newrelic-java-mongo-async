package com.nr.reactivemongo.instrumentation;

import java.util.StringTokenizer;

public class ReactiveMongoUtils {

	public static String[] splitFullCollectionName(String fullCollectionName) {
		StringTokenizer st = new StringTokenizer(fullCollectionName, ".");
		String db = "Unknown";
		String coll = "Unknown";
		if(st.hasMoreTokens()) {
			db = st.nextToken();
		}
		if(st.hasMoreTokens()) {
			coll = st.nextToken();
		}
		String[] result =  {db,coll};
		return result;
	}
}
