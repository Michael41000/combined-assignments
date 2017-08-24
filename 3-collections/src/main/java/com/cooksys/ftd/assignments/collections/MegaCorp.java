package com.cooksys.ftd.assignments.collections;

import com.cooksys.ftd.assignments.collections.hierarchy.Hierarchy;
import com.cooksys.ftd.assignments.collections.model.Capitalist;
import com.cooksys.ftd.assignments.collections.model.FatCat;
import com.cooksys.ftd.assignments.collections.model.WageSlave;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class MegaCorp implements Hierarchy<Capitalist, FatCat> {

    private Set<Capitalist> hierarchy;
	
	public MegaCorp()
	{
		hierarchy = new HashSet<Capitalist>();
	}
    
    /**
     * Adds a given element to the hierarchy.
     * <p>
     * If the given element is already present in the hierarchy,
     * do not add it and return false
     * <p>
     * If the given element has a parent and the parent is not part of the hierarchy,
     * add the parent and then add the given element
     * <p>
     * If the given element has no parent but is a Parent itself,
     * add it to the hierarchy
     * <p>
     * If the given element has no parent and is not a Parent itself,
     * do not add it and return false
     * <p>
     *
     * @param capitalist the element to add to the hierarchy
     * @return true if the element was added successfully, false otherwise
     */
    @Override
    public boolean add(Capitalist capitalist) {
        
    	if (capitalist == null)
        {
        	return false;
        }
    	
    	// If the given element is already present in the hierarchy
        if (has(capitalist))
        {
        	return false;
        } 
        // If the given element has a parent and the parent is not part of the hierarchy
        else if (capitalist.hasParent() && !has(capitalist.getParent()))
        {
        	// Recursively add the parents to the hierarchy
        	add(capitalist.getParent());
        	hierarchy.add(capitalist);
        	return true;
        }
        // If the given element has no parent and is a parent
        else if (!capitalist.hasParent() && capitalist instanceof FatCat)
        {
        	hierarchy.add(capitalist);
        	return true;
        }
        // If the given element has no parent and is a not a parent
        else if (!capitalist.hasParent() && capitalist instanceof WageSlave)
        {
        	return false;
        }
        else
        {
        	hierarchy.add(capitalist);
        	return true;
        }
    }

    /**
     * @param capitalist the element to search for
     * @return true if the element has been added to the hierarchy, false otherwise
     */
    @Override
    public boolean has(Capitalist capitalist) { 
        if (hierarchy.contains(capitalist))
        {
        	return true;
        }
        
        // The capitalist was not found, return false
        return false;
    }

    /**
     * @return all elements in the hierarchy,
     * or an empty set if no elements have been added to the hierarchy
     */
    @Override
    public Set<Capitalist> getElements() {
    	// Defensive set to return so that the user doesn't mess with original set
    	Set<Capitalist> returnSet = new HashSet<>();
    	returnSet.addAll(hierarchy);
    	return returnSet;
    }

    /**
     * @return all parent elements in the hierarchy,
     * or an empty set if no parents have been added to the hierarchy
     */
    @Override
    public Set<FatCat> getParents() {
    	Set<FatCat> returnSet = new HashSet<>();
    	// Add all the fatcats to the returnSet
    	for (Capitalist c : hierarchy)
    	{
    		if (c instanceof FatCat)
    		{
    			returnSet.add((FatCat) c);
    		}
    	}
    	return returnSet;
    }

    /**
     * @param fatCat the parent whose children need to be returned
     * @return all elements in the hierarchy that have the given parent as a direct parent,
     * or an empty set if the parent is not present in the hierarchy or if there are no children
     * for the given parent
     */
    @Override
    public Set<Capitalist> getChildren(FatCat fatCat) {
    	Set<Capitalist> returnSet = new HashSet<>();
    	// Make sure fatCat is in the hierarchy
    	if (has(fatCat))
    	{
    		for (Capitalist c : hierarchy)
    		{
    			if (c.hasParent())
    			{
    				// If the fatCat is equal to the parent of the capitalist in the hierarchy
    				// add it to the return set
					if (c.getParent().equals(fatCat))
					{
						
						returnSet.add(c);
					}
    			}
    		}
    	}
    	
    	return returnSet;
    }

    /**
     * @return a map in which the keys represent the parent elements in the hierarchy,
     * and the each value is a set of the direct children of the associate parent, or an
     * empty map if the hierarchy is empty.
     */
    @Override
    public Map<FatCat, Set<Capitalist>> getHierarchy() {
        Map<FatCat, Set<Capitalist>> returnMap = new HashMap<>();
        // Go through each element in the hierarchy as the parent to find each parents children
        for (Capitalist parent : hierarchy)
        {
        	if (parent instanceof FatCat)
        	{
        		Set<Capitalist> children = new HashSet<>();
        		// Go through each child in the hierarchy as the parent and check if the 
	        	for (Capitalist child : hierarchy)
	        	{
	        		if (child.hasParent())
	        		{
	        			if (child.getParent().equals(parent))
	        			{
	        				children.add(child);
	        			}
	        		}
	        	}
	        	returnMap.put((FatCat) parent, children);
        	}
        }
        
        return returnMap;
    }

    /**
     * @param capitalist
     * @return the parent chain of the given element, starting with its direct parent,
     * then its parent's parent, etc, or an empty list if the given element has no parent
     * or if its parent is not in the hierarchy
     */
    @Override
    public List<FatCat> getParentChain(Capitalist capitalist) {
    	List<FatCat> returnSet = new ArrayList<FatCat>();
    	if (capitalist == null || !has(capitalist.getParent()))
    	{
    		return returnSet;
    	}
    	
    	while (capitalist.hasParent())
    	{
    		for (Capitalist c : hierarchy)
    		{
    			if (capitalist.getParent().equals(c))
    			{
    				returnSet.add(capitalist.getParent());
    				capitalist = capitalist.getParent();
    				break;
    			}
    		}
    	}
    	
    	return returnSet;
    }
}
