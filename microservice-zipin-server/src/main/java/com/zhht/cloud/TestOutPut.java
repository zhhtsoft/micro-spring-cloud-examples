package com.zhht.cloud;

import java.io.UnsupportedEncodingException;

public class TestOutPut {

	
	public static String output="";
	public static void foo(int i){
	   try{
	   if(i==1){
	     throw new Exception();
	}
	   output+="1";
	}catch(Exception ex){
		output+=2;
	  return ;
	}finally{
		output+="3";
	}
	   output+="4";
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		foo(0);
		foo(1);
		System.out.println(output);
		Double d=5.3E12;
	}
}
