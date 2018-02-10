/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todolistgradedexample;

import java.util.Comparator;

/**
 *
 * @author shree
 * @param <E>
 */
public class ItemComparator <E extends Comparable<E>> implements Comparator<Item<E>>
{
    @Override
    public int compare(Item<E> o1, Item<E> o2) {
       return o1.getDeadLine().compareTo(o2.getDeadLine());
    }
    
}
