package amie.rules.eval;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javatools.datatypes.Integer;
import javatools.datatypes.IntHashMap;
import javatools.datatypes.Pair;

import amie.data.KB;

/**
 * For each relation r of a given KB, it takes a sample of the entities x in the domain of the relation r
 * and outputs all the facts of the form r(x,y)
 * @author galarrag
 *
 */
public class EntitiesRelationSampler {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		KB db = new KB();
		int maxOccurrencePerRelation = Integer.parseInt(args[0]);		
		db.load(new File(args[1]));
		IntSet allEntities = db.selectDistinct(KB.map("?s"), KB.triples(KB.triple(KB.map("?s"), KB.map("?p"), KB.map("?o"))));
		IntList allRelations = new IntArrayList(db.selectDistinct(KB.map("?p"), KB.triples(KB.triple(KB.map("?s"), KB.map("?p"), KB.map("?o")))));
		IntList entitiesArray = new IntArrayList(allEntities); 
		Int2ObjectMap<List<IntPair>> relationsMap = new Int2ObjectOpenHashMap<List<IntPair>>();
		Int2IntMap relationEntityCount = new Int2IntOpenHashMap();
		
		for(int relation: allRelations){
			relationEntityCount.put(relation, 0);
			relationsMap.put(relation, new ArrayList<IntPair>());
		}
		
		while(!allRelations.isEmpty() && !entitiesArray.isEmpty()){
			//Pick a random entity
			Random r = new Random();
			int position = r.nextInt(entitiesArray.size());
			int entity = entitiesArray.get(position);
			swap(entitiesArray, position, entitiesArray.size() - 1);
			entitiesArray.remove(entitiesArray.size() - 1);
			
			//Now take all the triples about this entity
			List<int[]> query = KB.triples(KB.triple(entity, KB.map("?p"), KB.map("?o")));
			Int2ObjectMap<IntSet> predicateObjects = db.selectDistinct(KB.map("?p"), KB.map("?o"), query);
			
			for(int relation: predicateObjects.keySet()){				
				if(relationEntityCount.get(relation) >= maxOccurrencePerRelation){
					continue;
				}
				
				relationEntityCount.increase(relation);
				List<IntPair> facts = relationsMap.get(relation);
				for(int object: predicateObjects.get(relation)){
					facts.add(new IntPair(entity, object));
				}
				
				if(relationEntityCount.get(relation) >= maxOccurrencePerRelation)
					allRelations.remove(relation);
			}
		}
		
		for(int relation: relationsMap.keySet()){
			for(IntPair entityObject: relationsMap.get(relation))
				System.out.println(entityObject.first + "\t" + relation + "\t" + entityObject.second);
		}
		
	}

	private static void swap(IntList entitiesArray, int i, int j) {
		int tmp = entitiesArray.get(i);
		entitiesArray.set(i, entitiesArray.get(j));
		entitiesArray.set(j, tmp);
	}

}
