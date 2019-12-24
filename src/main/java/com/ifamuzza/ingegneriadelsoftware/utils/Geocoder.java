package com.ifamuzza.ingegneriadelsoftware.utils;

import java.util.List;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageResult;

public class Geocoder {
  private static JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("94fb6718402f47f4be7355f37ed151cc");

  public static JOpenCageResult forward(String address) {
    JOpenCageForwardRequest request = new JOpenCageForwardRequest(address);
    request.setMinConfidence(1);
    request.setNoAnnotations(false);
    request.setNoDedupe(true);
    JOpenCageResponse response = jOpenCageGeocoder.forward(request);
    List<JOpenCageResult> results = response.getResults();
    return results.size() > 0 ? results.get(0) : null;
  }

  public static Double distance(double lat1, double lat2, double lon1, double lon2) {

    final int R = 6371;

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c * 1000;
  }

}