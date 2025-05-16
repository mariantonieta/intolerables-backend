package anto.es.intolerables.dto;
import java.util.List;

public class YelpDto {
    private List<Business> businesses;

    // Getters y Setters manuales
    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public static class Business {
        private String id;
        private String name;
        private Location location;
        private List<Category> categories;
        private Coordinates coordinates;
        private String image_url;
        private String url;

        // Getters y Setters manuales
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

        public Coordinates getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
        }

        public String getImageUrl() {
            return image_url;
        }

        public void setImageUrl(String image_url) {
            this.image_url = image_url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Location {
        private String address1;

        // Getters y Setters manuales
        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }
    }

    public static class Category {
        private String title;

        // Getters y Setters manuales
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class Coordinates {
        private double latitude;
        private double longitude;

        // Getters y Setters manuales
        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}
