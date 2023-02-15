package ar.edu.itba.paw.webapp.adapter;


import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.net.URI;
import java.util.HashMap;

public class CreatorAdapter extends XmlAdapter<CreatorAdapter.CreatorDto, HashMap<String, String>> {

    @XmlRootElement
    public static class CreatorDto {

//        public boolean hasImage;
//        public URI image;
//        public URI self;
        public long id;
        public String nameOrEmail;
        public String tier;
    }


    @Override
    public HashMap<String,String> unmarshal(CreatorDto p) throws Exception {
        HashMap<String, String> map = new HashMap<>();
//        map.put("hasImage", Boolean.toString(p.hasImage));
//        if (p.hasImage){
//            map.put("image", p.image.toString());
//        }
//        map.put("self", p.self.toString());
        map.put("id", Long.toString(p.id));
        map.put("nameOrEmail", p.nameOrEmail);
        map.put("tier", p.tier);
        return map;
    }


    @Override
    public CreatorDto marshal(HashMap<String, String> v) throws Exception {
        CreatorDto p = new CreatorDto();
        p.tier = v.get("tier");
        p.nameOrEmail = v.get("nameOrEmail");
//        p.self = new URI(v.get("self"));
        p.id = Long.parseLong(v.get("id"));
//        p.hasImage = Boolean.parseBoolean(v.get("hasImage"));
//        if (p.hasImage) {
//            p.image = new URI(v.get("image"));
//        }
        return p;
    }
}