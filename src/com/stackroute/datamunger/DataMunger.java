package com.stackroute.datamunger;

import java.util.ArrayList; 
import java.util.Scanner;

public class DataMunger {

	public static void main(String[] args) {
		// read the query from the user into queryString variable 
		Scanner sc = new Scanner(System.in); 
		String queryString = sc.nextLine();
		
		// call the parseQuery method and pass the queryString variable as a parameter
		DataMunger instance = new DataMunger();
		instance.parseQuery(queryString);
		sc.close();
	}

	
	public void parseQuery(String queryString) {
 
		// call the methods
		getSplitStrings(queryString);
		getFile(queryString);
		getBaseQuery(queryString);
		getConditionsPartQuery(queryString);
		getConditions(queryString);
		getLogicalOperators(queryString);
		getFields(queryString);
		getOrderByFields(queryString);
		getGroupByFields(queryString);
		getAggregateFunctions(queryString);
		
	}


	public String[] getSplitStrings(String queryString) {
		String[] arr = queryString.split(" ");
		for(int i = 0;i<arr.length;i++) {
			arr[i] = arr[i].toLowerCase();
		}
		return arr;
	}

	
	public String getFile(String queryString) {
		return queryString.split("from ")[1].split(" ")[0]; 
	}

	public String getBaseQuery(String queryString) {
		String[] arr = queryString.split(" ");
		String ret = "";
		boolean bool = true;
		for(int i = 0; i < arr.length; i++) {
			if(arr[i].toLowerCase().equals("where") || arr[i].toLowerCase().equals("order") || arr[i].toLowerCase().equals("group")) break;
			if(bool) {
				ret += arr[i] + " "; 
			}
		} 
		System.out.println(ret);
		return ret; 
	}

	/*
	 * This method is used to extract the conditions part from the query string. The
	 * conditions part contains starting from where keyword till the next keyword,
	 * which is either group by or order by clause. In case of absence of both group
	 * by and order by clause, it will contain till the end of the query string.
	 * Note: 
	 * ----- 
	 * 1. The field name or value in the condition can contain keywords
	 * as a substring. 
	 * For eg: from_city,job_order_no,group_no etc. 
	 * 2. The query might not contain where clause at all.
	 */
	public String getConditionsPartQuery(String queryString) {
		//System.out.println("conditions" + !queryString.contains("where"));
		if(!queryString.contains("where")) return null ;
		String[] arr = queryString.split(" "); 
		String ret = "";
		boolean bool = true;
		for(int i = 0; i < arr.length; i++) {
			if(arr[i].toLowerCase().equals("where")) bool = false;
			else if(arr[i].toLowerCase().equals("order") || arr[i].toLowerCase().equals("group") ) break;
			
			if(!bool) {
				ret += arr[i].toLowerCase() + " ";
			}
		} 
		ret = ret.trim();
		
		ret = ret.substring(ret.indexOf(" "), ret.length()).replaceFirst("\\s++$", ""); 
		//System.out.println(" season > 2014 and city ='bangalore' " == ret);
		//System.out.println("Condition part:" + (" season > 2014 and city ='bangalore' " ==ret));
		if(queryString.contains("group") ||queryString.contains("order")  ) return ret + " ";
		return ret;   
	}

	
	public String[] getConditions(String queryString)  {
		if(!queryString.contains("where")) return null; 
		queryString = getConditionsPartQuery(queryString);
		queryString = queryString.substring(queryString.indexOf(" "), queryString.length()).trim();
		String[] arr = queryString.split("(\\band\\b|\\bor\\b)");
		for(int i = 0; i< arr.length; i++) {
			arr[i] = arr[i].trim();
		}
		return arr;
	}

	
	public String[] getLogicalOperators(String queryString) {

		if(!queryString.contains("where")) return null; 
		queryString = getConditionsPartQuery(queryString);
		queryString = queryString.substring(queryString.indexOf(" "), queryString.length()).trim();
		String arr[] = queryString.split(" ");
		ArrayList<String> ret = new ArrayList<String>();
		String r[] = new String[ ret.size() ];
		int operators = 1;
		for(int i = 0; i < arr.length; i++ ) {
			if(arr[i].toLowerCase().equals("and") || arr[i].toLowerCase().equals("or") || arr[i].toLowerCase().equals("not")) {
				ret.add(arr[i]);
				operators++;
			}
		}
		return ret.toArray(r); 
	}

	public String[] getFields(String queryString) {
		queryString = queryString.split("from")[0];
		queryString = queryString.substring(queryString.indexOf(" "), queryString.length()).trim();
		String[] arr = queryString.split("\\s*,\\s*");;
		//System.out.println(queryString);
		for(int i = 0; i< arr.length; i++) {
			arr[i] = arr[i].trim();
			//System.out.println("Get Fields: " + arr[i]); 
		}
		return arr;
	}

	
	
	public String[] getOrderByFields(String queryString) {
		String arr[] = queryString.split(" ");
		boolean bool = false;
		String ret = "";
		for(int i = 0; i < arr.length; i++) {
			if(arr[i].equals("order")) bool = true;
			else if(arr[i].equals("group")) bool = false;
			if(bool && !arr[i].equals("by") && !arr[i].equals("order")) ret += arr[i] + " ";
		}
		return ret.split(" ");
	}

	public String[] getGroupByFields(String queryString) { 
		String arr[] = queryString.split(" ");
		boolean bool = false;
		String ret = "";
		for(int i = 0; i < arr.length; i++) {
			if(arr[i].equals("group")) bool = true;
			else if(arr[i].equals("order")) bool = false;
			if(bool && !arr[i].equals("by") && !arr[i].equals("group") && !arr[i].equals("order")) {
				ret += arr[i] + " ";
			}
		}
		if(ret.length() == 0) return null;
		return ret.split(" ");
	}
	
	public String[] getAggregateFunctions(String queryString) { 
		String[] arr = queryString.split("from");
        String[] arr1 = arr[0].split("select");;
        String[] arr2 = arr1[1].trim().split(",");
        if(arr2[0].equals("*")) return null;
        
            for (int i = 0; i < arr2.length; i++) {
            }
            return arr2; 
	}

}