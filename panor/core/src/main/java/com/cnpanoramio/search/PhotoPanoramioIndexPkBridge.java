package com.cnpanoramio.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.document.Fieldable;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;

import com.cnpanoramio.domain.PhotoPanoramioIndexPK;
@Deprecated
public class PhotoPanoramioIndexPkBridge implements TwoWayFieldBridge {

	@Override
	public void set(String name, Object value, Document document,
			LuceneOptions luceneOptions) {
		PhotoPanoramioIndexPK id = (PhotoPanoramioIndexPK) value;
        Store store = luceneOptions.getStore();
        Index index = luceneOptions.getIndex();
        TermVector termVector = luceneOptions.getTermVector();
        Float boost = luceneOptions.getBoost();

        Field field = new Field( name + ".level", id.getLevel().toString(),   //store each sub property in a field
                        store, index, termVector );
        field.setBoost( boost );
        document.add( field );
        
        field = new Field( name + ".south", id.getSouth().toString(), 
                        store, index, termVector );
        field.setBoost( boost );
        document.add( field );
        
        field = new Field( name + ".west", id.getWest().toString(), 
        		store, index, termVector );
        field.setBoost( boost );
        document.add( field );

        field = new Field( name, objectToString( id ),   //store unique representation in named field
                        store, index, termVector );
        field.setBoost( boost );
        document.add( field );

	}

	@Override
	public Object get(String name, Document document) {
		PhotoPanoramioIndexPK id = new PhotoPanoramioIndexPK();

		Fieldable field = document.getFieldable( name + ".level" );
        id.setLevel( Integer.valueOf(field.stringValue()) );
        
        field = document.getFieldable( name + ".south" );
        id.setSouth( Double.valueOf(field.stringValue()) );
        field = document.getFieldable( name + ".west" );
        id.setWest( Double.valueOf(field.stringValue()) );
        return id;
	}

	@Override
	public String objectToString(Object object) {
		PhotoPanoramioIndexPK id = (PhotoPanoramioIndexPK) object;
		StringBuilder sb = new StringBuilder();
        sb.append( id.getLevel() )
                .append( " " )
                .append( id.getSouth() )
                .append( " " )
                .append( id.getWest() );

        return sb.toString();
	}

}
