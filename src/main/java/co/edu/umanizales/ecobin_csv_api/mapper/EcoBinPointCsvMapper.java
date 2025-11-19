package co.edu.umanizales.ecobin_csv_api.mapper;

import co.edu.umanizales.ecobin_csv_api.model.EcoBinPoint;
import co.edu.umanizales.ecobin_csv_api.model.Location;

/**
 * Mapper para EcoBinPoint.
 * CSV: id,name,lat,lon,address
 */
public class EcoBinPointCsvMapper {

    public EcoBinPoint fromCsv(String[] c) {
        long id = Long.parseLong(c[0]);
        String name = c[1];
        double lat = Double.parseDouble(c[2]);
        double lon = Double.parseDouble(c[3]);
        String address = c[4];

        Location location = new Location(lat, lon, address);

        EcoBinPoint point = new EcoBinPoint();
        point.setId(id);
        point.setName(name);
        point.setLocation(location);
        return point;
    }

    public String[] toCsv(EcoBinPoint p) {
        return new String[] {
                String.valueOf(p.getId()),
                p.getName(),
                String.valueOf(p.getLocation().lat()),
                String.valueOf(p.getLocation().lon()),
                p.getLocation().address()
        };
    }
}
