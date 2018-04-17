package com.neu.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

public class TestSet {
	static String t;
	
	@Test
	public void test1(){
		Set<String> set = new HashSet<>();
		set.addAll(Arrays.asList("1", "2", "3"));
		Set<String> set2 = set;
		Iterator<String> iterator = set2.iterator();
		System.out.println(set);
		iterator.next();
		iterator.remove();
		System.out.println(set);
	}
	
	@Test
	public void test2(){
		new TestSet().t = "123";
		System.out.println(new TestSet().t);
	}
	
	@Test
	public void test3(){
		System.out.println(TestSet.t);
	}
	
	@Test
	public void test4(){
		String s = new String("test");
		String str = new String();
		str = s;
		System.out.println(s);
		str = "123";
		System.out.println(s);
	}
}
