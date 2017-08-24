package com.cooksys.ftd.assignments.collections;

import com.cooksys.ftd.assignments.collections.hierarchy.Hierarchy;
import com.cooksys.ftd.assignments.collections.model.Capitalist;
import com.cooksys.ftd.assignments.collections.model.FatCat;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class MegaCorp implements Hierarchy<Capitalist, FatCat> {

    private Map<Capitalist, Map> hierarchy;
	
	public MegaCorp()
	{
		hierarchy = new HashMap<Capitalist, Map>();
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
        	add(capitalist.getParent());
        	Stack<FatCat> parentChain = new Stack<>();
        	parentChain.addAll(getParentChain(capitalist));
        	Map<Capitalist, Map> currentLayer = hierarchy;
        	// Go down the hierarchy until you get to the layer that has the current capitalist's owner
        	while (parentChain.size() > 1)
        	{
        		Capitalist currentLayerOwner = parentChain.pop();
        		Map<Capitalist, Map> nextLayer = currentLayer.get(currentLayerOwner);
        		currentLayer = nextLayer;
        	}
        	
        	// Create the layer where the capitalist will be added
        	Map<Capitalist, Map> addedCapitalistLayer = new HashMap<Capitalist, Map>();
        	addedCapitalistLayer.put(capitalist, null);
        	
        	// Put that newly created layer in the hierarchy under the capitalist's owner
        	if (currentLayer == null)
        	{
        		
        		System.out.println("Hello");
        	}
        	currentLayer.put(capitalist.getParent(), addedCapitalistLayer);
        	
        	return true;
        	
        }
        // If the given element has no parent
        else if (!capitalist.hasParent())
        {
        	// If the given element is a parent
        	// If the given element has no parent but is a parent it must be the top?
        	Map<Capitalist, Map> newHierarchy = new HashMap<Capitalist, Map>();
        	newHierarchy.put(capitalist, hierarchy);
        	hierarchy = newHierarchy;
        	return true;
        	
        }
        
          
          
         
    	
    	return false;
    }

    /**
     * @param capitalist the element to search for
     * @return true if the element has been added to the hierarchy, false otherwise
     */
    @Override
    public boolean has(Capitalist capitalist) { 
        Queue<Map> layersToLookAt = new LinkedList<Map>();
        layersToLookAt.add(hierarchy);
        // Go through until each person has been checked in the hierarchy
        while (layersToLookAt.size() > 0)
        {
        	Map<Capitalist, Map> currentLayer = layersToLookAt.remove();
        	// If one of the people on the current layer is the capitalist you are looking for
        	if (currentLayer.containsKey(capitalist))
        	{
        		return true;
        	}
        	// If the capitalist you are looking for is not on the current layer
        	// move down to the next layer
        	else
        	{
        		//Get all the parents that are on this current level
        		Set<Capitalist> parents = currentLayer.keySet();
        		
        		// Go through each parent on this current level
        		for (Capitalist c : parents)
        		{
        			Map<Capitalist, Map> nextLayer = currentLayer.get(c);
        			// If they have children below them
        			if (nextLayer != null)
        			{
        				// Add that layer to the layers to look at queue
        				layersToLookAt.add(nextLayer);
        			}
        		}
        	}
        }
        
        // The capitalist was not found on any layer, so he is not in the hierarchy
        return false;
    }

    /**
     * @return all elements in the hierarchy,
     * or an empty set if no elements have been added to the hierarchy
     */
    @Override
    public Set<Capitalist> getElements() {
    	Set<Capitalist> elements = new HashSet<>();
    	
    	Queue<Map> layersToLookAt = new LinkedList<Map>();
    	// Add beginning layer to look at
        layersToLookAt.add(hierarchy);
        // Go through until each person has been checked in the hierarchy
        while (layersToLookAt.size() > 0)
        {
        	Map<Capitalist, Map> currentLayer = layersToLookAt.remove();
    		//Get all the parents that are on this current level
    		Set<Capitalist> parents = currentLayer.keySet();
    		
    		// Go through each parent on this current level
    		for (Capitalist c : parents)
    		{
    			// Add the current capitalist you are on to the set of capitalists to return
    			elements.add(c);
    			Map<Capitalist, Map> nextLayer = currentLayer.get(c);
    			// If they have children below them
    			if (nextLayer != null)
    			{
    				// Add that layer to the layers to look at queue
    				layersToLookAt.add(nextLayer);
    			}
    		}
        }
        
        return elements;
    }

    /**
     * @return all parent elements in the hierarchy,
     * or an empty set if no parents have been added to the hierarchy
     */
    @Override
    public Set<FatCat> getParents() {
        throw new NotImplementedException();
    }

    /**
     * @param fatCat the parent whose children need to be returned
     * @return all elements in the hierarchy that have the given parent as a direct parent,
     * or an empty set if the parent is not present in the hierarchy or if there are no children
     * for the given parent
     */
    @Override
    public Set<Capitalist> getChildren(FatCat fatCat) {
        throw new NotImplementedException();
    }

    /**
     * @return a map in which the keys represent the parent elements in the hierarchy,
     * and the each value is a set of the direct children of the associate parent, or an
     * empty map if the hierarchy is empty.
     */
    @Override
    public Map<FatCat, Set<Capitalist>> getHierarchy() {
        throw new NotImplementedException();
    }

    /**
     * @param capitalist
     * @return the parent chain of the given element, starting with its direct parent,
     * then its parent's parent, etc, or an empty list if the given element has no parent
     * or if its parent is not in the hierarchy
     */
    @Override
    public List<FatCat> getParentChain(Capitalist capitalist) {
    	// Contains the chain of parents for the capitalist to add all the way to the top
    	List<FatCat> parentChain = new ArrayList<>();
    	
    	// While we are not at the top of the hierarchy
    	while(capitalist.getParent() != null)
    	{
    		// Add the current parent to the parent chain
    		parentChain.add(capitalist.getParent());
    		capitalist = capitalist.getParent();
    	}
    	
    	return parentChain;
    }
}
