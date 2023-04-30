package be.vanoosten.esa.tools;

import be.vanoosten.esa.WikiIndexer;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

/**
 *
 * @author Philip van Oosten
 */
public class ConceptVector {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ConceptVector.class);
    Map<String, Float> conceptWeights;

    ConceptVector(TopDocs td, IndexReader indexReader) throws IOException {
        double norm = 0.0;
        conceptWeights = new HashMap<>();
        for (ScoreDoc scoreDoc : td.scoreDocs) {
            norm += scoreDoc.score * scoreDoc.score;
            String concept = indexReader.document(scoreDoc.doc).get(WikiIndexer.TITLE_FIELD);
            conceptWeights.put(concept, scoreDoc.score);
        }
        for (String concept : conceptWeights.keySet()) {
            conceptWeights.put(concept, (float) (conceptWeights.get(concept) / norm));
        }
    }

    public float dotProduct(ConceptVector other) {
        Set<String> commonConcepts = new HashSet<>(other.conceptWeights.keySet());
        commonConcepts.retainAll(conceptWeights.keySet());
        double dotProd = 0.0;
        for (String concept : commonConcepts) {
            dotProd += conceptWeights.get(concept) * other.conceptWeights.get(concept);
        }
        return (float) dotProd;
    }
    private  <K, V> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Object>() {
            @SuppressWarnings("unchecked")
            public int compare(Object o1, Object o2) {
                return ((Comparable<V>) ((Map.Entry<K, V>) (o1)).getValue()).compareTo(((Map.Entry<K, V>) (o2)).getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Iterator<Entry<K, V>> it = list.iterator(); it.hasNext();) {
            Map.Entry<K, V> entry = (Map.Entry<K, V>) it.next();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
    public Iterator<String> topConcepts(int n) {
    	int size = 0;
    	StringBuilder sb = new StringBuilder();
        Iterator<Entry<String, Float>> iter = sortByValue(conceptWeights).entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, Float> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append("\n");
            }
            size ++;
        }
        log.info(sb);
        return conceptWeights.entrySet().stream().
                sorted((Map.Entry<String, Float> e1, Map.Entry<String, Float> e2) -> (int) Math.signum(e1.getValue() - e2.getValue())).
                map(e -> e.getKey()).
                iterator();
    }

    public Query asQuery() {
        BooleanQuery relatedTermsQuery = new BooleanQuery();
        for (Map.Entry<String, Float> entry : conceptWeights.entrySet()) {
            String concept = entry.getKey();
            TermQuery conceptAsTermQuery = new TermQuery(new Term("concept", concept));
            conceptAsTermQuery.setBoost(entry.getValue());
            relatedTermsQuery.add(conceptAsTermQuery, BooleanClause.Occur.SHOULD);
        }
        return relatedTermsQuery;
    }
}
