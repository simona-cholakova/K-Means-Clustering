package org.example;

import java.util.List;
import java.util.ArrayList;

public class RegionData {

    public static List<Region> getLandRegions() {
        List<Region> landRegions = new ArrayList<>();

        landRegions.add(new Region(47.7, 16.8, 49.6, 22.6)); //Slovakia
        landRegions.add(new Region(45.8, 16.0, 48.5, 22.6)); //Hungary
        landRegions.add(new Region(48.5, 12.1, 51.1, 18.9)); //Czech Republic
        landRegions.add(new Region(45.8, 5.9, 47.8, 10.5));  //Switzerland
        landRegions.add(new Region(49.0, 14.5, 54.0, 23.8)); //Poland
        landRegions.add(new Region(46.3, 9.5, 49.0, 17.2));  //Austria
        landRegions.add(new Region(49.4, 5.7, 50.2, 6.4));   //Luxembourg
        landRegions.add(new Region(49.5, 2.5, 51.5, 5.5));   //Belgium
        landRegions.add(new Region(43.5, 0.5, 49.5, 7.8));   //Rouen, Tours, Montpellier, Turin Region
        landRegions.add(new Region(41.5, 19.0, 46.5, 23.0)); //Serbia
        landRegions.add(new Region(43.6, 20.9, 48.3, 29.5)); //Romania
        landRegions.add(new Region(45.5, 13.3, 46.8, 16.7)); //Slovenia
        landRegions.add(new Region(44.8, 8.0, 46.8, 12.0));  //Milan, Verona, Padova region
        landRegions.add(new Region(41.5, 20.4, 42.5, 23.2)); //Macedonia
        landRegions.add(new Region(41.2, 22.3, 44.2, 28.0)); //Bulgaria
        landRegions.add(new Region(51.0, 23.5, 56.0, 30.5)); //Belarus
        landRegions.add(new Region(45.5, 26.0, 49.5, 30.0)); //Moldova
        landRegions.add(new Region(48.0, 27.5, 51.5, 34.5)); //region including Zhytomyr, Poltava, and surrounding areas
        landRegions.add(new Region(50.0, 25.5, 51.0, 27.5)); //Rivne and Lutsk Region (Ukraine)

        return landRegions;
    }
}
