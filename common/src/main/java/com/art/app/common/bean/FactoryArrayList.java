package com.art.app.common.bean;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.OrderComparator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class FactoryArrayList<K, E extends MatchingBean<K>> extends ArrayList<E> 
        implements FactoryList<K, E>,InitializingBean {

    private static final long serialVersionUID = 5705342394882249201L;

    public FactoryArrayList() {
        super();
    }
    
    public FactoryArrayList(int size) {
        super(size);
    }
    
    public E getBean(K factor) {
        Iterator<E> itr = iterator();
        while(itr.hasNext()) {
            E beanMatch = itr.next();
			try {
				if(beanMatch.matching(factor)) {
					return beanMatch;
				}
			}
			/**
			 * 泛型擦除，编译时生成matching(Object)方法，不会动态寻找合适多态方法；
			 * 在父类被替换场景下产生ClassCastException异常，此时尝试利用反射寻找合适的方法；
			 * 使用案例见adp-access-convertor com.tuniu.apollo.adp.offline.ghotel.access.VendorCityInfoService
			 */
			catch (ClassCastException e) {
				try {
					Method matching = beanMatch.getClass().getMethod("matching", factor.getClass());
					Boolean matched = (Boolean) matching.invoke(beanMatch, factor);
					if (matched) return beanMatch;
				} catch (Exception e1) {
					throw new RuntimeException("try matching method error", e1);
				}
			}

        }
        return null;
    }

    public void afterPropertiesSet() throws Exception {
        if (!isEmpty()) {
            Object[] a = toArray();
            OrderComparator.sort(a);
            ListIterator i = listIterator();
            for (int j=0; j<a.length; j++) {
                i.next();
                i.set(a[j]);
            }
        }
    }

}
