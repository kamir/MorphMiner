/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bitocean.mm.contextualizer;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author kamir
 */
public class SimpleGeoCoder {

    Geocoder geocoder = new Geocoder();

    public static void main(String[] args) throws IOException {

        SimpleGeoCoder sgc = new SimpleGeoCoder();
        
        String lang = "en";
        
        // double[] latLon = sgc.getLatLonAsDouble( "Paris, France", lang );
        double[] latLon = sgc.getLatLonAsDouble( "Frankleben, Germany", lang );
        
        System.out.println( "LAT:" + latLon[0] + " # " + "LON:" + latLon[1] );
    }

    private static String handleRequest(GeocoderResult r) {
        GeocoderGeometry results = r.getGeometry();
        LatLng loc = results.getLocation();
        String s = loc.toString() + "#" + loc.toUrlValue();
        return loc.toUrlValue();
    }

    public double[] getLatLonAsDouble(String a,String lang) throws IOException {
        double[] v = new double[2];
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(a).setLanguage(lang).getGeocoderRequest();
        GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
        List<GeocoderResult> results = geocoderResponse.getResults();
        String[] s1 = {"",""};
        for( GeocoderResult r : results ) {
            String s = handleRequest( r );
            s1 = s.split(",");
        }
        v[0] = Double.parseDouble( s1[0] );
        v[1] = Double.parseDouble( s1[1] );
        return v;
    }

}
